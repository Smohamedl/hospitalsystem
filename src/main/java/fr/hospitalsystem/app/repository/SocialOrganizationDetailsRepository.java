package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.SocialOrganizationDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SocialOrganizationDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialOrganizationDetailsRepository extends JpaRepository<SocialOrganizationDetails, Long> {

}
