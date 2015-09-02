package org.fuc.queries.place;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.entities.Account;
import org.fuc.entities.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository("placesByOwnerQuery")
public class PlacesByOwnerQuery implements Query<Place> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Place> query(Criteria criteria) {
        Account account = ((AccountCriteria) criteria).getAccount();

        return entityManager
                .createNamedQuery(Place.FIND_BY_OWNER, Place.class)
                .setParameter("owner_id", account.getId())
                .getResultList();
    }
}
