package com.c99.innovation.controller;

import com.c99.innovation.common.JwtUtils;
import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.request.LoginRequest;
import com.c99.innovation.dto.request.RegisterRequest;
import com.c99.innovation.dto.request.TokenRefreshRequest;
import com.c99.innovation.dto.response.AuthenticationResponse;
import com.c99.innovation.dto.response.TokenRefreshResponse;
import com.c99.innovation.exception.TokenRefreshException;
import com.c99.innovation.service.RefreshTokenService;
import com.c99.innovation.service.RegisterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.c99.innovation.common.constant.Constants.*;

@Api(value = "Authentication Controller")
@RestController
@RequestMapping(value = API_V1)
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtTokenUtils;

    private RefreshTokenService refreshTokenService;

    private RegisterUserService registerUserService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtUtils jwtTokenUtil,
                                    RefreshTokenService refreshTokenService,
                                    RegisterUserService registerUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtil;
        this.refreshTokenService = refreshTokenService;
        this.registerUserService = registerUserService;
    }

    /**
     * This endpoint is used to provide a username/password mechanism authentication
     *
     * @param loginRequest
     * @return ResponseDTO<LoginResponseDTO>
     */
    @ApiOperation(value = "Login to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = LOGIN_SUCCESSFULLY),
            @ApiResponse(code = 401, message = BAD_CREDENTIALS)})
    @PostMapping("/login")
    public ResponseDTO<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetail = (UserDetails) authenticate.getPrincipal();
        String accessToken = jwtTokenUtils.generateAccessToken(userDetail.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(userDetail.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(userDetail, accessToken, refreshToken);
        return new ResponseDTO<>(HttpStatus.OK, LOGIN_SUCCESSFULLY, authenticationResponse);
    }

    /**
     * This endpoint is used to provide a way to get a new access token by proving a refresh token
     *
     * @param tokenRefreshRequest
     * @return ResponseDTO<TokenRefreshResponseDTO>
     */
    @ApiOperation(value = "Request a new access token by providing a refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = RETURN_NEW_ACCESS_TOKEN),
            @ApiResponse(code = 401, message = REFRESH_TOKEN_NOT_STORE)})
    @PostMapping("/refresh-token")
    public ResponseDTO<TokenRefreshResponse> generateNewAccessTokenByRefreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
        return refreshTokenService
                .findAccountByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyTokenExpiration)
                .map(account -> {
                    String newAccessToken = jwtTokenUtils.generateAccessToken(account.getUsername());
                    return new ResponseDTO<>(
                            HttpStatus.OK,
                            RETURN_NEW_ACCESS_TOKEN,
                            new TokenRefreshResponse(newAccessToken, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, REFRESH_TOKEN_NOT_STORE));
    }


}
