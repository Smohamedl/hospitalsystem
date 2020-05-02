package fr.hospitalsystem.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.Actype;

/**
 * Spring Data repository for the Actype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActypeRepository extends JpaRepository<Actype, Long> {
    @Query("select a from Actype a where a.medicalService.name = ?1")
    List<Actype> findByService(String service);
}
