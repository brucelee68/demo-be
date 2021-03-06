package com.c99.innovation.repository;

import com.c99.innovation.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByInnovationId(Long innovationId);

    Integer removeByUniqueNameEquals(String uniqueName);
}
