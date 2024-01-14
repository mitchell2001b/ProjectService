package com.example.ProjectService.repositories;

import com.example.ProjectService.Project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProjectRepository extends JpaRepository<Project, Long>
{
    @Query(value = "SELECT * FROM `project` ORDER BY `id` DESC LIMIT 1", nativeQuery = true)
    public Project findLastCreatedProject();

    @Query(value = "SELECT * FROM project WHERE owner_id = ?1", nativeQuery = true)
    public List<Project> getAllProjectsFromOwner(long ownerId);

    @Query(value = "DELETE FROM project WHERE owner_id = ?1", nativeQuery = true)
    @Modifying
    public void deleteAllProjectsFromOwner(Long ownerId);


    /*@Query(value = "SELECT * FROM `project` p " +
            "INNER JOIN `project_account` pa ON p.id = pa.project_id " +
            "INNER JOIN `account` a ON pa.account_id = a.id " +
            "WHERE p.id = ?1", nativeQuery = true)
    List<Account> findProjectMembersByProjectId(Integer projectId);*/


}
