package org.fuc.commands.account;

import org.fuc.core.Command;
import org.fuc.entities.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("deleteAccountCommand")
public class DeleteAccountCommand implements Command<Account> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Account account) {
        entityManager.remove(entityManager.merge(account));
    }
}
