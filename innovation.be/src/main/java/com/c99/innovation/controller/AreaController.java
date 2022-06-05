package com.c99.innovation.controller;

import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.request.AreaCreateRequest;
import com.c99.innovation.dto.request.AreaUpdateRequest;
import com.c99.innovation.entity.Area;
import com.c99.innovation.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.c99.innovation.common.constant.Constants.*;

@Api(value = "Area Controller")
@RestController
@RequestMapping(value = API_V1 + "/area")
public class AreaController {

    private AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @ApiOperation(value = "Get all areas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping()
    public ResponseDTO<List<Area>> getAllAreas() {
        List<Area> allArea = this.areaService.getAll();
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, allArea);
    }

    @ApiOperation(value = "Delete a area")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @DeleteMapping("/{id}")
    public ResponseDTO<Object> deleteArea(@PathVariable(name = "id") Long id) {
        this.areaService.deleteArea(id);
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "create a area")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PostMapping()
    public ResponseDTO<String> createArea(
            @Valid @RequestBody AreaCreateRequest areaCreateRequest) {
        String result = this.areaService.createArea(areaCreateRequest.getNewArea().trim());
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, result);
    }

    @ApiOperation(value = "Update a area")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PutMapping()
    public ResponseDTO<String> updateArea(
            @Valid @RequestBody AreaUpdateRequest areaUpdateRequest) {
        String result = this.areaService.updateArea(Long.parseLong(areaUpdateRequest.getId()), areaUpdateRequest.getNewName().trim());
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, result);
    }

    @ApiOperation(value = "Get all active areas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping("/active-area")
    public ResponseDTO<Map<String, Object>> getAllActiveTeams(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1") int size) {
        Map<String, Object> areas = areaService.getAllActiveArea(page, size);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, areas);
    }
}
