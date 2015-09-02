package org.fuc.commands.ride;

import org.fuc.core.Command;
import org.fuc.entities.Ride;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("deleteRideCommand")
@Transactional
public class DeleteRideCommand implements Command<Ride> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Ride ride) {
        System.out.println("Ride: " + ride);
        System.out.println("RideId: " + ride.getId());
        entityManager
                .createNativeQuery("delete from account_ride where ride_id = ?1")
                .setParameter(1, ride.getId())
                .executeUpdate();

        entityManager.remove(entityManager.contains(ride) ? ride : entityManager.merge(ride));
    }
}
