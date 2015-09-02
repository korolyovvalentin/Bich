package org.fuc.commands.city;

import org.fuc.core.Command;
import org.fuc.entities.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createCityCommand")
public class CreateCityCommand implements Command<City> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(City city) {
        entityManager.persist(city);
    }
}
