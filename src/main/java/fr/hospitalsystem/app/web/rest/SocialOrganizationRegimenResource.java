package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import fr.hospitalsystem.app.service.SocialOrganizationRegimenService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import fr.hospitalsystem.app.service.dto.SocialOrganizationRegimenCriteria;
import fr.hospitalsystem.app.service.SocialOrganizationRegimenQueryService;

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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.SocialOrganizationRegimen}.
 */
@RestController
@RequestMapping("/api")
public class SocialOrganizationRegimenResource {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationRegimenResource.class);

    private static final String ENTITY_NAME = "socialOrganizationRegimen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialOrganizationRegimenService socialOrganizationRegimenService;

    private final SocialOrganizationRegimenQueryService socialOrganizationRegimenQueryService;

    public SocialOrganizationRegimenResource(SocialOrganizationRegimenService socialOrganizationRegimenService, SocialOrganizationRegimenQueryService socialOrganizationRegimenQueryService) {
        this.socialOrganizationRegimenService = socialOrganizationRegimenService;
        this.socialOrganizationRegimenQueryService = socialOrganizationRegimenQueryService;
    }

    /**
     * {@code POST  /social-organization-regimen} : Create a new socialOrganizationRegimen.
     *
     * @param socialOrganizationRegimen the socialOrganizationRegimen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialOrganizationRegimen, or with status {@code 400 (Bad Request)} if the socialOrganizationRegimen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-organization-regimen")
    public ResponseEntity<SocialOrganizationRegimen> createSocialOrganizationRegimen(@Valid @RequestBody SocialOrganizationRegimen socialOrganizationRegimen) throws URISyntaxException {
        log.debug("REST request to save SocialOrganizationRegimen : {}", socialOrganizationRegimen);
        if (socialOrganizationRegimen.getId() != null) {
            throw new BadRequestAlertException("A new socialOrganizationRegimen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialOrganizationRegimen result = socialOrganizationRegimenService.save(socialOrganizationRegimen);
        return ResponseEntity.created(new URI("/api/social-organization-regimen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-organization-regimen} : Updates an existing socialOrganizationRegimen.
     *
     * @param socialOrganizationRegimen the socialOrganizationRegimen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialOrganizationRegimen,
     * or with status {@code 400 (Bad Request)} if the socialOrganizationRegimen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialOrganizationRegimen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-organization-regimen")
    public ResponseEntity<SocialOrganizationRegimen> updateSocialOrganizationRegimen(@Valid @RequestBody SocialOrganizationRegimen socialOrganizationRegimen) throws URISyntaxException {
        log.debug("REST request to update SocialOrganizationRegimen : {}", socialOrganizationRegimen);
        if (socialOrganizationRegimen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SocialOrganizationRegimen result = socialOrganizationRegimenService.save(socialOrganizationRegimen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialOrganizationRegimen.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /social-organization-regimen} : get all the socialOrganizationRegimen.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialOrganizationRegimen in body.
     */
    @GetMapping("/social-organization-regimen")
    public ResponseEntity<List<SocialOrganizationRegimen>> getAllSocialOrganizationRegimen(SocialOrganizationRegimenCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SocialOrganizationRegimen by criteria: {}", criteria);
        Page<SocialOrganizationRegimen> page = socialOrganizationRegimenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /social-organization-regimen/count} : count all the socialOrganizationRegimen.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/social-organization-regimen/count")
    public ResponseEntity<Long> countSocialOrganizationRegimen(SocialOrganizationRegimenCriteria criteria) {
        log.debug("REST request to count SocialOrganizationRegimen by criteria: {}", criteria);
        return ResponseEntity.ok().body(socialOrganizationRegimenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /social-organization-regimen/:id} : get the "id" socialOrganizationRegimen.
     *
     * @param id the id of the socialOrganizationRegimen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialOrganizationRegimen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-organization-regimen/{id}")
    public ResponseEntity<SocialOrganizationRegimen> getSocialOrganizationRegimen(@PathVariable Long id) {
        log.debug("REST request to get SocialOrganizationRegimen : {}", id);
        Optional<SocialOrganizationRegimen> socialOrganizationRegimen = socialOrganizationRegimenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialOrganizationRegimen);
    }

    /**
     * {@code DELETE  /social-organization-regimen/:id} : delete the "id" socialOrganizationRegimen.
     *
     * @param id the id of the socialOrganizationRegimen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-organization-regimen/{id}")
    public ResponseEntity<Void> deleteSocialOrganizationRegimen(@PathVariable Long id) {
        log.debug("REST request to delete SocialOrganizationRegimen : {}", id);
        socialOrganizationRegimenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/social-organization-regimen?query=:query} : search for the socialOrganizationRegimen corresponding
     * to the query.
     *
     * @param query the query of the socialOrganizationRegimen search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/social-organization-regimen")
    public ResponseEntity<List<SocialOrganizationRegimen>> searchSocialOrganizationRegimen(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SocialOrganizationRegimen for query {}", query);
        Page<SocialOrganizationRegimen> page = socialOrganizationRegimenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
