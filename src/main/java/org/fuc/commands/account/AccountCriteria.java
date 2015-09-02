package org.fuc.commands.account;

import org.fuc.core.Criteria;
import org.fuc.entities.Account;

public class AccountCriteria extends Criteria{
    private Account account;

    public AccountCriteria(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
