package org.fuc.queries.request;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.entities.PlaceRequest;
import org.fuc.entities.Request;
import org.fuc.entities.RequestStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository("requestsUpdatedQuery")
public class RequestsUpdatedQuery implements Query<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Request> query(Criteria criteria) {
        AccountCriteria accountCriteria = (AccountCriteria)criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> builderCriteria = builder.createQuery(Request.class);
        Root<Request> root = builderCriteria.from(Request.class);
        builderCriteria.select(root);

        builderCriteria.where(builder.and(
                builder.equal(root.get("owner"), accountCriteria.getAccount()),
                builder.or(
                        builder.equal(root.get("status"), RequestStatus.APPROVED),
                        builder.equal(root.get("status"), RequestStatus.DECLINED)
                )
        ));

//        builderCriteria.where(builder.equal(root.get("owner"), accountCriteria.getAccount()));
//        builderCriteria.where(builder.or(
//                builder.equal(root.get("status"), RequestStatus.APPROVED),
//                builder.equal(root.get("status"), RequestStatus.DECLINED)
//        ));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
