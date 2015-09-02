package org.fuc.commands.ride;

import org.fuc.core.Command;
import org.fuc.core.criterias.RidePointsCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createRideCommand")
public class CreateRideCommand implements Command<RidePointsCriteria> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(RidePointsCriteria context) {
        City[] cities = context.getCities();
        Ride ride = context.getRide();

        int order = 0;
        for (City city : cities) {
            RidePoint ridePoint = new RidePoint(ride, order++, city);
            ride.getPoints().add(ridePoint);
        }
        entityManager.persist(ride);
    }
}
