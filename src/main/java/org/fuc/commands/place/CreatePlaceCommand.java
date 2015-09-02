package org.fuc.commands.place;

import org.fuc.core.Command;
import org.fuc.entities.Place;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createPlaceCommand")
public class CreatePlaceCommand implements Command<Place> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Place place) {
        entityManager.persist(place);
    }
}
