package org.fuc.repositories;

import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Date;

@Repository
@Transactional(readOnly = true)
public class RidePointRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public RidePoint save(RidePoint ride) {
        entityManager.persist(ride);
        return ride;
    }

    public void update(RidePoint ride) {
        entityManager.merge(ride);
    }

    @Transactional
    public void delete(RidePoint ride) {
        assert entityManager != null;

        RidePoint refreshed = entityManager.merge(ride);
        entityManager.refresh(refreshed);
        entityManager.remove(refreshed);
    }

    public Ride findById(Long id) {
        return entityManager.find(Ride.class, id);
    }
}
