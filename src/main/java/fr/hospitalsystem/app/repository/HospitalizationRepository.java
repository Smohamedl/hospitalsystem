package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Hospitalization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hospitalization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long> {

}
