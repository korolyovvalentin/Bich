package org.fuc.repositories;

import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
