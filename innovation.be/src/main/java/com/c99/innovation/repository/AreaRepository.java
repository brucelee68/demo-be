package com.c99.innovation.repository;

import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    boolean existsByName(String username);

    @Override
    Page<Area> findAll(Pageable pageable);

    @Query("SELECT a\n" +
            "FROM Area a\n" +
            "WHERE a.status = com.c99.innovation.common.enumtype.StatusType.ACTIVE")
    List<Area> getAllActiveArea();


    Page<Area> findAllByStatus(StatusType statusType, Pageable pageable);
}
