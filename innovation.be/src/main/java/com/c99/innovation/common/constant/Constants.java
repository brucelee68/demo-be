package com.c99.innovation.common.constant;

import java.util.List;

public class Constants {

    public static final String FE_URL = "http:localhost:3000";
    public static final String API_V1 = "/api/v1";
    public static final List<String> ADMINISTRATOR_PREREQUISITES_URL_LIST = List.of(
            "/innovation/approve/{id}",
            "/innovation/reject/{id}"
    );
    public static final List<String> URL_WHITELIST = List.of(
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
            "/h2-console/**",
            "/static/**",
            "/manifest.json",
            "/favicon.ico",
            "/idea128.png",
            "/idea512.png",
            API_V1 + "/login",
            API_V1 + "/register",
            API_V1 + "/refresh-token",
            "/login",
            "/checkHealth",
            "/"
    );

    public static final List<String> WHITELIST_URL_REGEX = List.of(
            "(\\/static\\/).*"
    );

    public static final String ADMIN_AUTHORITY_VALUE = "ADMIN";
    public static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final List<String> IMAGE_EXTENSIONS = List.of(".png", ".jpeg", ".jpg", ".svg", ".gif");
    public static final String OBJECT_NULL_CANT_CONVERT_TO_JSON = "Object is null and can not be used to convert to JSON";
    public static final String UNAUTHORIZED_REASON = "Unauthorized error: {}";
    public static final String ERROR_MESSAGE_ATTRIBUTE_HTTP_REQUEST = "errorMessage";
    public static final String AUTHORIZATION_BEARER = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_MISSING = "JWT Access Token is missing";
    public static final String CAN_NOT_SET_AUTHENTICATION_VALUE = "Cannot set user authentication: {}";
    public static final String RETURN_NEW_ACCESS_TOKEN = "Return the new access token";
    public static final String REFRESH_TOKEN_NOT_STORE = "Refresh token is not stored in database!";
    public static final String LOGIN_SUCCESSFULLY = "Login Successfully";
    public static final String REGISTER_SUCCESSFULLY = "Register Successfully";
    public static final String USERNAME_NOT_FOUND = "Username is not found";
    public static final String BAD_CREDENTIALS = "Bad credential has been provided";
    public static final String SERVER_ERROR_MESSAGE = "Something went wrong! Try again";
    public static final String FAILED_FOR_TOKEN_MESSAGE = "Failed for [%s]: %s";
    public static final String UPLOAD_ATTACHMENT_SUCCESSFULLY = "Upload attachments successfully";
    public static final String BAD_REQUEST_COMMON_MESSAGE = "Invalid field values have been provided";
    public static final String CREATE_SUCCESSFULLY = "Created successfully";
    public static final String DELETE_SUCCESSFULLY = "Delete Successfully";
    public static final String UPDATE_SUCCESSFULLY = "Update successfully";
    public static final String EMPTY_FILE_UPLOAD_MESSAGE = "The file which has been uploaded is empty";
    public static final String ERROR_WHEN_UPLOAD_TO_S3 = " encounter some error when upload to S3!";
    public static final String RECORD_NOT_FOUND = "This received image has a invalid extension type";
    public static final String NOT_VALID_IMAGE_EXTENSION = "This received image has a invalid extension type";
    public static final String FETCHING_SUCCESSFULLY = "Fetching successfully";
    public static final String REQUEST_SUCCESSFULLY = "Request successfully!";
    public static final String USERNAME_EXISTED = "Username existed!";
    public static final String TEAM_EXISTED = "Team existed!";
    public static final String CREATE_FAILED = "Create failed";
    public static final String DELETE_FAILED = "Delete failed";
    public static final String UPDATE_FAILED = "Update failed";
    public static final String AREA_EXISTED = "Area existed!";

    private Constants() {

    }
}
