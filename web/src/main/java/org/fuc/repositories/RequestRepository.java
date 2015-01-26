package org.fuc.repositories;

import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class RequestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Request request) {
        entityManager.persist(request);
    }

    @Transactional
    public void update(Request ride) {
        entityManager.merge(ride);
    }

    @Transactional
    public void delete(Request request) {
        entityManager.remove(request);
    }

    public Collection<Request> findRequests(Long rideId, String status){
        return entityManager
                .createNamedQuery(Request.FIND_BY_RIDE, Request.class)
                .setParameter("ride_id", rideId)
                .setParameter("status", status)
                .getResultList();
    }

    public Request findById(Long id){
        return entityManager.find(Request.class, id);
    }
}
