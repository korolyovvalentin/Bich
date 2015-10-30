package org.fuc.queries.path;

import org.fuc.core.Criteria;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.PathRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository("pathRequestById")
public class PathRequestById implements QuerySingle<PathRequest> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PathRequest query(Criteria criteria) {
        return entityManager
                .find(PathRequest.class, ((IdCriteria) criteria).getId());
    }

}
