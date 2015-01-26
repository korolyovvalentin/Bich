package org.fuc.services;

import org.fuc.entities.RoleProvider;
import org.fuc.entities.Account;
import org.fuc.entities.Ride;
import org.fuc.repositories.RidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
public class RideService {
    @Autowired
    private RidesRepository ridesRepository;

    public Ride createRide(Ride ride) {
        if (!ride.getOwner().getRole().equals(RoleProvider.ROLE_DRIVER)) {
            throw new IllegalArgumentException("Ride's owner must be driver");
        }
        ridesRepository.save(ride);
        return ride;
    }

    @Transactional
    public void addParticipant(Ride ride, Account beatnik) {
        if (!ride.hasVacantSeat()) {
            throw new ArrayIndexOutOfBoundsException("No vacant seats");
        }
        if (!RoleProvider.ROLE_BEATNIK.equals(beatnik.getRole())) {
            throw new IllegalArgumentException("Participant must be beatnik");
        }
        Ride persistentRide = ridesRepository.findById(ride.getId());
        persistentRide.getParticipants().add(beatnik);
        ridesRepository.update(persistentRide);
    }

    public Collection<Ride> getDriverRides(Account driver){
        return ridesRepository.getRidesForOwner(driver);
    }

    public Collection<Ride> getAvailableRides(Account account){
        return ridesRepository.getAvailableRides(account);
    }
}
