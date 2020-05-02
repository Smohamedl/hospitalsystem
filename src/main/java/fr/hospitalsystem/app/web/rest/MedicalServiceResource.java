package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.repository.MedicalServiceRepository;
import fr.hospitalsystem.app.repository.search.MedicalServiceSearchRepository;
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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.MedicalService}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MedicalServiceResource {

    private final Logger log = LoggerFactory.getLogger(MedicalServiceResource.class);

    private static final String ENTITY_NAME = "medicalService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalServiceRepository medicalServiceRepository;

    private final MedicalServiceSearchRepository medicalServiceSearchRepository;

    public MedicalServiceResource(MedicalServiceRepository medicalServiceRepository, MedicalServiceSearchRepository medicalServiceSearchRepository) {
        this.medicalServiceRepository = medicalServiceRepository;
        this.medicalServiceSearchRepository = medicalServiceSearchRepository;
    }

    /**
     * {@code POST  /medical-services} : Create a new medicalService.
     *
     * @param medicalService the medicalService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalService, or with status {@code 400 (Bad Request)} if the medicalService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medical-services")
    public ResponseEntity<MedicalService> createMedicalService(@Valid @RequestBody MedicalService medicalService) throws URISyntaxException {
        log.debug("REST request to save MedicalService : {}", medicalService);
        if (medicalService.getId() != null) {
            throw new BadRequestAlertException("A new medicalService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicalService result = medicalServiceRepository.save(medicalService);
        medicalServiceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medical-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medical-services} : Updates an existing medicalService.
     *
     * @param medicalService the medicalService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalService,
     * or with status {@code 400 (Bad Request)} if the medicalService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medical-services")
    public ResponseEntity<MedicalService> updateMedicalService(@Valid @RequestBody MedicalService medicalService) throws URISyntaxException {
        log.debug("REST request to update MedicalService : {}", medicalService);
        if (medicalService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MedicalService result = medicalServiceRepository.save(medicalService);
        medicalServiceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicalService.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medical-services} : get all the medicalServices.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalServices in body.
     */
    @GetMapping("/medical-services")
    public ResponseEntity<List<MedicalService>> getAllMedicalServices(Pageable pageable) {
        log.debug("REST request to get a page of MedicalServices");
        Page<MedicalService> page = medicalServiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-services/:id} : get the "id" medicalService.
     *
     * @param id the id of the medicalService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medical-services/{id}")
    public ResponseEntity<MedicalService> getMedicalService(@PathVariable Long id) {
        log.debug("REST request to get MedicalService : {}", id);
        Optional<MedicalService> medicalService = medicalServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicalService);
    }

    /**
     * {@code DELETE  /medical-services/:id} : delete the "id" medicalService.
     *
     * @param id the id of the medicalService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medical-services/{id}")
    public ResponseEntity<Void> deleteMedicalService(@PathVariable Long id) {
        log.debug("REST request to delete MedicalService : {}", id);
        medicalServiceRepository.deleteById(id);
        medicalServiceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/medical-services?query=:query} : search for the medicalService corresponding
     * to the query.
     *
     * @param query the query of the medicalService search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/medical-services")
    public ResponseEntity<List<MedicalService>> searchMedicalServices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MedicalServices for query {}", query);
        Page<MedicalService> page = medicalServiceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
