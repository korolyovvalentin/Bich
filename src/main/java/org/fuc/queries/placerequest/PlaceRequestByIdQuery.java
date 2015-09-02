package org.fuc.queries.placerequest;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.PlaceRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("placeRequestByIdQuery")
public class PlaceRequestByIdQuery implements QuerySingle<PlaceRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PlaceRequest query(Criteria criteria) {
        return entityManager.find(PlaceRequest.class, ((IdCriteria)criteria).getId());
    }
}
