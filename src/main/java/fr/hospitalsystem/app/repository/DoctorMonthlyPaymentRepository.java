package fr.hospitalsystem.app.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;

/**
 * Spring Data repository for the DoctorMonthlyPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorMonthlyPaymentRepository extends JpaRepository<DoctorMonthlyPayment, Long>, JpaSpecificationExecutor<DoctorMonthlyPayment> {
    @Query("from DoctorMonthlyPayment as d where d.date = ?1 and d.doctor = ?2")
    Optional<DoctorMonthlyPayment> findOneByDate(LocalDate date, Doctor doctor);
}
