package fr.hospitalsystem.app.repository;
import fr.hospitalsystem.app.domain.QuantityPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuantityPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuantityPriceRepository extends JpaRepository<QuantityPrice, Long> {

}
