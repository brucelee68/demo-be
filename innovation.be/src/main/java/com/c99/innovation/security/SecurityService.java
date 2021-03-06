package com.c99.innovation.security;

import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.entity.Account;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.repository.InnovationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private AccountRepository accountRepository;

    private InnovationRepository innovationRepository;

    @Autowired
    public SecurityService(AccountRepository accountRepository,
                           InnovationRepository innovationRepository) {
        this.accountRepository = accountRepository;
        this.innovationRepository = innovationRepository;
    }

    public boolean isAccessibleToInnovationResource(Long innovationId) {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return false;
            }
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String role = userDetails.getAuthorities().stream().findFirst().orElseThrow(NullPointerException::new).getAuthority();
            if (RoleType.ADMIN.toString().equals(role)) {
                return true;
            }
            Account account = accountRepository
                    .findByUsernameIgnoreCase(userDetails.getUsername())
                    .orElseThrow(RecordNotFoundException::new);
            Account author = innovationRepository
                    .findActiveInnovationById(innovationId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getCreatedBy();
            return (account != null) && (author != null) && (account.getId() == author.getId());
        } catch (Exception ex) {
            return false;
        }
    }
}
