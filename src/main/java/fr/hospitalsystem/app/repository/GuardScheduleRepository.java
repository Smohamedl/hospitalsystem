package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.GuardSchedule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GuardSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardScheduleRepository extends JpaRepository<GuardSchedule, Long> {

}
