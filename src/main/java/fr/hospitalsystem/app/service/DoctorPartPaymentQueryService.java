package fr.hospitalsystem.app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import fr.hospitalsystem.app.domain.DoctorPartPayment;
import fr.hospitalsystem.app.repository.DoctorPartPaymentRepository;
import fr.hospitalsystem.app.repository.search.DoctorPartPaymentSearchRepository;
import fr.hospitalsystem.app.service.dto.DoctorPartPaymentCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DoctorPartPayment} entities in the database. The main input is a {@link DoctorPartPaymentCriteria}
 * which gets converted to {@link Specification}, in a way that all the filters must apply. It returns a {@link List} of {@link DoctorPartPayment} or
 * a {@link Page} of {@link DoctorPartPayment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorPartPaymentQueryService extends QueryService<DoctorPartPayment> {

    private final Logger log = LoggerFactory.getLogger(DoctorPartPaymentQueryService.class);

    private final DoctorPartPaymentRepository doctorPartPaymentRepository;

    private final DoctorPartPaymentSearchRepository doctorPartPaymentSearchRepository;

    public DoctorPartPaymentQueryService(DoctorPartPaymentRepository doctorPartPaymentRepository,
            DoctorPartPaymentSearchRepository doctorPartPaymentSearchRepository) {
        this.doctorPartPaymentRepository = doctorPartPaymentRepository;
        this.doctorPartPaymentSearchRepository = doctorPartPaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DoctorPartPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DoctorPartPayment> findByCriteria(DoctorPartPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DoctorPartPayment> specification = createSpecification(criteria);
        return doctorPartPaymentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DoctorPartPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorPartPayment> findByCriteria(DoctorPartPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DoctorPartPayment> specification = createSpecification(criteria);
        return doctorPartPaymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorPartPaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DoctorPartPayment> specification = createSpecification(criteria);
        return doctorPartPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorPartPaymentCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DoctorPartPayment> createSpecification(DoctorPartPaymentCriteria criteria) {
        Specification<DoctorPartPayment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getId(), DoctorPartPayment_.id));
            }
            if (criteria.getTotal() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getTotal(), DoctorPartPayment_.total));
            }
            if (criteria.getReference() != null) {
                // specification = specification.and(buildStringSpecification(criteria.getReference(), DoctorPartPayment_.reference));
            }
            if (criteria.getDate() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getDate(), DoctorPartPayment_.date));
            }
            if (criteria.getDoctorId() != null) {
                // specification = specification.and(buildSpecification(criteria.getDoctorId(),
                // root -> root.join(DoctorPartPayment_.doctor, JoinType.LEFT).get(Doctor_.id)));
            }
        }
        return specification;
    }
}
