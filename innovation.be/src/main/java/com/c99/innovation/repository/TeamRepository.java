package com.c99.innovation.repository;

import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.Account;
import com.c99.innovation.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Override
    Page<Team> findAll(Pageable pageable);

    @Query("SELECT t\n" +
            "FROM Team t\n" +
            "WHERE t.status = com.c99.innovation.common.enumtype.StatusType.ACTIVE")
    List<Team> getAllActiveTeam();

    Page<Team> findAllByStatus(StatusType statusType, Pageable pageable);
}
