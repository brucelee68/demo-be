package com.c99.innovation.service;

import com.c99.innovation.entity.Account;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<Account> findAccountByRefreshToken(String refreshToken);

    String createRefreshToken(String username);

    Account verifyTokenExpiration(Account account);
}
