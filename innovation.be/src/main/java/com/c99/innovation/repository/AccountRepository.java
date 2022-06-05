package com.c99.innovation.repository;

import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Load an entire optional account object
     *
     * @param username
     * @return optional account object
     */
    Optional<Account> findByUsernameIgnoreCase(String username);

    boolean existsByUsername(String username);

    Optional<Account> findByRefreshToken(String token);

    @Query(value = "SELECT a.username, a.role.id " +
            "FROM Account a WHERE a.status = 1")
    Page<Account> getAllActiveUser(Pageable pageable);

    Page<Account> findAllByStatus(StatusType statusType, Pageable pageable);
}
