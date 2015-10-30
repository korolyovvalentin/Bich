package org.fuc.commands.path;

import org.fuc.core.Command;
import org.fuc.core.criterias.PathRequestCriteria;
import org.fuc.entities.PathRequest;
import org.fuc.entities.Request;
import org.fuc.entities.RequestStatus;
import org.fuc.entities.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Transactional
@Repository("createPathRequestCommand")
public class CreatePathRequestCommand implements Command<PathRequestCriteria>{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("createRequestCommand")
    private Command<Request> createRequestCommand;

    @Override
    public void execute(PathRequestCriteria criteria) {
        List<Ride> rides = criteria.getRides();
        PathRequest pathRequest = new PathRequest(RequestStatus.NEW, criteria.getAccount());
        entityManager.persist(pathRequest);

        for(Ride ride : rides){
            Request request = new Request(RequestStatus.NEW, ride, criteria.getAccount());
            request.setPathRequest(pathRequest);
            pathRequest.getRequests().add(request);
            createRequestCommand.execute(request);
        }

        pathRequest.setDate(rides.get(0).getDate());
        pathRequest.setStart(criteria.getStart());
        pathRequest.setFinish(criteria.getEnd());

        entityManager.merge(pathRequest);
    }
}
