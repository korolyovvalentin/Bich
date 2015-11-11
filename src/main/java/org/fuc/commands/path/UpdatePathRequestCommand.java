package org.fuc.commands.path;

import org.fuc.core.Command;
import org.fuc.entities.PathRequest;
import org.fuc.entities.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;

@Transactional
@Repository("updatePathRequestCommand")
public class UpdatePathRequestCommand implements Command<PathRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("updateRequestCommand")
    private Command<Request> updateRequest;

    @Override
    public void execute(PathRequest pathRequest) {
        for (Request request : pathRequest.getRequests()) {
            request.setComment(pathRequest.getComment());
            request.setRating(pathRequest.getRating());
            request.setStatus(pathRequest.getStatus());

            updateRequest.execute(request);
        }

        entityManager.merge(pathRequest);
    }
}
