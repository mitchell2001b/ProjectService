package com.example.ProjectService.repositories;

import com.example.ProjectService.ProjectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<ProjectMember, Long>
{

}
