package org.fuc.queries.place;

import org.fuc.controllers.CityController;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.core.criterias.CityCriteria;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository("placesByOwnerQuery")
public class PlacesByCityQuery implements Query<Place> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Place> query(Criteria criteria) {
        City city = ((CityCriteria) criteria).getCity();

        return entityManager
                .createNamedQuery(Place.FIND_BY_CITY, Place.class)
                .setParameter("city_id", city.getId())
                .getResultList();
    }
}

