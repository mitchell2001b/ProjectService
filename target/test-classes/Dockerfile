FROM maven:3.9.4-jdk-17-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:latest
COPY target/ProjectService-0.0.1-SNAPSHOT.jar ProjectService-0.0.1-SNAPSHOT.jar
EXPOSE 8282
ENTRYPOINT ["java", "-jar", "ProjectService-0.0.1-SNAPSHOT.jar"]