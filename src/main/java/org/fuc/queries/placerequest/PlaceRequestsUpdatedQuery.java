package org.fuc.queries.placerequest;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.entities.PlaceRequest;
import org.fuc.entities.RequestStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository("placeRequestsUpdatedQuery")
public class PlaceRequestsUpdatedQuery implements Query<PlaceRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<PlaceRequest> query(Criteria criteria) {
        AccountCriteria placeCriteria = (AccountCriteria)criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> builderCriteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = builderCriteria.from(PlaceRequest.class);
        builderCriteria.select(root);
        builderCriteria.where(builder.equal(root.get("owner"), placeCriteria.getAccount()));
        builderCriteria.where(builder.or(
                builder.equal(root.get("status"), RequestStatus.APPROVED),
                builder.equal(root.get("status"), RequestStatus.DECLINED)
        ));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
