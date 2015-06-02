package org.fuc.repositories;

import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Place;
import org.hibernate.ejb.criteria.predicate.BetweenPredicate;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Collection;
import java.util.Date;

@Repository
@Transactional(readOnly = true)
public class PlaceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Place save(Place place) {
        entityManager.persist(place);
        return place;
    }

    public void update(Place place) {
        entityManager.merge(place);
    }

    @Transactional
    public void delete(Place place) {
//        entityManager
//                .createNativeQuery("delete from account_ride where ride_id = ?1")
//                .setParameter(1, place.getId())
//                .executeUpdate();

        Place refreshed = entityManager.merge(place);
        entityManager.refresh(refreshed);
        entityManager.remove(refreshed);
    }

    public Collection<Place> getOwnerPlaces(Account account) {
        return entityManager
                .createNamedQuery(Place.FIND_BY_OWNER, Place.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }

    public Collection<Place> getCityPlaces(City city) {
        return entityManager
                .createNamedQuery(Place.FIND_BY_CITY, Place.class)
                .setParameter("city_id", city.getId())
                .getResultList();
    }


    public Place findById(Long id) {
        return entityManager.find(Place.class, id);
    }
}
