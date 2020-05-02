package fr.hospitalsystem.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import fr.hospitalsystem.app.domain.Guard;
import fr.hospitalsystem.app.repository.GuardRepository;
import fr.hospitalsystem.app.repository.search.GuardSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Guard}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GuardResource {

    private final Logger log = LoggerFactory.getLogger(GuardResource.class);

    private static final String ENTITY_NAME = "guard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuardRepository guardRepository;

    private final GuardSearchRepository guardSearchRepository;

    public GuardResource(GuardRepository guardRepository, GuardSearchRepository guardSearchRepository) {
        this.guardRepository = guardRepository;
        this.guardSearchRepository = guardSearchRepository;
    }

    /**
     * {@code POST  /guards} : Create a new guard.
     *
     * @param guard the guard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guard, or with status {@code 400 (Bad Request)} if
     *         the guard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guards")
    public ResponseEntity<Guard> createGuard(@Valid @RequestBody Guard guard) throws URISyntaxException {
        log.debug("REST request to save Guard : {}", guard);
        if (guard.getId() != null) {
            throw new BadRequestAlertException("A new guard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Guard result = guardRepository.save(guard);
        guardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/guards/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /guards} : Updates an existing guard.
     *
     * @param guard the guard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guard, or with status {@code 400 (Bad Request)} if
     *         the guard is not valid, or with status {@code 500 (Internal Server Error)} if the guard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guards")
    public ResponseEntity<Guard> updateGuard(@Valid @RequestBody Guard guard) throws URISyntaxException {
        log.debug("REST request to update Guard : {}", guard);
        if (guard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Guard result = guardRepository.save(guard);
        guardSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guard.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /guards} : get all the guards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Guards in body.
     */
    @GetMapping("/guards")
    public ResponseEntity<List<Guard>> getAllGuards(Pageable pageable) {
        log.debug("REST request to get a page of Patients");
        Page<Guard> page = guardRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guards/:id} : get the "id" guard.
     *
     * @param id the id of the guard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guards/{id}")
    public ResponseEntity<Guard> getGuard(@PathVariable Long id) {
        log.debug("REST request to get Guard : {}", id);
        Optional<Guard> guard = guardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(guard);
    }

    /**
     * {@code DELETE  /guards/:id} : delete the "id" guard.
     *
     * @param id the id of the guard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guards/{id}")
    public ResponseEntity<Void> deleteGuard(@PathVariable Long id) {
        log.debug("REST request to delete Guard : {}", id);
        guardRepository.deleteById(id);
        guardSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/guards?query=:query} : search for the guard corresponding to the query.
     *
     * @param query the query of the guard search.
     * @return the result of the search.
     */
    @GetMapping("/_search/guards")
    public ResponseEntity<List<Guard>> searchGuards(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Guards for query {}", query);
        Page<Guard> page = guardSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
