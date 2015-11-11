package org.fuc.queries.placerequest;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.PlaceCriteria;
import org.fuc.entities.PlaceRequest;
import org.fuc.entities.RequestStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository("placeRequestsNewQuery")
public class PlaceRequestsNewQuery implements Query<PlaceRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<PlaceRequest> query(Criteria criteria) {
        PlaceCriteria placeCriteria = (PlaceCriteria)criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> builderCriteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = builderCriteria.from(PlaceRequest.class);
        builderCriteria.select(root);

        builderCriteria.where(builder.and(
                builder.equal(root.get("place"), placeCriteria.getPlace()),
                builder.equal(root.get("status"), RequestStatus.NEW)
        ));
//        builderCriteria.where(builder.equal(root.get("place"), placeCriteria.getPlace()));
//        builderCriteria.where(builder.equal(root.get("status"), RequestStatus.NEW));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
