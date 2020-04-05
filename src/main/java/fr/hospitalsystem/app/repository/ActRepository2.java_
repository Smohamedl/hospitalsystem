package fr.hospitalsystem.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.Act;

/**
 * Spring Data repository for the Act entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActRepository extends JpaRepository<Act, Long> {
    // from Cat as cat where cat.mate.id = 69
    @Query("from Act as act where act.receiptAct.id = ?1")
    Optional<Act> findByReceiptId(long id);

}
