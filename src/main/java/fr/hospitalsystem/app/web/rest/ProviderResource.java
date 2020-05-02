package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Provider;
import fr.hospitalsystem.app.repository.ProviderRepository;
import fr.hospitalsystem.app.repository.search.ProviderSearchRepository;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link Provider}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProviderResource {

    private final Logger log = LoggerFactory.getLogger(ProviderResource.class);

    private static final String ENTITY_NAME = "provider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderRepository providerRepository;

    private final ProviderSearchRepository providersearchRepository;

    public ProviderResource(ProviderRepository providerRepository, ProviderSearchRepository providersearchRepository) {
        this.providerRepository = providerRepository;
        this.providersearchRepository = providersearchRepository;
    }

    /**
     * {@code POST  /provider} : Create a new provider.
     *
     * @param provider the provider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provider, or with status {@code 400 (Bad Request)} if the provider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provider")
    public ResponseEntity<Provider> createProvidedr(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to save Provider : {}", provider);
        if (provider.getId() != null) {
            throw new BadRequestAlertException("A new provider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Provider result = providerRepository.save(provider);
        providersearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/provider/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provider} : Updates an existing provider.
     *
     * @param provider the provider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provider,
     * or with status {@code 400 (Bad Request)} if the provider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provider")
    public ResponseEntity<Provider> updateProvidedr(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to update Provider : {}", provider);
        if (provider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Provider result = providerRepository.save(provider);
        providersearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provider.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /provider} : get all the providers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providers in body.
     */
    @GetMapping("/provider")
    public ResponseEntity<List<Provider>> getAllproviders(Pageable pageable) {
        log.debug("REST request to get a page of providers");
        Page<Provider> page = providerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provider/:id} : get the "id" providedr.
     *
     * @param id the id of the providedr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the providedr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provider/{id}")
    public ResponseEntity<Provider> getProvidedr(@PathVariable Long id) {
        log.debug("REST request to get Provider : {}", id);
        Optional<Provider> providedr = providerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(providedr);
    }

    /**
     * {@code DELETE  /provider/:id} : delete the "id" providedr.
     *
     * @param id the id of the providedr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provider/{id}")
    public ResponseEntity<Void> deleteProvidedr(@PathVariable Long id) {
        log.debug("REST request to delete Provider : {}", id);
        providerRepository.deleteById(id);
        providersearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/provider?query=:query} : search for the providedr corresponding
     * to the query.
     *
     * @param query the query of the providedr search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/provider")
    public ResponseEntity<List<Provider>> searchproviders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of providers for query {}", query);
        Page<Provider> page = providersearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
