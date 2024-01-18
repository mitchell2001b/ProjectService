package com.example.ProjectService.Project;

import com.example.ProjectService.AzureServices.KeyVaultService;
import com.example.ProjectService.dtos.ProjectDto;
import com.example.ProjectService.dtos.ProjectMemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


import io.micrometer.core.instrument.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/projects")
public class ProjectController
{
    private static final String ENVIRONMENT_PROPERTY = "spring.profiles.active";
    private static final String TEST_PROFILE = "test";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    private final MeterRegistry microMeterRegistry;

    private final KeyVaultService keyVaultService;

    private final Environment environment;

    private Counter createCallsCounter;

    private Timer httpRequestsTimer;
    private Gauge httpRequestsPerSecondGauge;
    private String secretKey;
    @Autowired
    public ProjectController(ProjectService service, MeterRegistry meterRegistry, KeyVaultService vaultService, Environment envi)
    {
        this.projectService = service;
        this.microMeterRegistry = meterRegistry;
        this.keyVaultService = vaultService;
        this.environment = envi;

        this.httpRequestsTimer = Timer.builder("http.requests.timer.create")
                .publishPercentileHistogram()
                .register(meterRegistry);


        this.httpRequestsPerSecondGauge = Gauge.builder("http.requests.per.second.create", this, value -> {
                    double totalCount = httpRequestsTimer.count();
                    double totalTimeSeconds = httpRequestsTimer.totalTime(TimeUnit.SECONDS);
                    return totalCount / totalTimeSeconds;
                })
                .register(meterRegistry);
    }






    public static boolean isTestingEnvironment()
    {
        String activeProfiles = System.getProperty(ENVIRONMENT_PROPERTY);
        return activeProfiles != null && activeProfiles.contains(TEST_PROFILE);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> CreateProject(@RequestBody ProjectDto newProject, @RequestHeader(name = "Authorization") String authorizationHeader)
    {
        createCallsCounter = microMeterRegistry.counter("project.create.calls", "type", "project");

        ResponseEntity<String> responseEntity = httpRequestsTimer.record(() -> {

            String jwtToken = authorizationHeader.replace("Bearer ", "");
            if (environment.matchesProfiles("test")) {
                secretKey = "MockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectService";
            } else {
                secretKey = keyVaultService.getSecretValue("semester6key");
            }

            Project project = null;

            createCallsCounter.increment();
            SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> jws;
            try {
                jws = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(jwtToken);

            } catch (JwtException e) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Not authorized");
            }


            Claims claims = jws.getBody();
            String roleName = (String) claims.get("roleName");


            if (!"user".equals(roleName) && !"admin".equals(roleName)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Not authorized");
            }
            try {
                project = projectService.CreateProject(newProject);

            } catch (Exception e) {
                LOGGER.error("Error creating project", e);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.toString());
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Project created successfully");

        });

        return responseEntity;
    }


    /*@PostMapping(value = "/myprojects")
    public ResponseEntity<List<ProjectDto>> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto)
    {
        if(ownerDto == null)
        {
            return ResponseEntity.badRequest().build();
        }
        List<ProjectDto> dtos = null;

        try
        {
            dtos = this.projectService.GetAllProjectsFromOwner(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisConnectionFailureException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisConnectionException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisCommandTimeoutException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (Exception e)
        {
            LOGGER.error("Error getting my projects", e);
        }
        return null;

    }*/
    @PostMapping(value = "/myprojects")
    public CompletableFuture<ResponseEntity<List<ProjectDto>>> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto, @RequestHeader(name = "Authorization") String authorizationHeader) {

        microMeterRegistry.counter("project_myprojects_http_requests_total_amount", "endpoint", "/actuator/prometheus").increment();
        if (ownerDto == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        if (environment.matchesProfiles("test"))
        {
            secretKey = "MockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectService";
        }
        else
        {
            secretKey = keyVaultService.getSecretValue("semester6key");
        }
        SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        Jws<Claims> jws;
        try
        {
            jws = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(jwtToken);

        }
        catch (JwtException e)
        {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
        }

        Claims claims = jws.getBody();
        String roleName = (String) claims.get("roleName");
        Long userId = ((Number) claims.get("id")).longValue();


        if (!"user".equals(roleName) && !"admin".equals(roleName) && !userId.equals(ownerDto.GetId()))
        {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
        }

        return this.projectService.GetAllProjectsFromOwnerInDbAsync(ownerDto)
                .thenApply(dtos -> ResponseEntity.ok(dtos))
                .exceptionally(ex -> {
                    LOGGER.error("Error getting my projects", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
    @GetMapping(value = "/tess")
    public String tess()
    {
        return "tes dit is tes";
    }
}



