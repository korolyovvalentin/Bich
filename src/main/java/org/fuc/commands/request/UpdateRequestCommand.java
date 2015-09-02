package org.fuc.commands.request;

import org.fuc.core.Command;
import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("updateRequestCommand")
public class UpdateRequestCommand implements Command<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Request placeRequest) {
        entityManager.merge(placeRequest);
    }
}
