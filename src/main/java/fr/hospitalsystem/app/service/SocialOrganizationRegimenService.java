package fr.hospitalsystem.app.service;

import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import fr.hospitalsystem.app.repository.SocialOrganizationRegimenRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationRegimenSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SocialOrganizationRegimen}.
 */
@Service
@Transactional
public class SocialOrganizationRegimenService {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationRegimenService.class);

    private final SocialOrganizationRegimenRepository socialOrganizationRegimenRepository;

    private final SocialOrganizationRegimenSearchRepository socialOrganizationRegimenSearchRepository;

    public SocialOrganizationRegimenService(SocialOrganizationRegimenRepository socialOrganizationRegimenRepository, SocialOrganizationRegimenSearchRepository socialOrganizationRegimenSearchRepository) {
        this.socialOrganizationRegimenRepository = socialOrganizationRegimenRepository;
        this.socialOrganizationRegimenSearchRepository = socialOrganizationRegimenSearchRepository;
    }

    /**
     * Save a socialOrganizationRegimen.
     *
     * @param socialOrganizationRegimen the entity to save.
     * @return the persisted entity.
     */
    public SocialOrganizationRegimen save(SocialOrganizationRegimen socialOrganizationRegimen) {
        log.debug("Request to save SocialOrganizationRegimen : {}", socialOrganizationRegimen);
        SocialOrganizationRegimen result = socialOrganizationRegimenRepository.save(socialOrganizationRegimen);
        socialOrganizationRegimenSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the socialOrganizationRegimen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganizationRegimen> findAll(Pageable pageable) {
        log.debug("Request to get all SocialOrganizationRegimen");
        return socialOrganizationRegimenRepository.findAll(pageable);
    }


    /**
     * Get one socialOrganizationRegimen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialOrganizationRegimen> findOne(Long id) {
        log.debug("Request to get SocialOrganizationRegimen : {}", id);
        return socialOrganizationRegimenRepository.findById(id);
    }

    /**
     * Delete the socialOrganizationRegimen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialOrganizationRegimen : {}", id);
        socialOrganizationRegimenRepository.deleteById(id);
        socialOrganizationRegimenSearchRepository.deleteById(id);
    }

    /**
     * Search for the socialOrganizationRegimen corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganizationRegimen> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SocialOrganizationRegimen for query {}", query);
        return socialOrganizationRegimenSearchRepository.search(queryStringQuery(query), pageable);    }
}
