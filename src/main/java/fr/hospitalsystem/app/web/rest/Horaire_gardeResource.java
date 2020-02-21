package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Horaire_garde;
import fr.hospitalsystem.app.repository.Horaire_gardeRepository;
import fr.hospitalsystem.app.repository.search.Horaire_gardeSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Horaire_garde}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class Horaire_gardeResource {

    private final Logger log = LoggerFactory.getLogger(Horaire_gardeResource.class);

    private static final String ENTITY_NAME = "horaire_garde";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Horaire_gardeRepository horaire_gardeRepository;

    private final Horaire_gardeSearchRepository horaire_gardeSearchRepository;

    public Horaire_gardeResource(Horaire_gardeRepository horaire_gardeRepository, Horaire_gardeSearchRepository horaire_gardeSearchRepository) {
        this.horaire_gardeRepository = horaire_gardeRepository;
        this.horaire_gardeSearchRepository = horaire_gardeSearchRepository;
    }

    /**
     * {@code POST  /horaire-gardes} : Create a new horaire_garde.
     *
     * @param horaire_garde the horaire_garde to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horaire_garde, or with status {@code 400 (Bad Request)} if the horaire_garde has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horaire-gardes")
    public ResponseEntity<Horaire_garde> createHoraire_garde(@RequestBody Horaire_garde horaire_garde) throws URISyntaxException {
        log.debug("REST request to save Horaire_garde : {}", horaire_garde);
        if (horaire_garde.getId() != null) {
            throw new BadRequestAlertException("A new horaire_garde cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Horaire_garde result = horaire_gardeRepository.save(horaire_garde);
        horaire_gardeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/horaire-gardes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horaire-gardes} : Updates an existing horaire_garde.
     *
     * @param horaire_garde the horaire_garde to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horaire_garde,
     * or with status {@code 400 (Bad Request)} if the horaire_garde is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horaire_garde couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horaire-gardes")
    public ResponseEntity<Horaire_garde> updateHoraire_garde(@RequestBody Horaire_garde horaire_garde) throws URISyntaxException {
        log.debug("REST request to update Horaire_garde : {}", horaire_garde);
        if (horaire_garde.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Horaire_garde result = horaire_gardeRepository.save(horaire_garde);
        horaire_gardeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horaire_garde.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /horaire-gardes} : get all the horaire_gardes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horaire_gardes in body.
     */
    @GetMapping("/horaire-gardes")
    public List<Horaire_garde> getAllHoraire_gardes() {
        log.debug("REST request to get all Horaire_gardes");
        return horaire_gardeRepository.findAll();
    }

    /**
     * {@code GET  /horaire-gardes/:id} : get the "id" horaire_garde.
     *
     * @param id the id of the horaire_garde to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horaire_garde, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horaire-gardes/{id}")
    public ResponseEntity<Horaire_garde> getHoraire_garde(@PathVariable Long id) {
        log.debug("REST request to get Horaire_garde : {}", id);
        Optional<Horaire_garde> horaire_garde = horaire_gardeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(horaire_garde);
    }

    /**
     * {@code DELETE  /horaire-gardes/:id} : delete the "id" horaire_garde.
     *
     * @param id the id of the horaire_garde to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horaire-gardes/{id}")
    public ResponseEntity<Void> deleteHoraire_garde(@PathVariable Long id) {
        log.debug("REST request to delete Horaire_garde : {}", id);
        horaire_gardeRepository.deleteById(id);
        horaire_gardeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/horaire-gardes?query=:query} : search for the horaire_garde corresponding
     * to the query.
     *
     * @param query the query of the horaire_garde search.
     * @return the result of the search.
     */
    @GetMapping("/_search/horaire-gardes")
    public List<Horaire_garde> searchHoraire_gardes(@RequestParam String query) {
        log.debug("REST request to search Horaire_gardes for query {}", query);
        return StreamSupport
            .stream(horaire_gardeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
