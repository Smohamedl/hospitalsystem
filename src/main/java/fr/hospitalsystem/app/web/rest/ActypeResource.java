package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.repository.ActypeRepository;
import fr.hospitalsystem.app.repository.search.ActypeSearchRepository;
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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Actype}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ActypeResource {

    private final Logger log = LoggerFactory.getLogger(ActypeResource.class);

    private static final String ENTITY_NAME = "actype";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActypeRepository actypeRepository;

    private final ActypeSearchRepository actypeSearchRepository;

    public ActypeResource(ActypeRepository actypeRepository, ActypeSearchRepository actypeSearchRepository) {
        this.actypeRepository = actypeRepository;
        this.actypeSearchRepository = actypeSearchRepository;
    }

    /**
     * {@code POST  /actypes} : Create a new actype.
     *
     * @param actype the actype to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actype, or with status {@code 400 (Bad Request)} if the actype has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actypes")
    public ResponseEntity<Actype> createActype(@Valid @RequestBody Actype actype) throws URISyntaxException {
        log.debug("REST request to save Actype : {}", actype);
        if (actype.getId() != null) {
            throw new BadRequestAlertException("A new actype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Actype result = actypeRepository.save(actype);
        actypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/actypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actypes} : Updates an existing actype.
     *
     * @param actype the actype to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actype,
     * or with status {@code 400 (Bad Request)} if the actype is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actype couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actypes")
    public ResponseEntity<Actype> updateActype(@Valid @RequestBody Actype actype) throws URISyntaxException {
        log.debug("REST request to update Actype : {}", actype);
        if (actype.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Actype result = actypeRepository.save(actype);
        actypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actype.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /actypes} : get all the actypes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actypes in body.
     */
    @GetMapping("/actypes")
    public ResponseEntity<List<Actype>> getAllActypes(Pageable pageable) {
        log.debug("REST request to get a page of Actypes");
        Page<Actype> page = actypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /actypes/:id} : get the "id" actype.
     *
     * @param id the id of the actype to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actype, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actypes/{id}")
    public ResponseEntity<Actype> getActype(@PathVariable Long id) {
        log.debug("REST request to get Actype : {}", id);
        Optional<Actype> actype = actypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actype);
    }

    /**
     * {@code DELETE  /actypes/:id} : delete the "id" actype.
     *
     * @param id the id of the actype to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actypes/{id}")
    public ResponseEntity<Void> deleteActype(@PathVariable Long id) {
        log.debug("REST request to delete Actype : {}", id);
        actypeRepository.deleteById(id);
        actypeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/actypes?query=:query} : search for the actype corresponding
     * to the query.
     *
     * @param query the query of the actype search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/actypes")
    public ResponseEntity<List<Actype>> searchActypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Actypes for query {}", query);
        Page<Actype> page = actypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
