package com.c99.innovation.service;

import com.c99.innovation.entity.Account;

import java.util.List;
import java.util.Map;

public interface UserManagementService {
    List<Account> getAllUsers();

    boolean deleteUser(Long id);

    String editUser(Long id, String newPassword, Long roleId);

    Map<String, Object> getAllActiveUser(int page, int size);
}
