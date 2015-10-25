package org.fuc.queries.ride;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.RideFilterCriteria;
import org.fuc.entities.Ride;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;

@Repository("ridesByFilterQuery")
public class RidesByFilterQuery implements Query<Ride> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Ride> query(Criteria criteria) {
        RideFilterCriteria filterCriteria = (RideFilterCriteria) criteria;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ride> builderCriteria = builder.createQuery(Ride.class);
        Root<Ride> root = builderCriteria.from(Ride.class);
        builderCriteria.select(root);

        if(filterCriteria.getDate() != null){
            builderCriteria.where(
                    builder.equal(
                            root.get("date"),
                            filterCriteria.getDate()));
        } else {
            builderCriteria.where(
                    builder.greaterThanOrEqualTo(
                            root.<Date>get("date"),
                            new Date()));
        }

        return entityManager.createQuery(builderCriteria).getResultList();
    }
}
