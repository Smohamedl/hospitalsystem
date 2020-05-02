package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.QuantityPrice;
import fr.hospitalsystem.app.repository.QuantityPriceRepository;
import fr.hospitalsystem.app.repository.search.QuantityPriceSearchRepository;
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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.QuantityPrice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuantityPriceResource {

    private final Logger log = LoggerFactory.getLogger(QuantityPriceResource.class);

    private static final String ENTITY_NAME = "quantityPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuantityPriceRepository quantityPriceRepository;

    private final QuantityPriceSearchRepository quantityPriceSearchRepository;

    public QuantityPriceResource(QuantityPriceRepository quantityPriceRepository, QuantityPriceSearchRepository quantityPriceSearchRepository) {
        this.quantityPriceRepository = quantityPriceRepository;
        this.quantityPriceSearchRepository = quantityPriceSearchRepository;
    }

    /**
     * {@code POST  /quantity-prices} : Create a new quantityPrice.
     *
     * @param quantityPrice the quantityPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quantityPrice, or with status {@code 400 (Bad Request)} if the quantityPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quantity-prices")
    public ResponseEntity<QuantityPrice> createQuantityPrice(@Valid @RequestBody QuantityPrice quantityPrice) throws URISyntaxException {
        log.debug("REST request to save QuantityPrice : {}", quantityPrice);
        if (quantityPrice.getId() != null) {
            throw new BadRequestAlertException("A new quantityPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuantityPrice result = quantityPriceRepository.save(quantityPrice);
        quantityPriceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/quantity-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quantity-prices} : Updates an existing quantityPrice.
     *
     * @param quantityPrice the quantityPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quantityPrice,
     * or with status {@code 400 (Bad Request)} if the quantityPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quantityPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quantity-prices")
    public ResponseEntity<QuantityPrice> updateQuantityPrice(@Valid @RequestBody QuantityPrice quantityPrice) throws URISyntaxException {
        log.debug("REST request to update QuantityPrice : {}", quantityPrice);
        if (quantityPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuantityPrice result = quantityPriceRepository.save(quantityPrice);
        quantityPriceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quantityPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quantity-prices} : get all the quantityPrices.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quantityPrices in body.
     */
    @GetMapping("/quantity-prices")
    public ResponseEntity<List<QuantityPrice>> getAllQuantityPrices(Pageable pageable) {
        log.debug("REST request to get a page of QuantityPrices");
        Page<QuantityPrice> page = quantityPriceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quantity-prices/:id} : get the "id" quantityPrice.
     *
     * @param id the id of the quantityPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quantityPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quantity-prices/{id}")
    public ResponseEntity<QuantityPrice> getQuantityPrice(@PathVariable Long id) {
        log.debug("REST request to get QuantityPrice : {}", id);
        Optional<QuantityPrice> quantityPrice = quantityPriceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quantityPrice);
    }

    /**
     * {@code DELETE  /quantity-prices/:id} : delete the "id" quantityPrice.
     *
     * @param id the id of the quantityPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quantity-prices/{id}")
    public ResponseEntity<Void> deleteQuantityPrice(@PathVariable Long id) {
        log.debug("REST request to delete QuantityPrice : {}", id);
        quantityPriceRepository.deleteById(id);
        quantityPriceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/quantity-prices?query=:query} : search for the quantityPrice corresponding
     * to the query.
     *
     * @param query the query of the quantityPrice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/quantity-prices")
    public ResponseEntity<List<QuantityPrice>> searchQuantityPrices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of QuantityPrices for query {}", query);
        Page<QuantityPrice> page = quantityPriceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
