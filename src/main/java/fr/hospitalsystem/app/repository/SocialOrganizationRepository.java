package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.SocialOrganization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SocialOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialOrganizationRepository extends JpaRepository<SocialOrganization, Long>, JpaSpecificationExecutor<SocialOrganization> {

}
