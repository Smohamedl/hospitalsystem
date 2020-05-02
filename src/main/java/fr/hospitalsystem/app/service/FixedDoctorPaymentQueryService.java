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
import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import fr.hospitalsystem.app.repository.FixedDoctorPaymentRepository;
import fr.hospitalsystem.app.repository.search.FixedDoctorPaymentSearchRepository;
import fr.hospitalsystem.app.service.dto.FixedDoctorPaymentCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FixedDoctorPayment} entities in the database. The main input is a
 * {@link FixedDoctorPaymentCriteria} which gets converted to {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link FixedDoctorPayment} or a {@link Page} of {@link FixedDoctorPayment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixedDoctorPaymentQueryService extends QueryService<FixedDoctorPayment> {

    private final Logger log = LoggerFactory.getLogger(FixedDoctorPaymentQueryService.class);

    private final FixedDoctorPaymentRepository fixedDoctorPaymentRepository;

    private final FixedDoctorPaymentSearchRepository fixedDoctorPaymentSearchRepository;

    public FixedDoctorPaymentQueryService(FixedDoctorPaymentRepository fixedDoctorPaymentRepository,
            FixedDoctorPaymentSearchRepository fixedDoctorPaymentSearchRepository) {
        this.fixedDoctorPaymentRepository = fixedDoctorPaymentRepository;
        this.fixedDoctorPaymentSearchRepository = fixedDoctorPaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FixedDoctorPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixedDoctorPayment> findByCriteria(FixedDoctorPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FixedDoctorPayment> specification = createSpecification(criteria);
        return fixedDoctorPaymentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FixedDoctorPayment} which matches the criteria from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedDoctorPayment> findByCriteria(FixedDoctorPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FixedDoctorPayment> specification = createSpecification(criteria);
        return fixedDoctorPaymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixedDoctorPaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FixedDoctorPayment> specification = createSpecification(criteria);
        return fixedDoctorPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link FixedDoctorPaymentCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FixedDoctorPayment> createSpecification(FixedDoctorPaymentCriteria criteria) {
        Specification<FixedDoctorPayment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getId(), FixedDoctorPayment_.id));
            }
            if (criteria.getPaid() != null) {
                // specification = specification.and(buildSpecification(criteria.getPaid(), FixedDoctorPayment_.paid));
            }
            if (criteria.getDate() != null) {
                // specification = specification.and(buildRangeSpecification(criteria.getDate(), FixedDoctorPayment_.date));
            }
            if (criteria.getReference() != null) {
                // specification = specification.and(buildStringSpecification(criteria.getReference(), FixedDoctorPayment_.reference));
            }
            if (criteria.getDoctorId() != null) {
                // specification = specification.and(buildSpecification(criteria.getDoctorId(), root -> root.join(FixedDoctorPayment_.doctor,
                // JoinType.LEFT).get(Doctor_.id)));
            }
        }
        return specification;
    }
}
