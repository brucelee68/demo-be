package com.c99.innovation.service;

import com.c99.innovation.dto.request.RegisterRequest;

public interface RegisterUserService {

    String registerUser( String username, String password, long roleId);

}
