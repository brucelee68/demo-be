package com.c99.innovation.repository;

import com.c99.innovation.entity.Clap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClapRepository extends JpaRepository<Clap, Long> {

    @Query("SELECT SUM(c.clap)\n" +
            "FROM Clap c\n" +
            "WHERE c.innovation.id = :innovationId AND c.status = com.c99.innovation.common.enumtype.StatusType.ACTIVE")
    Long countAllClapByInnovationId(@Param("innovationId") Long innovationId);

    @Query("SELECT c FROM Clap c WHERE c.innovation.id = :innovationId AND c.account.id = :accountId")
    Clap getClapByInnovationIdAndAccountId(@Param("innovationId") Long innovationId, @Param("accountId") Long accountId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Clap c SET clap = clap + 1 " +
                    "WHERE c.id = :id")
    void increaseInnovationClapByAccount(@Param("id") Long id);
}
