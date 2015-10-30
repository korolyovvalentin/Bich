package org.fuc.commands.path;

import org.fuc.core.Command;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.PathRequest;
import org.fuc.entities.Request;
import org.fuc.entities.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("updatePathRequestStatus")
public class UpdatePathRequestStatusCommand implements Command<IdCriteria> {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Qualifier("pathRequestById")
    private QuerySingle<PathRequest> pathRequestById;

    @Override
    public void execute(IdCriteria context) {
        PathRequest pathRequest = pathRequestById.query(context);
        int approvedCount = 0;

        for (Request request : pathRequest.getRequests()) {
            String status = request.getStatus();
            if(status.equals(RequestStatus.DECLINED)){
                pathRequest.setStatus(RequestStatus.DECLINED);
                break;
            }
            if(status.equals(RequestStatus.APPROVED)){
                approvedCount++;
            }
        }

        if(approvedCount == pathRequest.getRequests().size()){
            pathRequest.setStatus(RequestStatus.APPROVED);
        }

        entityManager.merge(pathRequest);
    }
}
