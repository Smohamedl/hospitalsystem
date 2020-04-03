package fr.hospitalsystem.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.ReceiptAct;

/**
 * Spring Data repository for the ReceiptAct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceiptActRepository extends JpaRepository<ReceiptAct, Long> {
    // @Query("select a.receiptAct from Act a where a.doctor.name = ?1")
    @Query("select r from ReceiptAct r where r.act.doctor.name = ?1")
    Page<ReceiptAct> findByService(String service, Pageable pageable);
}
