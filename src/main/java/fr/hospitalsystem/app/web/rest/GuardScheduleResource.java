package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.GuardSchedule;
import fr.hospitalsystem.app.repository.GuardScheduleRepository;
import fr.hospitalsystem.app.repository.search.GuardScheduleSearchRepository;
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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.GuardSchedule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GuardScheduleResource {

    private final Logger log = LoggerFactory.getLogger(GuardScheduleResource.class);

    private static final String ENTITY_NAME = "guardSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuardScheduleRepository guardScheduleRepository;

    private final GuardScheduleSearchRepository guardScheduleSearchRepository;

    public GuardScheduleResource(GuardScheduleRepository guardScheduleRepository, GuardScheduleSearchRepository guardScheduleSearchRepository) {
        this.guardScheduleRepository = guardScheduleRepository;
        this.guardScheduleSearchRepository = guardScheduleSearchRepository;
    }

    /**
     * {@code POST  /guard-schedules} : Create a new guardSchedule.
     *
     * @param guardSchedule the guardSchedule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guardSchedule, or with status {@code 400 (Bad Request)} if the guardSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guard-schedules")
    public ResponseEntity<GuardSchedule> createGuardSchedule(@RequestBody GuardSchedule guardSchedule) throws URISyntaxException {
        log.debug("REST request to save GuardSchedule : {}", guardSchedule);
        if (guardSchedule.getId() != null) {
            throw new BadRequestAlertException("A new guardSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuardSchedule result = guardScheduleRepository.save(guardSchedule);
        guardScheduleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/guard-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guard-schedules} : Updates an existing guardSchedule.
     *
     * @param guardSchedule the guardSchedule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardSchedule,
     * or with status {@code 400 (Bad Request)} if the guardSchedule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guardSchedule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guard-schedules")
    public ResponseEntity<GuardSchedule> updateGuardSchedule(@RequestBody GuardSchedule guardSchedule) throws URISyntaxException {
        log.debug("REST request to update GuardSchedule : {}", guardSchedule);
        if (guardSchedule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuardSchedule result = guardScheduleRepository.save(guardSchedule);
        guardScheduleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guardSchedule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guard-schedules} : get all the guardSchedules.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guardSchedules in body.
     */
    @GetMapping("/guard-schedules")
    public List<GuardSchedule> getAllGuardSchedules() {
        log.debug("REST request to get all GuardSchedules");
        return guardScheduleRepository.findAll();
    }

    /**
     * {@code GET  /guard-schedules/:id} : get the "id" guardSchedule.
     *
     * @param id the id of the guardSchedule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guardSchedule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guard-schedules/{id}")
    public ResponseEntity<GuardSchedule> getGuardSchedule(@PathVariable Long id) {
        log.debug("REST request to get GuardSchedule : {}", id);
        Optional<GuardSchedule> guardSchedule = guardScheduleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(guardSchedule);
    }

    /**
     * {@code DELETE  /guard-schedules/:id} : delete the "id" guardSchedule.
     *
     * @param id the id of the guardSchedule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guard-schedules/{id}")
    public ResponseEntity<Void> deleteGuardSchedule(@PathVariable Long id) {
        log.debug("REST request to delete GuardSchedule : {}", id);
        guardScheduleRepository.deleteById(id);
        guardScheduleSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/guard-schedules?query=:query} : search for the guardSchedule corresponding
     * to the query.
     *
     * @param query the query of the guardSchedule search.
     * @return the result of the search.
     */
    @GetMapping("/_search/guard-schedules")
    public List<GuardSchedule> searchGuardSchedules(@RequestParam String query) {
        log.debug("REST request to search GuardSchedules for query {}", query);
        return StreamSupport
            .stream(guardScheduleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
