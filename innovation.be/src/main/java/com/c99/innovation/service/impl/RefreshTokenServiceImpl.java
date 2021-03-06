package com.c99.innovation.service.impl;

import com.c99.innovation.common.constant.Constants;
import com.c99.innovation.entity.Account;
import com.c99.innovation.exception.TokenRefreshException;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${innovation.jwt.refresh_expiration}")
    private Long refreshTokenDurationMs;

    private AccountRepository accountRepository;

    public RefreshTokenServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * This method is used to find an account record in database based on the token value
     *
     * @param token
     * @return
     */
    @Override
    public Optional<Account> findAccountByRefreshToken(String token) {
        return accountRepository.findByRefreshToken(token);
    }

    /**
     * This method is used to create a new refresh token
     *
     * @param username
     * @return
     */
    @Override
    public String createRefreshToken(String username) {
        Account account = accountRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USERNAME_NOT_FOUND));
        account.setTokenExpiredDate(Instant.now().plusMillis(refreshTokenDurationMs));
        account.setRefreshToken(UUID.randomUUID().toString());
        this.accountRepository.flush();
        return account.getRefreshToken();
    }

    /**
     * This method is used to verify a refresh token is expired or not
     *
     * @param account
     * @return
     */
    @Override
    public Account verifyTokenExpiration(Account account) {
        if (account.getTokenExpiredDate().compareTo(Instant.now()) < 0) {
            Account account1 = accountRepository.findById(account.getId()).orElseThrow();
            account1.setRefreshToken(null);
            account1.setTokenExpiredDate(null);
            accountRepository.save(account1);
            throw new TokenRefreshException(account.getRefreshToken(), "Refresh token was expired. Please make a new sign in request");
        }
        return account;
    }
}