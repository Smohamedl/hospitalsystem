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
import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;
import fr.hospitalsystem.app.repository.DoctorMonthlyPaymentRepository;
import fr.hospitalsystem.app.repository.search.DoctorMonthlyPaymentSearchRepository;
import fr.hospitalsystem.app.service.dto.DoctorMonthlyPaymentCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DoctorMonthlyPayment} entities in the database. The main input is a
 * {@link DoctorMonthlyPaymentCriteria} which gets converted to {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link DoctorMonthlyPayment} or a {@link Page} of {@link DoctorMonthlyPayment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorMonthlyPaymentQueryService extends QueryService<DoctorMonthlyPayment> {

    private final Logger log = LoggerFactory.getLogger(DoctorMonthlyPaymentQueryService.class);

    private final DoctorMonthlyPaymentRepository doctorMonthlyPaymentRepository;

    private final DoctorMonthlyPaymentSearchRepository doctorMonthlyPaymentSearchRepository;

    public DoctorMonthlyPaymentQueryService(DoctorMonthlyPaymentRepository doctorMonthlyPaymentRepository,
            DoctorMonthlyPaymentSearchRepository doctorMonthlyPaymentSearchRepository) {
        this.doctorMonthlyPaymentRepository = doctorMonthlyPaymentRepository;
        this.doctorMonthlyPaymentSearchRepository = doctorMonthlyPaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DoctorMonthlyPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DoctorMonthlyPayment> findByCriteria(DoctorMonthlyPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DoctorMonthlyPayment> specification = createSpecification(criteria);
        return doctorMonthlyPaymentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DoctorMonthlyPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorMonthlyPayment> findByCriteria(DoctorMonthlyPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DoctorMonthlyPayment> specification = createSpecification(criteria);
        return doctorMonthlyPaymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorMonthlyPaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DoctorMonthlyPayment> specification = createSpecification(criteria);
        return doctorMonthlyPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorMonthlyPaymentCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DoctorMonthlyPayment> createSpecification(DoctorMonthlyPaymentCriteria criteria) {
        Specification<DoctorMonthlyPayment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getId(), DoctorMonthlyPayment_.id));
            }
            if (criteria.getPaid() != null) {
                // specification = specification.and(buildSpecification(criteria.getPaid(), DoctorMonthlyPayment_.paid));
            }
            if (criteria.getDate() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getDate(), DoctorMonthlyPayment_.date));
            }
            if (criteria.getReference() != null) {
                // specification = specification.and(buildStringSpecification(criteria.getReference(), DoctorMonthlyPayment_.reference));
            }
            if (criteria.getDoctorId() != null) {
                // specification = specification.and(buildSpecification(criteria.getDoctorId(),
                // root -> root.join(DoctorMonthlyPayment_.doctor, JoinType.LEFT).get(Doctor_.id)));
            }
        }
        return specification;
    }
}
