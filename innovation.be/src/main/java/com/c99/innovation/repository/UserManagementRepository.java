package com.c99.innovation.repository;

import com.c99.innovation.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManagementRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT a.username, a.password, a.role " +
            "FROM Account a ")
    List<Account> getAllUser();

    @Override
    Page<Account> findAll(Pageable pageable);
}
