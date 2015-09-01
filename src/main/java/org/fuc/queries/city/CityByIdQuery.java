package org.fuc.queries.city;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.entities.City;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component("cityByIdQuery")
public class CityByIdQuery implements QuerySingle<City> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public City query(Criteria criteria) {
        CityIdCriteria idCriteria = (CityIdCriteria) criteria;

        return entityManager
                .createNamedQuery(City.FIND_BY_ID, City.class)
                .setParameter("id", idCriteria.getId())
                .getSingleResult();
    }
}
