package org.fuc.repositories;

import org.fuc.entities.City;
import org.fuc.entities.Place;
import org.fuc.entities.PlaceRequest;
import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class PlaceRequestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(PlaceRequest request) {
        entityManager.persist(request);
    }

    @Transactional
    public void update(PlaceRequest ride) {
        entityManager.merge(ride);
    }

    @Transactional
    public void delete(PlaceRequest request) {
        entityManager.remove(request);
    }

    public Collection<PlaceRequest> findRequests(City city, String type) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> criteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = criteria.from(PlaceRequest.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("type"), type));

        if(city != null)
            criteria.where(builder.equal(root.get("city_id"), city.getId()));

        return entityManager.createQuery(criteria).getResultList();
    }

    public Collection<PlaceRequest> findRequests(Long rideId, String status) {
        return entityManager
                .createNamedQuery(Request.FIND_BY_RIDE, PlaceRequest.class)
                .setParameter("ride_id", rideId)
                .setParameter("status", status)
                .getResultList();
    }

    public Collection<Request> findUpdatedRequests(Long beatnikId) {
        return entityManager
                .createNamedQuery(Request.FIND_UPDATED, Request.class)
                .setParameter("beatnik_id", beatnikId)
                .getResultList();
    }

    public PlaceRequest findById(Long id) {
        return entityManager.find(PlaceRequest.class, id);
    }
}
