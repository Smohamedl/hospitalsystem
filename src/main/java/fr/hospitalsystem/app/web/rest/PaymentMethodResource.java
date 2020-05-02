package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.PaymentMethod;
import fr.hospitalsystem.app.repository.PaymentMethodRepository;
import fr.hospitalsystem.app.repository.search.PaymentMethodSearchRepository;
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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.PaymentMethod}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodSearchRepository paymentMethodSearchRepository;

    public PaymentMethodResource(PaymentMethodRepository paymentMethodRepository, PaymentMethodSearchRepository paymentMethodSearchRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodSearchRepository = paymentMethodSearchRepository;
    }

    /**
     * {@code GET  /payment-methods} : get all the paymentMethods.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMethods in body.
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods(Pageable pageable) {
        log.debug("REST request to get a page of PaymentMethods");
        Page<PaymentMethod> page = paymentMethodRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-methods/:id} : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMethod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-methods/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentMethod);
    }

    /**
     * {@code SEARCH  /_search/payment-methods?query=:query} : search for the paymentMethod corresponding
     * to the query.
     *
     * @param query the query of the paymentMethod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-methods")
    public ResponseEntity<List<PaymentMethod>> searchPaymentMethods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentMethods for query {}", query);
        Page<PaymentMethod> page = paymentMethodSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
