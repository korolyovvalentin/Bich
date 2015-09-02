package org.fuc.queries.request;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.PlaceCriteria;
import org.fuc.core.criterias.RideCriteria;
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

@Repository("requestsNewQuery")
public class RequestsNewQuery implements Query<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Request> query(Criteria criteria) {
        RideCriteria placeCriteria = (RideCriteria)criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> builderCriteria = builder.createQuery(Request.class);
        Root<Request> root = builderCriteria.from(Request.class);
        builderCriteria.select(root);
        builderCriteria.where(builder.equal(root.get("ride"), placeCriteria.getRide()));
        builderCriteria.where(builder.equal(root.get("status"), RequestStatus.NEW));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
