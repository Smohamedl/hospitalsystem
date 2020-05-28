package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Session;
import fr.hospitalsystem.app.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("select d from Session d where d.created_by = ?1")
    Page<Session> findAllByCreated_by(String Created_by, Pageable pageable);

    @Query("select d from Session d where d.created_by = ?1 ORDER BY d.created_date desc")
    Optional<Session> findOneByCreateDateOOrderByCreated_date(String created_by);
}
