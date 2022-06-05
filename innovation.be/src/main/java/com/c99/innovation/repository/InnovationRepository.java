package com.c99.innovation.repository;


import com.c99.innovation.dto.response.LatestOwnInnovation;
import com.c99.innovation.dto.response.MyInnovation;
import com.c99.innovation.dto.response.TopContributing;
import com.c99.innovation.dto.response.TopInnovationByClap;
import com.c99.innovation.entity.Innovation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InnovationRepository extends JpaRepository<Innovation, Long>, JpaSpecificationExecutor<Innovation> {

    @Query("SELECT i \n" +
            "FROM Innovation i \n" +
            "WHERE i.id = :id and i.status <> com.c99.innovation.common.enumtype.Status.DELETED")
    Optional<Innovation> findActiveInnovationById(@Param("id") Long id);

    @Query("SELECT COUNT(i.id) " +
            "FROM Innovation i " +
            "WHERE i.type.id = :typeId and i.status <> com.c99.innovation.common.enumtype.Status.DELETED")
    long getCountingByType(@Param("typeId") long typeId);

    @Query(value = "SELECT i " +
            "FROM Innovation i " +
            "WHERE i.status = com.c99.innovation.common.enumtype.Status.APPROVED \n" +
            "ORDER BY i.createdAt DESC")
    List<Innovation> getLatestInnovationAllTypes(Pageable pageable);

    @Query(value =
            "SELECT new com.c99.innovation.dto.response.TopContributing(t.projectName, COUNT(i.id)) \n" +
                    "FROM Innovation i join Team t on i.team.id = t.id \n" +
                    "WHERE i.status = com.c99.innovation.common.enumtype.Status.APPROVED \n" +
                    "GROUP BY t.projectName \n" +
                    "ORDER BY COUNT(i.id) DESC")
    List<TopContributing> getTopContributingProjectsAllTime(Pageable pageable);

    @Query(value =
            "SELECT new com.c99.innovation.dto.response.TopInnovationByClap(i.id, i.content, SUM(c.clap), i.type) \n" +
                    "FROM Innovation i join Clap c on i.id = c.innovation.id \n" +
                    "WHERE i.status = com.c99.innovation.common.enumtype.Status.APPROVED \n" +
                    "GROUP BY i.id \n" +
                    "ORDER BY SUM(c.clap) DESC")
    List<TopInnovationByClap> getTopInnovationsByClap(Pageable pageable);

    @Query(value = "SELECT new com.c99.innovation.dto.response.LatestOwnInnovation(i.id, t.name, i.content, i.createdAt) \n" +
            "FROM Innovation i JOIN Type t ON i.type.id = t.id \n" +
            "Where i.createdBy.username = :_username and i.status = com.c99.innovation.common.enumtype.Status.APPROVED \n" +
            "ORDER BY i.createdAt DESC")
    List<LatestOwnInnovation> getLatestInnovationByCurrentAccount(@Param("_username") String username, Pageable pageRequest);


    @Query(value = "SELECT new com.c99.innovation.dto.response.MyInnovation(i.id, i.content, te.projectName, t.name, i.createdAt, i.lastModifiedAt, i.status) \n" +
            "FROM Innovation i JOIN Team te ON i.team.id = te.id JOIN Type t ON i.type.id = t.id " +
            "JOIN Account ac ON i.createdBy.id = ac.id \n" +
            "WHERE ac.username = :_username AND i.status <> 3\n" +
            "ORDER BY i.lastModifiedAt DESC")
    List<MyInnovation> getMyInnovation(@Param("_username") String username);

    @Query(value = "SELECT a.name from Area a join InnovationArea ia ON ia.area.id = a.id \n" +
            "join Innovation i on ia.innovation.id = i.id where i.id = :id")
    List<String> getAreaByID(@Param("id") long id);

}
