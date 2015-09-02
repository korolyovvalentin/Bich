package org.fuc.queries.ride;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.entities.Account;
import org.fuc.entities.Ride;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;

@Repository("ridesByBeatnikQuery")
public class RidesByBeatnikQuery implements Query<Ride> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Ride> query(Criteria criteria) {
        AccountCriteria accountCriteria = (AccountCriteria) criteria;
        Account account = accountCriteria.getAccount();

        return entityManager
                .createNamedQuery(Ride.AVAILABLE_RIDES, Ride.class)
                .setParameter("beatnik_id", account.getId())
                .setParameter("date", new Date())
                .getResultList();
    }
}
