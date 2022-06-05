package com.c99.innovation.service.impl;

import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.*;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.*;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.c99.innovation.common.constant.Constants.*;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClapRepository clapRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InnovationRepository innovationRepository;



    @Override
    public List<Account> getAllUsers() {
        List<Account> userManagement = accountRepository.findAll();
        return userManagement;
    }

    @Override
    public boolean deleteUser(Long id) {
        Account user = accountRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if (a.getRole().getName().equals(RoleType.ADMIN.toString())) {
            user.setStatus(StatusType.DELETED);
            accountRepository.save(user);

            List<Innovation> listInnovation = innovationRepository.findAll();
            for (Innovation i : listInnovation) {
                if(i.getCreatedBy().getId() == id){
                    i.setStatus(Status.DELETED);
                }
            }

            List<Clap> listClap = clapRepository.findAll();
            for (Clap c : listClap) {
                if(c.getAccount().getId() == id){
                    c.setStatus(StatusType.DELETED);
                    clapRepository.save(c);
                }
            }

            List<CommentInnovation> listComment = commentRepository.findAll();
            for (CommentInnovation ci : listComment) {
                if(ci.getAccount().getId() == id){
                    ci.setStatus(StatusType.DELETED);
                    commentRepository.save(ci);
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public String editUser(Long id, String newPassword, Long roleId) {
        Account user = accountRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if (a.getRole().getName().equals(RoleType.ADMIN.toString())) {
            if (!newPassword.isBlank()) {
                user.setPassword(passwordEncoder.encode(newPassword.trim()));
            }
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any role"));
            user.setRole(role);
            accountRepository.save(user);
            return UPDATE_SUCCESSFULLY;
        }
        return UPDATE_FAILED;
    }

    @Override
    public Map<String, Object> getAllActiveUser(int page, int size) {
        Page<Account> userManagement = accountRepository.findAllByStatus(StatusType.ACTIVE ,PageRequest.of(page, size));
        List<Account> accountList = userManagement.getContent();
        int start = size * (page+1) - size + 1;
        Map<String, Object> response = new HashMap<>();

        response.put("accounts", accountList);
        response.put("currentPage", userManagement.getNumber());
        response.put("size", userManagement.getSize());
        response.put("totalElements", userManagement.getTotalElements());
        response.put("totalPages", userManagement.getTotalPages());
        response.put("startPage", start);
        return response;
    }


}
