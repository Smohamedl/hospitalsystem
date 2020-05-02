package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;
import fr.hospitalsystem.app.service.DoctorMonthlyPaymentService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import fr.hospitalsystem.app.service.dto.DoctorMonthlyPaymentCriteria;
import fr.hospitalsystem.app.service.DoctorMonthlyPaymentQueryService;

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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.DoctorMonthlyPayment}.
 */
@RestController
@RequestMapping("/api")
public class DoctorMonthlyPaymentResource {

    private final Logger log = LoggerFactory.getLogger(DoctorMonthlyPaymentResource.class);

    private static final String ENTITY_NAME = "doctorMonthlyPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorMonthlyPaymentService doctorMonthlyPaymentService;

    private final DoctorMonthlyPaymentQueryService doctorMonthlyPaymentQueryService;

    public DoctorMonthlyPaymentResource(DoctorMonthlyPaymentService doctorMonthlyPaymentService, DoctorMonthlyPaymentQueryService doctorMonthlyPaymentQueryService) {
        this.doctorMonthlyPaymentService = doctorMonthlyPaymentService;
        this.doctorMonthlyPaymentQueryService = doctorMonthlyPaymentQueryService;
    }

    /**
     * {@code POST  /doctor-monthly-payments} : Create a new doctorMonthlyPayment.
     *
     * @param doctorMonthlyPayment the doctorMonthlyPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorMonthlyPayment, or with status {@code 400 (Bad Request)} if the doctorMonthlyPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctor-monthly-payments")
    public ResponseEntity<DoctorMonthlyPayment> createDoctorMonthlyPayment(@Valid @RequestBody DoctorMonthlyPayment doctorMonthlyPayment) throws URISyntaxException {
        log.debug("REST request to save DoctorMonthlyPayment : {}", doctorMonthlyPayment);
        if (doctorMonthlyPayment.getId() != null) {
            throw new BadRequestAlertException("A new doctorMonthlyPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorMonthlyPayment result = doctorMonthlyPaymentService.save(doctorMonthlyPayment);
        return ResponseEntity.created(new URI("/api/doctor-monthly-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctor-monthly-payments} : Updates an existing doctorMonthlyPayment.
     *
     * @param doctorMonthlyPayment the doctorMonthlyPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorMonthlyPayment,
     * or with status {@code 400 (Bad Request)} if the doctorMonthlyPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorMonthlyPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctor-monthly-payments")
    public ResponseEntity<DoctorMonthlyPayment> updateDoctorMonthlyPayment(@Valid @RequestBody DoctorMonthlyPayment doctorMonthlyPayment) throws URISyntaxException {
        log.debug("REST request to update DoctorMonthlyPayment : {}", doctorMonthlyPayment);
        if (doctorMonthlyPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorMonthlyPayment result = doctorMonthlyPaymentService.save(doctorMonthlyPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorMonthlyPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctor-monthly-payments} : get all the doctorMonthlyPayments.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorMonthlyPayments in body.
     */
    @GetMapping("/doctor-monthly-payments")
    public ResponseEntity<List<DoctorMonthlyPayment>> getAllDoctorMonthlyPayments(DoctorMonthlyPaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DoctorMonthlyPayments by criteria: {}", criteria);
        Page<DoctorMonthlyPayment> page = doctorMonthlyPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /doctor-monthly-payments/count} : count all the doctorMonthlyPayments.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/doctor-monthly-payments/count")
    public ResponseEntity<Long> countDoctorMonthlyPayments(DoctorMonthlyPaymentCriteria criteria) {
        log.debug("REST request to count DoctorMonthlyPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(doctorMonthlyPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doctor-monthly-payments/:id} : get the "id" doctorMonthlyPayment.
     *
     * @param id the id of the doctorMonthlyPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorMonthlyPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctor-monthly-payments/{id}")
    public ResponseEntity<DoctorMonthlyPayment> getDoctorMonthlyPayment(@PathVariable Long id) {
        log.debug("REST request to get DoctorMonthlyPayment : {}", id);
        Optional<DoctorMonthlyPayment> doctorMonthlyPayment = doctorMonthlyPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorMonthlyPayment);
    }

    /**
     * {@code DELETE  /doctor-monthly-payments/:id} : delete the "id" doctorMonthlyPayment.
     *
     * @param id the id of the doctorMonthlyPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctor-monthly-payments/{id}")
    public ResponseEntity<Void> deleteDoctorMonthlyPayment(@PathVariable Long id) {
        log.debug("REST request to delete DoctorMonthlyPayment : {}", id);
        doctorMonthlyPaymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/doctor-monthly-payments?query=:query} : search for the doctorMonthlyPayment corresponding
     * to the query.
     *
     * @param query the query of the doctorMonthlyPayment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/doctor-monthly-payments")
    public ResponseEntity<List<DoctorMonthlyPayment>> searchDoctorMonthlyPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorMonthlyPayments for query {}", query);
        Page<DoctorMonthlyPayment> page = doctorMonthlyPaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
