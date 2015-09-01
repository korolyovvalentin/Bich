package org.fuc.commands.city;

import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Component("createCityCommand")
public class CreateCityCommand implements Command {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Criteria criteria) {
        CityCriteria cityCriteria = (CityCriteria) criteria;
        entityManager.persist(cityCriteria.getCity());
    }
}
