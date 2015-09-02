package org.fuc.queries.request;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("requestByIdQuery")
public class RequestByIdQuery implements QuerySingle<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Request query(Criteria criteria) {
        return entityManager.find(Request.class, ((IdCriteria)criteria).getId());
    }
}
