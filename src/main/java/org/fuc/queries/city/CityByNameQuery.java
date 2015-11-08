package org.fuc.queries.city;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.NameCriteria;
import org.fuc.entities.City;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("cityByNameQuery")
public class CityByNameQuery implements QuerySingle<City> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public City query(Criteria criteria) {
        NameCriteria idCriteria = (NameCriteria) criteria;

        List<City> result =  entityManager
                .createNamedQuery(City.FIND_BY_NAME, City.class)
                .setParameter("name", idCriteria.getName())
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }
}
