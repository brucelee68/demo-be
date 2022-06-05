package com.c99.innovation.service;

import com.c99.innovation.dto.request.InnovationCreationRequest;
import com.c99.innovation.dto.request.InnovationUpdatingRequest;
import com.c99.innovation.dto.response.CommentResponse;
import com.c99.innovation.dto.response.InnovationDetailResponse;
import com.c99.innovation.dto.sortingfiltering.InnovationFilter;
import com.c99.innovation.dto.sortingfiltering.PageDTO;
import com.c99.innovation.entity.CommentInnovation;

import java.util.List;
import java.util.Optional;

public interface InnovationService {

    InnovationDetailResponse createInnovation(InnovationCreationRequest request);

    InnovationDetailResponse findById(Long id);

    boolean deleteInnovation(long id);

    InnovationDetailResponse updateInnovation(Long id, InnovationUpdatingRequest innovationUpdateRequest);

    PageDTO<InnovationDetailResponse> getAll(InnovationFilter innovationFilter, Optional<String> keyword, Optional<String> sort, Optional<String> direction, Optional<Integer> pageSize, Optional<Integer> pageIndex);

    boolean approveAnInnovation(Long id);

    boolean rejectAnInnovation(Long id);

    Long increaseClapInAnInnovation(Long id);

    Long getClapInnovation(Long id);

    CommentInnovation createComment(String content, Long innovationId);

    boolean deleteAnComment(Long id);

    boolean updateAnComment(Long id, String content);

    List<CommentResponse> getAllCommentByInnovationId(Long id);
}
