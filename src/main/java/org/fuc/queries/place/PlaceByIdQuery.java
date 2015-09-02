package org.fuc.queries.place;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("placeByIdQuery")
public class PlaceByIdQuery implements QuerySingle<Place> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Place query(Criteria criteria) {
        IdCriteria idCriteria = (IdCriteria) criteria;

        return entityManager.find(Place.class, idCriteria.getId());
    }
}
