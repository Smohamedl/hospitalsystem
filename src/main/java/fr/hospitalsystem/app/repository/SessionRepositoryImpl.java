package fr.hospitalsystem.app.repository;

import fr.hospitalsystem.app.domain.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SessionRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Session> findOneByCreateDateOOrderByCreated_dateLimitX(String created_by, int limit) {
        final String request = "SELECT s FROM Session s where s.created_by = " + created_by + " ORDER BY s.created_date";
        return entityManager.createQuery(request, Session.class).setMaxResults(limit).getResultList();
    }
}
