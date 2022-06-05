package com.c99.innovation.repository;

import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.InnovationArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InnovationAreaRepository extends JpaRepository<InnovationArea, Long> {

    @Query("SELECT i.area FROM InnovationArea i WHERE i.innovation.id = :innovationId")
    List<Area> findAllAreaByInnovationId(@Param("innovationId") Long innovationId);


    @Modifying
    @Query("DELETE FROM InnovationArea i WHERE i.innovation.id = :innovationId")
    void removeAllByInnovationId(@Param("innovationId") Long innovationId);
}
