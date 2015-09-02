package org.fuc.commands.account;

import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.entities.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("deleteAccountCommand")
public class DeleteAccountCommand implements Command {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Criteria criteria) {
        AccountCriteria accountCriteria = (AccountCriteria) criteria;
        Account account = accountCriteria.getAccount();
        entityManager.remove(entityManager.merge(account));
    }
}
