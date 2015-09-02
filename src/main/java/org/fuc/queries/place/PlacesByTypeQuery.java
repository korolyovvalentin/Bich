package org.fuc.queries.place;


import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.PlaceTypeCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository("placesByTypeQuery")
public class PlacesByTypeQuery implements Query<Place> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Place> query(Criteria criteria) {
        PlaceTypeCriteria cityCriteria = (PlaceTypeCriteria) criteria;
        City city = cityCriteria.getCity();
        String type = cityCriteria.getType();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Place> builderCriteria = builder.createQuery(Place.class);
        Root<Place> root = builderCriteria.from(Place.class);
        builderCriteria.select(root);

        if(city != null)
            builderCriteria.where(builder.equal(root.get("city"), city));
        if(type != null)
            builderCriteria.where(builder.equal(root.get("type"), type));

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
