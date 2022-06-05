package com.c99.innovation.mapper;

import com.c99.innovation.dto.response.AccountResponse;
import com.c99.innovation.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private ModelMapper mapper;

    public AccountMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AccountResponse convertToDto(Account account) {
        return this.mapper.map(account, AccountResponse.class);
    }
}
