package org.fuc.queries.account;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.EmailCriteria;
import org.fuc.entities.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository("accountByEmailQuery")
public class AccountByEmailQuery implements QuerySingle<Account> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account query(Criteria criteria) {
        EmailCriteria emailCriteria = (EmailCriteria) criteria;

        try {
            return entityManager.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
                    .setParameter("email", emailCriteria.getEmail())
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }
}
