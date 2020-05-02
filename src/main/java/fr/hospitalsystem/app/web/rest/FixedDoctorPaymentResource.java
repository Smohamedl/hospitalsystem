package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import fr.hospitalsystem.app.service.FixedDoctorPaymentService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import fr.hospitalsystem.app.service.dto.FixedDoctorPaymentCriteria;
import fr.hospitalsystem.app.service.FixedDoctorPaymentQueryService;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.FixedDoctorPayment}.
 */
@RestController
@RequestMapping("/api")
public class FixedDoctorPaymentResource {

    private final Logger log = LoggerFactory.getLogger(FixedDoctorPaymentResource.class);

    private static final String ENTITY_NAME = "fixedDoctorPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FixedDoctorPaymentService fixedDoctorPaymentService;

    private final FixedDoctorPaymentQueryService fixedDoctorPaymentQueryService;

    public FixedDoctorPaymentResource(FixedDoctorPaymentService fixedDoctorPaymentService, FixedDoctorPaymentQueryService fixedDoctorPaymentQueryService) {
        this.fixedDoctorPaymentService = fixedDoctorPaymentService;
        this.fixedDoctorPaymentQueryService = fixedDoctorPaymentQueryService;
    }

    /**
     * {@code POST  /fixed-doctor-payments} : Create a new fixedDoctorPayment.
     *
     * @param fixedDoctorPayment the fixedDoctorPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fixedDoctorPayment, or with status {@code 400 (Bad Request)} if the fixedDoctorPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fixed-doctor-payments")
    public ResponseEntity<FixedDoctorPayment> createFixedDoctorPayment(@Valid @RequestBody FixedDoctorPayment fixedDoctorPayment) throws URISyntaxException {
        log.debug("REST request to save FixedDoctorPayment : {}", fixedDoctorPayment);
        if (fixedDoctorPayment.getId() != null) {
            throw new BadRequestAlertException("A new fixedDoctorPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedDoctorPayment result = fixedDoctorPaymentService.save(fixedDoctorPayment);
        return ResponseEntity.created(new URI("/api/fixed-doctor-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fixed-doctor-payments} : Updates an existing fixedDoctorPayment.
     *
     * @param fixedDoctorPayment the fixedDoctorPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedDoctorPayment,
     * or with status {@code 400 (Bad Request)} if the fixedDoctorPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fixedDoctorPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fixed-doctor-payments")
    public ResponseEntity<FixedDoctorPayment> updateFixedDoctorPayment(@Valid @RequestBody FixedDoctorPayment fixedDoctorPayment) throws URISyntaxException {
        log.debug("REST request to update FixedDoctorPayment : {}", fixedDoctorPayment);
        if (fixedDoctorPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedDoctorPayment result = fixedDoctorPaymentService.save(fixedDoctorPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fixedDoctorPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fixed-doctor-payments} : get all the fixedDoctorPayments.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fixedDoctorPayments in body.
     */
    @GetMapping("/fixed-doctor-payments")
    public ResponseEntity<List<FixedDoctorPayment>> getAllFixedDoctorPayments(FixedDoctorPaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FixedDoctorPayments by criteria: {}", criteria);
        Page<FixedDoctorPayment> page = fixedDoctorPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /fixed-doctor-payments/count} : count all the fixedDoctorPayments.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/fixed-doctor-payments/count")
    public ResponseEntity<Long> countFixedDoctorPayments(FixedDoctorPaymentCriteria criteria) {
        log.debug("REST request to count FixedDoctorPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(fixedDoctorPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fixed-doctor-payments/:id} : get the "id" fixedDoctorPayment.
     *
     * @param id the id of the fixedDoctorPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fixedDoctorPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fixed-doctor-payments/{id}")
    public ResponseEntity<FixedDoctorPayment> getFixedDoctorPayment(@PathVariable Long id) {
        log.debug("REST request to get FixedDoctorPayment : {}", id);
        Optional<FixedDoctorPayment> fixedDoctorPayment = fixedDoctorPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedDoctorPayment);
    }

    /**
     * {@code DELETE  /fixed-doctor-payments/:id} : delete the "id" fixedDoctorPayment.
     *
     * @param id the id of the fixedDoctorPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fixed-doctor-payments/{id}")
    public ResponseEntity<Void> deleteFixedDoctorPayment(@PathVariable Long id) {
        log.debug("REST request to delete FixedDoctorPayment : {}", id);
        fixedDoctorPaymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fixed-doctor-payments?query=:query} : search for the fixedDoctorPayment corresponding
     * to the query.
     *
     * @param query the query of the fixedDoctorPayment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fixed-doctor-payments")
    public ResponseEntity<List<FixedDoctorPayment>> searchFixedDoctorPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FixedDoctorPayments for query {}", query);
        Page<FixedDoctorPayment> page = fixedDoctorPaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
