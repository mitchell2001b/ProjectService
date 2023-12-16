package com.example.ProjectService.repositories;

import com.example.ProjectService.ProjectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAccountRepository extends JpaRepository<ProjectMember, Long>
{
    @Query(value = "SELECT * FROM `projectmember` WHERE id = ?1 AND email = ?2", nativeQuery = true)
    public ProjectMember FindProjectMember(long id, String email);
}
