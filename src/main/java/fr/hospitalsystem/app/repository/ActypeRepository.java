package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Actype;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Actype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActypeRepository extends JpaRepository<Actype, Long> {
	@Query("select a from Actype a where a.medicalService.name = ?1")
	List<Actype> findByService(String service);
}
