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
        assert entityManager != null;
        entityManager
                .createNativeQuery("delete from account_ride where ride_id = ?1")
                .setParameter(1, ride.getId())
                .executeUpdate();

        entityManager.remove(entityManager.contains(ride) ? ride : entityManager.merge(ride));
    }

    public Collection<Ride> getRidesForOwner(Account account) {
        return entityManager
                .createNamedQuery(Ride.FIND_BY_OWNER, Ride.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }

    public Collection<Ride> getAvailableRides(Account account){
        return entityManager
                .createNamedQuery(Ride.AVAILABLE_RIDES, Ride.class)
                .setParameter("beatnik_id", account.getId())
                .setParameter("date", new Date())
                .getResultList();
    }

    public Ride findById(Long id) {
        return entityManager.find(Ride.class, id);
    }
}
