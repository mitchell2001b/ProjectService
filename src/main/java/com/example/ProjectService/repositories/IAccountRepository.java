package com.example.ProjectService.repositories;

import com.example.ProjectService.Account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, Integer>
{

}
