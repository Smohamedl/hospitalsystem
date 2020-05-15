package fr.hospitalsystem.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

import fr.hospitalsystem.app.domain.Act;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.ReceiptAct;
import fr.hospitalsystem.app.domain.Session;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.SessionRepository;
import fr.hospitalsystem.app.service.ActService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Act}.
 */
@RestController
@RequestMapping("/api")
public class ActResource {

    private final Logger log = LoggerFactory.getLogger(ActResource.class);

    private static final String ENTITY_NAME = "act";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActService actService;

    private final SessionRepository sessionRepository;

    private final ReceiptActRepository receiptActRepository;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    public ActResource(SessionRepository sessionRepository, ActService actService, ReceiptActRepository receiptActRepository) {
        this.sessionRepository = sessionRepository;
        this.actService = actService;
        this.receiptActRepository = receiptActRepository;
    }

    /**
     * {@code POST  /acts} : Create a new act.
     *
     * @param act the act to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new act, or with status {@code 400 (Bad Request)} if the
     *         act has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acts")
    public ResponseEntity<Act> createAct(@Valid @RequestBody Act act) throws URISyntaxException {
        log.debug("REST request to save Act : {}", act);
        if (act.getId() != null) {
            throw new BadRequestAlertException("A new act cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceiptAct receiptAct = new ReceiptAct();
        receiptAct.setDate(LocalDate.now());
        receiptAct.setPaid(false);
        receiptAct.setPaidDoctor(false);
        double total = 0;
        for (Actype actype : act.getActypes()) {
            total += actype.getPrice();
        }

        if (act.getPatient().getSocialOrganizationDetails() != null) {
            total = total * (1 - (act.getPatient().getSocialOrganizationDetails().getSocialOrganizationRegimen().getPercentage() / 100));
        }
        receiptAct.setTotal(total);

        act.setReceiptAct(receiptAct);
        Act result = actService.save(act);

        HttpSession httpSession = httpSessionFactory.getObject();
        Session curentSession = (Session) httpSession.getAttribute("SessionUser");

        if (act.getPaymentMethod().getName().equalsIgnoreCase("espece")) {
            curentSession.setTotalCash(curentSession.getTotalCash() + receiptAct.getTotal());
        } else if (act.getPaymentMethod().getName().equalsIgnoreCase("cheque")) {
            curentSession.setTotalCheck(curentSession.getTotalCheck() + receiptAct.getTotal());
        }

        curentSession.setTotal(curentSession.getTotalCash() + curentSession.getTotalCheck());
        sessionRepository.save(curentSession);

        return ResponseEntity.created(new URI("/api/acts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /acts} : Updates an existing act.
     *
     * @param act the act to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated act, or with status {@code 400 (Bad Request)} if the
     *         act is not valid, or with status {@code 500 (Internal Server Error)} if the act couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acts")
    public ResponseEntity<Act> updateAct(@Valid @RequestBody Act act) throws URISyntaxException {
        log.debug("REST request to update Act : {}", act);
        if (act.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Act result = actService.save(act);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, act.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /acts} : get all the acts.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acts in body.
     */
    @GetMapping("/acts")
    public ResponseEntity<List<Act>> getAllActs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Acts");
        Page<Act> page;
        if (eagerload) {
            page = actService.findAllWithEagerRelationships(pageable);
        } else {
            page = actService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acts/:id} : get the "id" act.
     *
     * @param id the id of the act to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the act, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acts/{id}")
    public ResponseEntity<Act> getAct(@PathVariable Long id) {
        log.debug("REST request to get Act : {}", id);
        Optional<Act> act = actService.findOne(id);
        return ResponseUtil.wrapOrNotFound(act);
    }

    /**
     * {@code DELETE  /acts/:id} : delete the "id" act.
     *
     * @param id the id of the act to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acts/{id}")
    public ResponseEntity<Void> deleteAct(@PathVariable Long id) {
        log.debug("REST request to delete Act : {}", id);
        actService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/acts?query=:query} : search for the act corresponding to the query.
     *
     * @param query    the query of the act search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/acts")
    public ResponseEntity<List<Act>> searchActs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Acts for query {}", query);
        Page<Act> page = actService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
