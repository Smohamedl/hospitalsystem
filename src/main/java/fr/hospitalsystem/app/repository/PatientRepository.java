package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.Patient;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	@Query("select p from Patient p where p.name = ?1")
	List<Patient> findByName(String name);
	
	@Query("select p from Patient p where p.tel = ?1")
	List<Patient> findByTel(String tel);
}
