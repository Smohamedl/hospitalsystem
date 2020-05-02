package fr.hospitalsystem.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.hospitalsystem.app.domain.Act;

/**
 * Spring Data repository for the Act entity.
 */
@Repository
public interface ActRepository extends JpaRepository<Act, Long> {
    @Query("from Act as act where act.receiptAct.id = ?1")
    Optional<Act> findByReceiptId(long id);

    @Query(value = "select distinct act from Act act left join fetch act.actypes", countQuery = "select count(distinct act) from Act act")
    Page<Act> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct act from Act act left join fetch act.actypes")
    List<Act> findAllWithEagerRelationships();

    @Query("select act from Act act left join fetch act.actypes where act.id =:id")
    Optional<Act> findOneWithEagerRelationships(@Param("id") Long id);

}
