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

import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import fr.hospitalsystem.app.domain.*; // for static metamodels
import fr.hospitalsystem.app.repository.SocialOrganizationRegimenRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationRegimenSearchRepository;
import fr.hospitalsystem.app.service.dto.SocialOrganizationRegimenCriteria;

/**
 * Service for executing complex queries for {@link SocialOrganizationRegimen} entities in the database.
 * The main input is a {@link SocialOrganizationRegimenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SocialOrganizationRegimen} or a {@link Page} of {@link SocialOrganizationRegimen} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocialOrganizationRegimenQueryService extends QueryService<SocialOrganizationRegimen> {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationRegimenQueryService.class);

    private final SocialOrganizationRegimenRepository socialOrganizationRegimenRepository;

    private final SocialOrganizationRegimenSearchRepository socialOrganizationRegimenSearchRepository;

    public SocialOrganizationRegimenQueryService(SocialOrganizationRegimenRepository socialOrganizationRegimenRepository, SocialOrganizationRegimenSearchRepository socialOrganizationRegimenSearchRepository) {
        this.socialOrganizationRegimenRepository = socialOrganizationRegimenRepository;
        this.socialOrganizationRegimenSearchRepository = socialOrganizationRegimenSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SocialOrganizationRegimen} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SocialOrganizationRegimen> findByCriteria(SocialOrganizationRegimenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SocialOrganizationRegimen> specification = createSpecification(criteria);
        return socialOrganizationRegimenRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SocialOrganizationRegimen} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganizationRegimen> findByCriteria(SocialOrganizationRegimenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SocialOrganizationRegimen> specification = createSpecification(criteria);
        return socialOrganizationRegimenRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocialOrganizationRegimenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SocialOrganizationRegimen> specification = createSpecification(criteria);
        return socialOrganizationRegimenRepository.count(specification);
    }

    /**
     * Function to convert {@link SocialOrganizationRegimenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SocialOrganizationRegimen> createSpecification(SocialOrganizationRegimenCriteria criteria) {
        Specification<SocialOrganizationRegimen> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                //specification = specification.and(buildRangeSpecification(criteria.getId(), SocialOrganizationRegimen_.id));
            }
            if (criteria.getPercentage() != null) {
                //specification = specification.and(buildRangeSpecification(criteria.getPercentage(), SocialOrganizationRegimen_.percentage));
            }
            if (criteria.getSocialOrganizationId() != null) {
                //specification = specification.and(buildSpecification(criteria.getSocialOrganizationId(),
                //    root -> root.join(SocialOrganizationRegimen_.socialOrganization, JoinType.LEFT).get(SocialOrganization_.id)));
            }
        }
        return specification;
    }
}
