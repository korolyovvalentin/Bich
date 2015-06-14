package org.fuc.repositories;

import org.fuc.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

        if (city != null)
            criteria.where(builder.equal(root.get("city"), city));

        return entityManager.createQuery(criteria).getResultList();
    }

    public Collection<PlaceRequest> findNewRequests(Place place) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> criteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = criteria.from(PlaceRequest.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("place"), place));
        criteria.where(builder.equal(root.get("status"), RequestStatus.NEW));

        return entityManager.createQuery(criteria).getResultList();
    }

    public Collection<PlaceRequest> findUpdatedRequests(Account owner) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlaceRequest> criteria = builder.createQuery(PlaceRequest.class);
        Root<PlaceRequest> root = criteria.from(PlaceRequest.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("owner"), owner));
        criteria.where(builder.or(
                builder.equal(root.get("status"), RequestStatus.APPROVED),
                builder.equal(root.get("status"), RequestStatus.DECLINED)
        ));

        return entityManager.createQuery(criteria).getResultList();
    }

    public PlaceRequest findById(Long id) {
        return entityManager.find(PlaceRequest.class, id);
    }
}
