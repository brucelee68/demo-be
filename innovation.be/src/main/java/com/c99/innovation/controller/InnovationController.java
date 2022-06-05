package com.c99.innovation.controller;

import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.request.*;
import com.c99.innovation.dto.response.CommentResponse;
import com.c99.innovation.dto.response.InnovationDetailResponse;
import com.c99.innovation.dto.sortingfiltering.InnovationFilter;
import com.c99.innovation.dto.sortingfiltering.PageDTO;
import com.c99.innovation.entity.CommentInnovation;
import com.c99.innovation.service.InnovationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.c99.innovation.common.constant.Constants.API_V1;
import static com.c99.innovation.common.constant.Constants.BAD_REQUEST_COMMON_MESSAGE;
import static com.c99.innovation.common.constant.Constants.CREATE_SUCCESSFULLY;
import static com.c99.innovation.common.constant.Constants.DELETE_SUCCESSFULLY;
import static com.c99.innovation.common.constant.Constants.FETCHING_SUCCESSFULLY;
import static com.c99.innovation.common.constant.Constants.UPDATE_SUCCESSFULLY;

@Api(value = "Innovation Controller")
@RestController
@RequestMapping(value = API_V1 + "/innovation")
public class InnovationController {

    private InnovationService innovationService;

    @Autowired
    public InnovationController(InnovationService innovationService) {
        this.innovationService = innovationService;
    }

    @ApiOperation(value = "Create a new innovation/improvement/idea")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CREATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PostMapping()
    public ResponseDTO<InnovationDetailResponse> createInnovation(
            @Valid @RequestBody InnovationCreationRequest innovationCreationRequest) {
        InnovationDetailResponse innovationDetailResponse = this.innovationService.createInnovation(innovationCreationRequest);
        return new ResponseDTO<>(HttpStatus.OK, CREATE_SUCCESSFULLY, innovationDetailResponse);
    }

    @ApiOperation(value = "Get a innovation/improvement/idea")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/{id}")
    public ResponseDTO<InnovationDetailResponse> getOneInnovation(
            @PathVariable(name = "id") Long id) {
        InnovationDetailResponse innovationDetailResponse = this.innovationService.findById(id);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, innovationDetailResponse);
    }

    @ApiOperation(value = "Get list innovation/improvement/idea based on criteria")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping()
    public ResponseDTO<PageDTO<InnovationDetailResponse>> getAll(
            @RequestParam(value = "projects", required = false) Optional<List<String>> projects,
            @RequestParam(value = "types", required = false) Optional<List<Long>> types,
            @RequestParam(value = "areas", required = false) Optional<List<Long>> areas,
            @RequestParam(value = "status", required = false) Optional<List<Status>> status,
            @RequestParam(value = "own", required = false) Optional<Boolean> own,
            @RequestParam(value = "q", required = false) Optional<String> keyword,
            @RequestParam(value = "_order", required = false) Optional<String> order,
            @RequestParam(value = "_sort", required = false) Optional<String> sort,
            @RequestParam(value = "limit", required = false) Optional<Integer> pageSize,
            @RequestParam(value = "page", required = false) Optional<Integer> pageIndex) {

        InnovationFilter innovationFilter = new InnovationFilter(types, projects, areas, status, own.orElse(false), true);
        PageDTO<InnovationDetailResponse> detailResponsePageDTO = this.innovationService.getAll(innovationFilter, keyword, sort, order, pageSize, pageIndex);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, detailResponsePageDTO);
    }

    @ApiOperation(value = "Update a new innovation/improvement/idea")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PutMapping("/{id}")
    public ResponseDTO<InnovationDetailResponse> updateInnovation(
            @Valid @RequestBody InnovationUpdatingRequest innovationUpdateRequest,
            @PathVariable(name = "id") Long id) {
        InnovationDetailResponse innovationDetailResponse = this.innovationService.updateInnovation(id, innovationUpdateRequest);
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, innovationDetailResponse);
    }

    @ApiOperation(value = "Delete a new innovation/improvement/idea")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = DELETE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @DeleteMapping("/{id}")
    public ResponseDTO<Object> deleteInnovation(@PathVariable(name = "id") Long id) {
        this.innovationService.deleteInnovation(id);
        return new ResponseDTO<>(HttpStatus.OK, DELETE_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "Approve an innovation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PatchMapping("/approve/{id}")
    public ResponseDTO<Object> approveAnInnovation(@PathVariable(name = "id") Long id) {
        this.innovationService.approveAnInnovation(id);
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "Reject an innovation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PatchMapping("/reject/{id}")
    public ResponseDTO<Object> rejectAnInnovation(@PathVariable(name = "id") Long id) {
        this.innovationService.rejectAnInnovation(id);
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "increase clap in an innovation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PatchMapping("/clap/{id}")
    public ResponseDTO<Object> increaseClapInAnInnovation(@PathVariable(name = "id") Long id) {
        Long result = this.innovationService.increaseClapInAnInnovation(id);
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, result);

    }

    @ApiOperation(value = "Get a innovation/improvement/idea")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/count/clap/{id}")
    public ResponseDTO<Long> getClapInnovation(
            @PathVariable(name = "id") Long id) {
        Long clapInnovationResponse = this.innovationService.getClapInnovation(id);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, clapInnovationResponse);
    }
    
    @ApiOperation(value = "create a comment in an innovation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CREATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PostMapping("/comment")
    public ResponseDTO<Object> createCommentInAnInnovation(
            @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        CommentInnovation commentInnovation = this.innovationService.createComment(commentCreateRequest.getContent(), Long.parseLong(commentCreateRequest.getInnovationId()));
        return new ResponseDTO<>(HttpStatus.OK, CREATE_SUCCESSFULLY, "commentInnovation");
    }

    @ApiOperation(value = "Get all comment of an innovation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/comment/{id}")
    public ResponseDTO<List<CommentResponse>> getCommentsByInnovationId(
            @PathVariable(name = "id") Long id) {
        List<CommentResponse> commentInnovationList = this.innovationService.getAllCommentByInnovationId(id);
        return new ResponseDTO<List<CommentResponse>>(HttpStatus.OK, FETCHING_SUCCESSFULLY, commentInnovationList);
    }

    @ApiOperation(value = "delete an comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = DELETE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PatchMapping("/comment/delete/{id}")
    public ResponseDTO<Object> deleteAnComment(@PathVariable(name = "id") Long id) {
        this.innovationService.deleteAnComment(id);
        return new ResponseDTO<>(HttpStatus.OK, DELETE_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PutMapping("/comment/update")
    public ResponseDTO<CommentResponse> updateComment(
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
        this.innovationService.updateAnComment(Long.parseLong(commentUpdateRequest.getId()), commentUpdateRequest.getContent());
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, null);
    }
}
