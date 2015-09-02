package org.fuc.commands.account;

import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.entities.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createAccountCommand")
public class CreateAccountCommand implements Command {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public void execute(Criteria criteria) {
        AccountCriteria accountCriteria = (AccountCriteria) criteria;
        Account account = accountCriteria.getAccount();

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.persist(account);
    }
}
