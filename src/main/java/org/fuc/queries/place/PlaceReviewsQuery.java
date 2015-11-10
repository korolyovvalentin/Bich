package org.fuc.queries.place;

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

@Repository("placeReviewsQuery")
public class PlaceReviewsQuery implements Query<PlaceRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<PlaceRequest> query(Criteria criteria) {
        PlaceCriteria placeCriteria = (PlaceCriteria) criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> builderCriteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = builderCriteria.from(PlaceRequest.class);
        builderCriteria.select(root);

        builderCriteria.where(builder.equal(root.get("place"), placeCriteria.getPlace()));
        builderCriteria.where(builder.or(
                builder.equal(root.get("status"), RequestStatus.OLD),
                builder.equal(root.get("status"), RequestStatus.OLD)
        ));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
