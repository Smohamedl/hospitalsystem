package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Hospitalization;
import fr.hospitalsystem.app.repository.HospitalizationRepository;
import fr.hospitalsystem.app.repository.search.HospitalizationSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Hospitalization}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HospitalizationResource {

    private final Logger log = LoggerFactory.getLogger(HospitalizationResource.class);

    private static final String ENTITY_NAME = "hospitalization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalizationRepository hospitalizationRepository;

    private final HospitalizationSearchRepository hospitalizationSearchRepository;

    public HospitalizationResource(HospitalizationRepository hospitalizationRepository, HospitalizationSearchRepository hospitalizationSearchRepository) {
        this.hospitalizationRepository = hospitalizationRepository;
        this.hospitalizationSearchRepository = hospitalizationSearchRepository;
    }

    /**
     * {@code POST  /hospitalizations} : Create a new hospitalization.
     *
     * @param hospitalization the hospitalization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospitalization, or with status {@code 400 (Bad Request)} if the hospitalization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hospitalizations")
    public ResponseEntity<Hospitalization> createHospitalization(@Valid @RequestBody Hospitalization hospitalization) throws URISyntaxException {
        log.debug("REST request to save Hospitalization : {}", hospitalization);
        if (hospitalization.getId() != null) {
            throw new BadRequestAlertException("A new hospitalization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hospitalization result = hospitalizationRepository.save(hospitalization);
        hospitalizationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hospitalizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hospitalizations} : Updates an existing hospitalization.
     *
     * @param hospitalization the hospitalization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalization,
     * or with status {@code 400 (Bad Request)} if the hospitalization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospitalization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hospitalizations")
    public ResponseEntity<Hospitalization> updateHospitalization(@Valid @RequestBody Hospitalization hospitalization) throws URISyntaxException {
        log.debug("REST request to update Hospitalization : {}", hospitalization);
        if (hospitalization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hospitalization result = hospitalizationRepository.save(hospitalization);
        hospitalizationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospitalization.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hospitalizations} : get all the hospitalizations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitalizations in body.
     */
    @GetMapping("/hospitalizations")
    public ResponseEntity<List<Hospitalization>> getAllHospitalizations(Pageable pageable) {
        log.debug("REST request to get a page of Hospitalizations");
        Page<Hospitalization> page = hospitalizationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hospitalizations/:id} : get the "id" hospitalization.
     *
     * @param id the id of the hospitalization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospitalization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitalizations/{id}")
    public ResponseEntity<Hospitalization> getHospitalization(@PathVariable Long id) {
        log.debug("REST request to get Hospitalization : {}", id);
        Optional<Hospitalization> hospitalization = hospitalizationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hospitalization);
    }

    /**
     * {@code DELETE  /hospitalizations/:id} : delete the "id" hospitalization.
     *
     * @param id the id of the hospitalization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hospitalizations/{id}")
    public ResponseEntity<Void> deleteHospitalization(@PathVariable Long id) {
        log.debug("REST request to delete Hospitalization : {}", id);
        hospitalizationRepository.deleteById(id);
        hospitalizationSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hospitalizations?query=:query} : search for the hospitalization corresponding
     * to the query.
     *
     * @param query the query of the hospitalization search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/hospitalizations")
    public ResponseEntity<List<Hospitalization>> searchHospitalizations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Hospitalizations for query {}", query);
        Page<Hospitalization> page = hospitalizationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
