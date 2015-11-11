package org.fuc.queries.request;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.RideCriteria;
import org.fuc.entities.Request;
import org.fuc.entities.RequestStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository("requestsNewQuery")
public class RequestsNewQuery implements Query<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Request> query(Criteria criteria) {
        RideCriteria rideCriteria = (RideCriteria) criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> builderCriteria = builder.createQuery(Request.class);
        Root<Request> root = builderCriteria.from(Request.class);
        builderCriteria.select(root);

        builderCriteria.where(builder.and(
                builder.equal(root.get("owner"), rideCriteria.getAccount()),
                builder.and(
                        builder.equal(root.get("ride"), rideCriteria.getRide()),
                        builder.equal(root.get("status"), RequestStatus.NEW)
                )
        ));

//        builderCriteria.where(builder.equal(root.get("owner"), rideCriteria.getAccount()));
//        builderCriteria.where(builder.equal(root.get("ride"), rideCriteria.getRide()));
//        builderCriteria.where(builder.or(
//                builder.equal(root.get("status"), RequestStatus.NEW),
//                builder.equal(root.get("status"), RequestStatus.NEW)
//        ));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
