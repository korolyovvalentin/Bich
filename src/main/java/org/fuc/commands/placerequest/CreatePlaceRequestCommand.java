package org.fuc.commands.placerequest;

import org.fuc.core.Command;
import org.fuc.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createPlaceRequestCommand")
public class CreatePlaceRequestCommand implements Command<PlaceRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(PlaceRequest placeRequest) {
        entityManager.persist(placeRequest);
    }
}
