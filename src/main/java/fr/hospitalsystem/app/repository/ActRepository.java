package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Act;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Act entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActRepository extends JpaRepository<Act, Long> {

}
