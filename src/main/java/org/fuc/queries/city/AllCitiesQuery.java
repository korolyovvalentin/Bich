package org.fuc.queries.city;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.entities.City;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository("allCitiesQuery")
public class AllCitiesQuery implements Query<City> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<City> query(Criteria criteria) {
        return entityManager.createNamedQuery(City.ALL_CITIES, City.class).getResultList();
    }
}
