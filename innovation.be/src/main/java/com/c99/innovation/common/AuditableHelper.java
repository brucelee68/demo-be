package com.c99.innovation.common;

import com.c99.innovation.dto.AuditableResponse;
import com.c99.innovation.dto.response.AccountResponse;
import com.c99.innovation.entity.Account;
import com.c99.innovation.entity.AuditableEntity;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.mapper.AccountMapper;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.security.UserDetailImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditableHelper {

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    public AuditableHelper(AccountRepository accountRepository,
                           AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AuditableEntity updateAuditingCreatedFields(AuditableEntity auditable) {

        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsernameIgnoreCase(principal.getUsername())
                .orElseThrow(RecordNotFoundException::new);
        auditable.setCreatedAt(LocalDateTime.now());
        auditable.setCreatedBy(account);
        return auditable;
    }

    public AuditableEntity updateAuditingModifiedFields(AuditableEntity auditable) {

        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsernameIgnoreCase(principal.getUsername())
                .orElseThrow(RecordNotFoundException::new);
        auditable.setLastModifiedAt(LocalDateTime.now());
        auditable.setLastModifiedBy(account);
        return auditable;
    }

    public AuditableResponse setupDisplayAuditableRelatedToPersonFields(AuditableEntity auditableEntity, AuditableResponse auditableResponse) {
        AccountResponse createdBy = this.accountMapper.convertToDto(auditableEntity.getCreatedBy());
        auditableResponse.setCreatedBy(createdBy);
        if (auditableEntity.getLastModifiedBy() != null) {
            AccountResponse updatedBy = this.accountMapper.convertToDto(auditableEntity.getLastModifiedBy());
            auditableResponse.setLastModifiedBy(updatedBy);
        }
        return auditableResponse;
    }

    public AuditableResponse setupDisplayAuditableRelatedToTimestampFields(AuditableEntity auditableEntity, AuditableResponse auditableResponse) {
        auditableResponse.setCreatedAt(auditableEntity.getCreatedAt());
        if (auditableEntity.getLastModifiedAt() != null) {
            auditableEntity.setLastModifiedAt(auditableEntity.getLastModifiedAt());
        }
        return auditableResponse;
    }
}
