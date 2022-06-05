package com.c99.innovation.controller;

import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.request.TeamCreateRequest;
import com.c99.innovation.dto.request.TeamUpdateRequest;
import com.c99.innovation.entity.Team;
import com.c99.innovation.service.TeamService;
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

@Api(value = "Team Controller")
@RestController
@RequestMapping(value = API_V1 + "/team")
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @ApiOperation(value = "Get all teams")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping()
    public ResponseDTO<List<Team>> getAllTeams() {
        List<Team> allTeams = this.teamService.getAll();
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, allTeams);
    }

    @ApiOperation(value = "create a team")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PostMapping()
    public ResponseDTO<String> createTeam(
            @Valid @RequestBody TeamCreateRequest teamCreateRequest) {
        String result = this.teamService.createTeam(teamCreateRequest.getNewTeam().trim(), teamCreateRequest.getNewProjectName().trim());
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, result);
    }

    @ApiOperation(value = "Delete a Team")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @DeleteMapping("/{id}")
    public ResponseDTO<Object> deleteTeam(@PathVariable(name = "id") Long id) {
         String deleteATeam = this.teamService.deleteTeam(id);
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, deleteATeam);
    }

    @ApiOperation(value = "Update a team")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PutMapping("/{id}")
    public ResponseDTO<String> updateTeam(
            @Valid @RequestBody TeamUpdateRequest teamUpdateRequest,
            @PathVariable(name = "id") Long id) {
        String result = this.teamService.editTeam(id, teamUpdateRequest.getNewTeam().trim(), teamUpdateRequest.getNewProjectName().trim());
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, result);
    }

    @ApiOperation(value = "Get all active teams")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping("/active-team")
    public ResponseDTO<Map<String, Object>> getAllActiveTeams(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1") int size) {
        Map<String, Object> teams = teamService.getAllActiveTeam(page, size);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, teams);
    }
}
