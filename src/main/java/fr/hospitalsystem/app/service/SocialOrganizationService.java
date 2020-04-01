package fr.hospitalsystem.app.service;

import fr.hospitalsystem.app.domain.SocialOrganization;
import fr.hospitalsystem.app.repository.SocialOrganizationRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SocialOrganization}.
 */
@Service
@Transactional
public class SocialOrganizationService {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationService.class);

    private final SocialOrganizationRepository socialOrganizationRepository;

    private final SocialOrganizationSearchRepository socialOrganizationSearchRepository;

    public SocialOrganizationService(SocialOrganizationRepository socialOrganizationRepository, SocialOrganizationSearchRepository socialOrganizationSearchRepository) {
        this.socialOrganizationRepository = socialOrganizationRepository;
        this.socialOrganizationSearchRepository = socialOrganizationSearchRepository;
    }

    /**
     * Save a socialOrganization.
     *
     * @param socialOrganization the entity to save.
     * @return the persisted entity.
     */
    public SocialOrganization save(SocialOrganization socialOrganization) {
        log.debug("Request to save SocialOrganization : {}", socialOrganization);
        SocialOrganization result = socialOrganizationRepository.save(socialOrganization);
        socialOrganizationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the socialOrganizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganization> findAll(Pageable pageable) {
        log.debug("Request to get all SocialOrganizations");
        return socialOrganizationRepository.findAll(pageable);
    }


    /**
     * Get one socialOrganization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialOrganization> findOne(Long id) {
        log.debug("Request to get SocialOrganization : {}", id);
        return socialOrganizationRepository.findById(id);
    }

    /**
     * Delete the socialOrganization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialOrganization : {}", id);
        socialOrganizationRepository.deleteById(id);
        socialOrganizationSearchRepository.deleteById(id);
    }

    /**
     * Search for the socialOrganization corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganization> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SocialOrganizations for query {}", query);
        return socialOrganizationSearchRepository.search(queryStringQuery(query), pageable);    }
}
