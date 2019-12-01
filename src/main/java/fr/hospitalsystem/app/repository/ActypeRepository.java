package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Actype;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Actype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActypeRepository extends JpaRepository<Actype, Long> {

}
