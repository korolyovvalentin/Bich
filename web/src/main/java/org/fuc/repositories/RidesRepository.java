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
        entityManager.persist(entityManager.merge(ride));
        return ride;
    }

    @Transactional
    public void delete(Ride ride) {
        entityManager.remove(entityManager.merge(ride));
    }

    public Collection<Ride> getRidesForOwner(Account account) {
        return entityManager
                .createNamedQuery(Ride.FIND_BY_OWNER, Ride.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }

    public Ride findById(Long id) {
        return entityManager
                .createNamedQuery(Ride.FIND_BY_ID, Ride.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
