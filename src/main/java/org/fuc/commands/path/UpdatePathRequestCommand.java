package org.fuc.commands.path;

import org.fuc.core.Command;
import org.fuc.entities.PathRequest;
import org.fuc.entities.Request;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository("updatePathRequestCommand")
public class UpdatePathRequestCommand implements Command<PathRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void execute(PathRequest pathRequest) {
        entityManager.merge(pathRequest);
    }
}
