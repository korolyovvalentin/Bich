package org.fuc.commands.ride;

import org.fuc.core.Command;
import org.fuc.core.criterias.RideParticipantCriteria;
import org.fuc.entities.Account;
import org.fuc.entities.Ride;
import org.fuc.entities.RoleProvider;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("addRideParticipantCommand")
@Transactional
public class AddRideParticipantCommand implements Command<RideParticipantCriteria> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(RideParticipantCriteria context) {
        Ride ride = context.getRide();
        Account beatnik = context.getAccount();

        if (!ride.hasVacantSeat()) {
            throw new ArrayIndexOutOfBoundsException("No vacant seats");
        }
        if (!RoleProvider.ROLE_BEATNIK.equals(beatnik.getRole())) {
            throw new IllegalArgumentException("Participant must be beatnik");
        }
        ride.getParticipants().add(beatnik);
        entityManager.merge(ride);
    }
}
