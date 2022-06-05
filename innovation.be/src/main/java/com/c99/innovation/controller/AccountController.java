package com.c99.innovation.controller;

import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.request.AccountUpdateRequest;
import com.c99.innovation.dto.request.RegisterRequest;
import com.c99.innovation.dto.request.TeamCreateRequest;
import com.c99.innovation.entity.Account;
import com.c99.innovation.service.RegisterUserService;
import com.c99.innovation.service.impl.UserManagementServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.c99.innovation.common.constant.Constants.*;

@Api(value = "Account Controller")
@RestController
@RequestMapping(value = API_V1 + "/user-management")
public class AccountController {
    @Autowired
    private UserManagementServiceImpl userManagementService;

    @Autowired
    private RegisterUserService registerUserService;

    @ApiOperation(value = "Get all account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping()
    public ResponseDTO<List<Account>> getAllAccounts() {
        List<Account> getListUsers = userManagementService.getAllUsers();
        return  new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, getListUsers);
    }

    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @DeleteMapping("/{id}")
    public ResponseDTO<Object> deleteUser(@PathVariable(name = "id") Long id) {
         this.userManagementService.deleteUser(id);
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "Update a account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATE_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PutMapping("/{id}")
    public ResponseDTO<String> updateAccount(
            @Valid @RequestBody AccountUpdateRequest accountUpdateRequest,
            @PathVariable(name = "id") Long id) {
        this.userManagementService.editUser(id, accountUpdateRequest.getNewPassword().trim(), Long.parseLong(accountUpdateRequest.getRoleId()));
        return new ResponseDTO<>(HttpStatus.OK, UPDATE_SUCCESSFULLY, null);
    }

    @ApiOperation(value = "Get all active account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping("/active-user")
    public ResponseDTO<Map<String, Object>> getAllActiveAccounts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1") int size) {
        Map<String, Object> users = userManagementService.getAllActiveUser(page, size);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, users);
    }

    /**
     * This endpoint is used to register a username and role
     *
     */
    @ApiOperation(value = "Register account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REQUEST_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @PostMapping("/register")
    public ResponseDTO<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String result = this.registerUserService.registerUser(registerRequest.getUsername().trim(),registerRequest.getPassword().trim(), registerRequest.getRoleId());
        return new ResponseDTO<>(HttpStatus.OK, REQUEST_SUCCESSFULLY, result);
    }

}
