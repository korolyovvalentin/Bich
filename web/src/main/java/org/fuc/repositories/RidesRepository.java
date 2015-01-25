package org.fuc.repositories;

import org.fuc.entities.Account;
import org.fuc.entities.Ride;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class RidesRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Ride save(Ride ride) {
        entityManager.persist(ride);
        return ride;
    }

    public void update(Ride ride) {
        entityManager.merge(ride);
    }

    @Transactional
    public void delete(Ride ride) {
        entityManager
                .createNativeQuery("delete from account_ride where ride_id = ?1")
                .setParameter(1, ride.getId());
        Ride refreshed = entityManager.merge(ride);
        entityManager.refresh(refreshed);
        entityManager.remove(refreshed);
    }

    public Collection<Ride> getRidesForOwner(Account account) {
        return entityManager
                .createNamedQuery(Ride.FIND_BY_OWNER, Ride.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }

    public Ride findById(Long id) {
        return entityManager.find(Ride.class, id);
    }
}
