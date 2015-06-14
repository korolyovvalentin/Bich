package org.fuc.repositories;

import org.fuc.entities.City;
import org.fuc.entities.PlaceRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class CitiesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public City save(City city) {
        entityManager.persist(city);
        return city;
    }

    @Transactional
    public void delete(City city) {
        entityManager.remove(entityManager.merge(city));
    }

    public Collection<City> getCities() {
        return entityManager.createNamedQuery(City.ALL_CITIES, City.class).getResultList();
    }

    public Collection<City> getCities(City[] cities){
        Long[] ids = new Long[cities.length];
        for(int i = 0; i < cities.length; i++){
            ids[i] = cities[i].getId();
        }

        Query query = entityManager.createQuery("select c from City c where NOT c.id IN (:ids)", City.class);
        query.setParameter("ids", Arrays.asList(ids));
        return query.getResultList();
    }

    public City findById(Long id) {
        return entityManager
                .createNamedQuery(City.FIND_BY_ID, City.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
