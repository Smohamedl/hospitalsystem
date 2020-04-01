package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("select d from Session d where d.created_by = ?1")
    Page<Session> findAllByCreated_by(String Created_by, Pageable pageable);

}
