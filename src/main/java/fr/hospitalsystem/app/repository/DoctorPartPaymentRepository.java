package fr.hospitalsystem.app.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.domain.DoctorPartPayment;

/**
 * Spring Data repository for the DoctorPartPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorPartPaymentRepository extends JpaRepository<DoctorPartPayment, Long>, JpaSpecificationExecutor<DoctorPartPayment> {
    @Query("from DoctorPartPayment as d where d.date = ?1 and d.doctor = ?2")
    Optional<DoctorPartPayment> findOneByDate(LocalDate date, Doctor doctor);

    @Query("from DoctorPartPayment as d where d.doctor = ?1")
    Optional<DoctorPartPayment> findByDoctor(Doctor doctor);
}
