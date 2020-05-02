package fr.hospitalsystem.app.service;

import fr.hospitalsystem.app.domain.SocialOrganizationDetails;
import fr.hospitalsystem.app.repository.SocialOrganizationDetailsRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationDetailsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SocialOrganizationDetails}.
 */
@Service
@Transactional
public class SocialOrganizationDetailsService {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationDetailsService.class);

    private final SocialOrganizationDetailsRepository socialOrganizationDetailsRepository;

    private final SocialOrganizationDetailsSearchRepository socialOrganizationDetailsSearchRepository;

    public SocialOrganizationDetailsService(SocialOrganizationDetailsRepository socialOrganizationDetailsRepository, SocialOrganizationDetailsSearchRepository socialOrganizationDetailsSearchRepository) {
        this.socialOrganizationDetailsRepository = socialOrganizationDetailsRepository;
        this.socialOrganizationDetailsSearchRepository = socialOrganizationDetailsSearchRepository;
    }

    /**
     * Save a socialOrganizationDetails.
     *
     * @param socialOrganizationDetails the entity to save.
     * @return the persisted entity.
     */
    public SocialOrganizationDetails save(SocialOrganizationDetails socialOrganizationDetails) {
        log.debug("Request to save SocialOrganizationDetails : {}", socialOrganizationDetails);
        SocialOrganizationDetails result = socialOrganizationDetailsRepository.save(socialOrganizationDetails);
        socialOrganizationDetailsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the socialOrganizationDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganizationDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SocialOrganizationDetails");
        return socialOrganizationDetailsRepository.findAll(pageable);
    }


    /**
     * Get one socialOrganizationDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialOrganizationDetails> findOne(Long id) {
        log.debug("Request to get SocialOrganizationDetails : {}", id);
        return socialOrganizationDetailsRepository.findById(id);
    }

    /**
     * Delete the socialOrganizationDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialOrganizationDetails : {}", id);
        socialOrganizationDetailsRepository.deleteById(id);
        socialOrganizationDetailsSearchRepository.deleteById(id);
    }

    /**
     * Search for the socialOrganizationDetails corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialOrganizationDetails> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SocialOrganizationDetails for query {}", query);
        return socialOrganizationDetailsSearchRepository.search(queryStringQuery(query), pageable);    }
}
