package org.fuc.services;

import org.fuc.entities.Account;
import org.fuc.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional()
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
