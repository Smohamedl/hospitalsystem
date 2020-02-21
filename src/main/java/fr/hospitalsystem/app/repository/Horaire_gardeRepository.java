package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Horaire_garde;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Horaire_garde entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Horaire_gardeRepository extends JpaRepository<Horaire_garde, Long> {

}
