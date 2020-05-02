package fr.hospitalsystem.app.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.hospitalsystem.app.domain.SocialOrganization;
import fr.hospitalsystem.app.domain.*; // for static metamodels
import fr.hospitalsystem.app.repository.SocialOrganizationRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationSearchRepository;
import fr.hospitalsystem.app.service.dto.SocialOrganizationCriteria;

/**
 * Service for executing complex queries for {@link SocialOrganization} entities in the database.
 * The main input is a {@link SocialOrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SocialOrganization} or a {@link Page} of {@link SocialOrganization} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocialOrganizationQueryService extends QueryService<SocialOrganization> {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationQueryService.class);

    private final SocialOrganizationRepository socialOrganizationRepository;

    private final SocialOrganizationSearchRepository socialOrganizationSearchRepository;

    public SocialOrganizationQueryService(SocialOrganizationRepository socialOrganizationRepository, SocialOrganizationSearchRepository socialOrganizationSearchRepository) {
        this.socialOrganizationRepository = socialOrganizationRepository;
        this.socialOrganizationSearchRepository = socialOrganizationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SocialOrganization} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SocialOrganization> findByCriteria(SocialOrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SocialOrganization> specification = createSpecification(criteria);
        return socialOrganizationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SocialOrganization} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganization> findByCriteria(SocialOrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SocialOrganization> specification = createSpecification(criteria);
        return socialOrganizationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocialOrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SocialOrganization> specification = createSpecification(criteria);
        return socialOrganizationRepository.count(specification);
    }

    /**
     * Function to convert {@link SocialOrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SocialOrganization> createSpecification(SocialOrganizationCriteria criteria) {
        Specification<SocialOrganization> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SocialOrganization_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SocialOrganization_.name));
            }
            if (criteria.getSocialOrganizationRegimenId() != null) {
                specification = specification.and(buildSpecification(criteria.getSocialOrganizationRegimenId(),
                    root -> root.join(SocialOrganization_.socialOrganizationRegimen, JoinType.LEFT).get(SocialOrganizationRegimen_.id)));
            }
        }
        return specification;
    }
}
