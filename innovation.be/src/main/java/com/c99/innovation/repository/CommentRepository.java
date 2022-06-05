package com.c99.innovation.repository;

import com.c99.innovation.entity.CommentInnovation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentInnovation, Long> {

    @Query("SELECT ci\n" +
            "FROM CommentInnovation ci\n" +
            "WHERE ci.innovation.id = :innovationId AND ci.status = com.c99.innovation.common.enumtype.StatusType.ACTIVE\n" +
            "ORDER BY ci.id DESC")
    List<CommentInnovation> getAllCommentByInnovationId(@Param("innovationId") Long innovationId);

    @Query("SELECT ci\n" +
            "FROM CommentInnovation ci\n" +
            "WHERE ci.status = com.c99.innovation.common.enumtype.StatusType.ACTIVE\n" +
            "ORDER BY ci.lastModifiedAt DESC\n")
    List<CommentInnovation> getLatestComments(Pageable pageable);

}
