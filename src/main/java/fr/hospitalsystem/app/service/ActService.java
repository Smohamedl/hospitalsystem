package fr.hospitalsystem.app.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hospitalsystem.app.domain.Act;
import fr.hospitalsystem.app.repository.ActRepository;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.search.ActSearchRepository;

/**
 * Service Implementation for managing {@link Act}.
 */
@Service
@Transactional
public class ActService {

    private final Logger log = LoggerFactory.getLogger(ActService.class);

    private final ActRepository actRepository;

    private final ReceiptActRepository receiptActRepository;

    private final ActSearchRepository actSearchRepository;

    public ActService(ActRepository actRepository, ActSearchRepository actSearchRepository, ReceiptActRepository receiptActRepository) {
        this.actRepository = actRepository;
        this.actSearchRepository = actSearchRepository;
        this.receiptActRepository = receiptActRepository;
    }

    /**
     * Save a act.
     *
     * @param act the entity to save.
     * @return the persisted entity.
     */
    public Act save(Act act) {
        log.debug("Request to save Act : {}", act);
        // receiptActRepository.save(act.getReceiptAct());
        Act result = actRepository.save(act);
        actSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the acts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Act> findAll(Pageable pageable) {
        log.debug("Request to get all Acts");
        return actRepository.findAll(pageable);
    }

    /**
     * Get one act by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Act> findOne(Long id) {
        log.debug("Request to get Act : {}", id);
        return actRepository.findById(id);
    }

    /**
     * Delete the act by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Act : {}", id);
        actRepository.deleteById(id);
        actSearchRepository.deleteById(id);
    }

    /**
     * Search for the act corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Act> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Acts for query {}", query);
        return actSearchRepository.search(queryStringQuery(query), pageable);
    }
}
