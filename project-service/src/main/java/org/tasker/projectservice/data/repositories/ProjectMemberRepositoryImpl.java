package org.tasker.projectservice.data.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.ProjectMember;

import java.util.List;

@Repository
public class ProjectMemberRepositoryImpl implements ProjectMemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProjectMember> findByProjectIdPaged(Long projectId, int limit, int offset) {
        TypedQuery<ProjectMember> query = getProjectMemberQueryById(projectId);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<ProjectMember> findByProjectId(Long projectId) {
        return getProjectMemberQueryById(projectId).getResultList();
    }

    private TypedQuery<ProjectMember> getProjectMemberQueryById(Long projectId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProjectMember> cq = cb.createQuery(ProjectMember.class);
        Root<ProjectMember> root = cq.from(ProjectMember.class);

        Predicate predicate = cb.equal(root.get("project").get("id"), projectId);
        cq.where(predicate);
        return em.createQuery(cq);
    }


}
