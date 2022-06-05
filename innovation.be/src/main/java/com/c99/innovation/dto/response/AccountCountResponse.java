package com.c99.innovation.dto.response;

import com.c99.innovation.entity.Account;

public class AccountCountResponse {
    private Account account;
    private long count;

    public AccountCountResponse(Account account, long count) {
        this.account = account;
        this.count = count;
    }

    public AccountCountResponse() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
