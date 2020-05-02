package fr.hospitalsystem.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.hospitalsystem.app.domain.ReceiptAct;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.search.ReceiptActSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.ReceiptAct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReceiptActResource {

    private final Logger log = LoggerFactory.getLogger(ReceiptActResource.class);

    private static final String ENTITY_NAME = "receiptAct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiptActRepository receiptActRepository;

    private final ReceiptActSearchRepository receiptActSearchRepository;

    public ReceiptActResource(ReceiptActRepository receiptActRepository, ReceiptActSearchRepository receiptActSearchRepository) {
        this.receiptActRepository = receiptActRepository;
        this.receiptActSearchRepository = receiptActSearchRepository;
    }

    /**
     * {@code POST  /receipt-acts} : Create a new receiptAct.
     *
     * @param receiptAct the receiptAct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiptAct, or with status {@code 400 (Bad Request)}
     *         if the receiptAct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/receipt-acts")
    public ResponseEntity<ReceiptAct> createReceiptAct(@RequestBody ReceiptAct receiptAct) throws URISyntaxException {
        log.debug("REST request to save ReceiptAct : {}", receiptAct);
        if (receiptAct.getId() != null) {
            throw new BadRequestAlertException("A new receiptAct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceiptAct result = receiptActRepository.save(receiptAct);
        receiptActSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/receipt-acts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /receipt-acts} : Updates an existing receiptAct.
     *
     * @param receiptAct the receiptAct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptAct, or with status {@code 400 (Bad Request)}
     *         if the receiptAct is not valid, or with status {@code 500 (Internal Server Error)} if the receiptAct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receipt-acts")
    public ResponseEntity<ReceiptAct> updateReceiptAct(@RequestBody ReceiptAct receiptAct) throws URISyntaxException {
        log.debug("REST request to update ReceiptAct : {}", receiptAct);
        if (receiptAct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReceiptAct result = receiptActRepository.save(receiptAct);
        receiptActSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receiptAct.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /receipt-acts} : get all the receiptActs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receiptActs in body.
     */
    @GetMapping("/receipt-acts")
    public ResponseEntity<List<ReceiptAct>> getAllReceiptActs(Pageable pageable) {
        log.debug("REST request to get a page of ReceiptActs");
        Page<ReceiptAct> page = receiptActRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /receipt-acts/:id} : get the "id" receiptAct.
     *
     * @param id the id of the receiptAct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiptAct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receipt-acts/{id}")
    public ResponseEntity<ReceiptAct> getReceiptAct(@PathVariable Long id) {
        log.debug("REST request to get ReceiptAct : {}", id);
        Optional<ReceiptAct> receiptAct = receiptActRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(receiptAct);
    }

    /**
     * {@code DELETE  /receipt-acts/:id} : delete the "id" receiptAct.
     *
     * @param id the id of the receiptAct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receipt-acts/{id}")
    public ResponseEntity<Void> deleteReceiptAct(@PathVariable Long id) {
        log.debug("REST request to delete ReceiptAct : {}", id);
        receiptActRepository.deleteById(id);
        receiptActSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/receipt-acts?query=:query} : search for the receiptAct corresponding to the query.
     *
     * @param query    the query of the receiptAct search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/receipt-acts")
    public ResponseEntity<List<ReceiptAct>> searchReceiptActs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReceiptActs for query {}", query);
        Page<ReceiptAct> page = receiptActSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/receipt-acts/doctor/{service}")
    public ResponseEntity<List<ReceiptAct>> getReceiptActsByService(Pageable pageable, @PathVariable String service) {
        log.debug("REST request to get a page of ReceiptActs");
        Page<ReceiptAct> page = receiptActRepository.findByService(service, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
