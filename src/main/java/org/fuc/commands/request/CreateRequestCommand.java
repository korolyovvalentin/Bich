package org.fuc.commands.request;

import org.fuc.core.Command;
import org.fuc.entities.Request;
import org.fuc.entities.RequestStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("createRequestCommand")
public class CreateRequestCommand implements Command<Request> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(Request placeRequest) {
        placeRequest.setStatus(RequestStatus.NEW);
        entityManager.persist(placeRequest);
    }
}
