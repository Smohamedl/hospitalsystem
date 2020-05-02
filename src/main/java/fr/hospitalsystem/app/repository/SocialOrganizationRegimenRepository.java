package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SocialOrganizationRegimen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialOrganizationRegimenRepository extends JpaRepository<SocialOrganizationRegimen, Long>, JpaSpecificationExecutor<SocialOrganizationRegimen> {

}
