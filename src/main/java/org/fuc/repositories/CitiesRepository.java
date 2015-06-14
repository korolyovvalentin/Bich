package org.fuc.repositories;

import org.fuc.entities.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public City findById(Long id) {
        return entityManager
                .createNamedQuery(City.FIND_BY_ID, City.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
