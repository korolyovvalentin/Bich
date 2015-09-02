package org.fuc.queries.ride;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.Ride;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("rideByIdQuery")
public class RideByIdQuery implements QuerySingle<Ride> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Ride query(Criteria criteria) {
        IdCriteria idCriteria = (IdCriteria) criteria;
        return entityManager.find(Ride.class, idCriteria.getId());
    }
}
