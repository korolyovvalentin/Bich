package org.fuc.queries.city;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.entities.City;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collection;

@Component("citiesExceptSelectedQuery")
public class CitiesExceptSelectedQuery implements Query<City> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<City> query(Criteria criteria) {
        SelectedCitiesCriteria selectedCitiesCriteria = (SelectedCitiesCriteria) criteria;
        City[] cities = selectedCitiesCriteria.getCities();

        Long[] ids = new Long[cities.length];
        for (int i = 0; i < cities.length; i++) {
            ids[i] = cities[i].getId();
        }

        javax.persistence.Query query = entityManager.createQuery("select c from City c where NOT c.id IN (:ids)", City.class);
        query.setParameter("ids", Arrays.asList(ids));
        return query.getResultList();
    }
}