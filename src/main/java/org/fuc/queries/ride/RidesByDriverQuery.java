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

@Repository("ridesByDriverQuery")
public class RidesByDriverQuery implements Query<Ride> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Ride> query(Criteria criteria) {
        Account account = ((AccountCriteria) criteria).getAccount();
        return entityManager
                .createNamedQuery(Ride.FIND_BY_OWNER, Ride.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }
}
