package com.c99.innovation.service.impl;

import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.Account;
import com.c99.innovation.entity.Role;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.repository.RoleRepository;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.c99.innovation.common.constant.Constants.*;

@Service
public class RegisterUserServiceImp implements RegisterUserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(String username, String password, long roleId) {

        List<Account> listAccount = accountRepository.findAll();
        for (int i = 0; i < listAccount.size(); i++) {
            if (username.equalsIgnoreCase(listAccount.get(i).getUsername())) {
                return USERNAME_EXISTED;
            }
        }
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if (a.getRole().getName().equals(RoleType.ADMIN.toString())) {
            Account acc = new Account();
         Role r = roleRepository.findById(roleId).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any role"));
         String p = passwordEncoder.encode(password);
            acc.setUsername(username);
            acc.setPassword(p);
            acc.setRole(r);
            acc.setStatus(StatusType.ACTIVE);
            accountRepository.save(acc);
            return CREATE_SUCCESSFULLY;
        }
        return CREATE_FAILED;
    }

}
