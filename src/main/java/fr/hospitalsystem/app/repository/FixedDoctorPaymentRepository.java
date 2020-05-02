package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FixedDoctorPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedDoctorPaymentRepository extends JpaRepository<FixedDoctorPayment, Long>, JpaSpecificationExecutor<FixedDoctorPayment> {

}
