package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Guard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Guard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardRepository extends JpaRepository<Guard, Long> {

}
