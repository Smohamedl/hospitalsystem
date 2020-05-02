package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.DoctorPartPayment;
import fr.hospitalsystem.app.service.DoctorPartPaymentService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import fr.hospitalsystem.app.service.dto.DoctorPartPaymentCriteria;
import fr.hospitalsystem.app.service.DoctorPartPaymentQueryService;

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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.DoctorPartPayment}.
 */
@RestController
@RequestMapping("/api")
public class DoctorPartPaymentResource {

    private final Logger log = LoggerFactory.getLogger(DoctorPartPaymentResource.class);

    private static final String ENTITY_NAME = "doctorPartPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorPartPaymentService doctorPartPaymentService;

    private final DoctorPartPaymentQueryService doctorPartPaymentQueryService;

    public DoctorPartPaymentResource(DoctorPartPaymentService doctorPartPaymentService, DoctorPartPaymentQueryService doctorPartPaymentQueryService) {
        this.doctorPartPaymentService = doctorPartPaymentService;
        this.doctorPartPaymentQueryService = doctorPartPaymentQueryService;
    }

    /**
     * {@code POST  /doctor-part-payments} : Create a new doctorPartPayment.
     *
     * @param doctorPartPayment the doctorPartPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorPartPayment, or with status {@code 400 (Bad Request)} if the doctorPartPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctor-part-payments")
    public ResponseEntity<DoctorPartPayment> createDoctorPartPayment(@Valid @RequestBody DoctorPartPayment doctorPartPayment) throws URISyntaxException {
        log.debug("REST request to save DoctorPartPayment : {}", doctorPartPayment);
        if (doctorPartPayment.getId() != null) {
            throw new BadRequestAlertException("A new doctorPartPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorPartPayment result = doctorPartPaymentService.save(doctorPartPayment);
        return ResponseEntity.created(new URI("/api/doctor-part-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctor-part-payments} : Updates an existing doctorPartPayment.
     *
     * @param doctorPartPayment the doctorPartPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorPartPayment,
     * or with status {@code 400 (Bad Request)} if the doctorPartPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorPartPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctor-part-payments")
    public ResponseEntity<DoctorPartPayment> updateDoctorPartPayment(@Valid @RequestBody DoctorPartPayment doctorPartPayment) throws URISyntaxException {
        log.debug("REST request to update DoctorPartPayment : {}", doctorPartPayment);
        if (doctorPartPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorPartPayment result = doctorPartPaymentService.save(doctorPartPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorPartPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctor-part-payments} : get all the doctorPartPayments.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorPartPayments in body.
     */
    @GetMapping("/doctor-part-payments")
    public ResponseEntity<List<DoctorPartPayment>> getAllDoctorPartPayments(DoctorPartPaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DoctorPartPayments by criteria: {}", criteria);
        Page<DoctorPartPayment> page = doctorPartPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /doctor-part-payments/count} : count all the doctorPartPayments.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/doctor-part-payments/count")
    public ResponseEntity<Long> countDoctorPartPayments(DoctorPartPaymentCriteria criteria) {
        log.debug("REST request to count DoctorPartPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(doctorPartPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doctor-part-payments/:id} : get the "id" doctorPartPayment.
     *
     * @param id the id of the doctorPartPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorPartPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctor-part-payments/{id}")
    public ResponseEntity<DoctorPartPayment> getDoctorPartPayment(@PathVariable Long id) {
        log.debug("REST request to get DoctorPartPayment : {}", id);
        Optional<DoctorPartPayment> doctorPartPayment = doctorPartPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorPartPayment);
    }

    /**
     * {@code DELETE  /doctor-part-payments/:id} : delete the "id" doctorPartPayment.
     *
     * @param id the id of the doctorPartPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctor-part-payments/{id}")
    public ResponseEntity<Void> deleteDoctorPartPayment(@PathVariable Long id) {
        log.debug("REST request to delete DoctorPartPayment : {}", id);
        doctorPartPaymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/doctor-part-payments?query=:query} : search for the doctorPartPayment corresponding
     * to the query.
     *
     * @param query the query of the doctorPartPayment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/doctor-part-payments")
    public ResponseEntity<List<DoctorPartPayment>> searchDoctorPartPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorPartPayments for query {}", query);
        Page<DoctorPartPayment> page = doctorPartPaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
