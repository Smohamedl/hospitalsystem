package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.SocialOrganization;
import fr.hospitalsystem.app.service.SocialOrganizationService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import fr.hospitalsystem.app.service.dto.SocialOrganizationCriteria;
import fr.hospitalsystem.app.service.SocialOrganizationQueryService;

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
 * REST controller for managing {@link fr.hospitalsystem.app.domain.SocialOrganization}.
 */
@RestController
@RequestMapping("/api")
public class SocialOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationResource.class);

    private static final String ENTITY_NAME = "socialOrganization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialOrganizationService socialOrganizationService;

    private final SocialOrganizationQueryService socialOrganizationQueryService;

    public SocialOrganizationResource(SocialOrganizationService socialOrganizationService, SocialOrganizationQueryService socialOrganizationQueryService) {
        this.socialOrganizationService = socialOrganizationService;
        this.socialOrganizationQueryService = socialOrganizationQueryService;
    }

    /**
     * {@code POST  /social-organizations} : Create a new socialOrganization.
     *
     * @param socialOrganization the socialOrganization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialOrganization, or with status {@code 400 (Bad Request)} if the socialOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-organizations")
    public ResponseEntity<SocialOrganization> createSocialOrganization(@Valid @RequestBody SocialOrganization socialOrganization) throws URISyntaxException {
        log.debug("REST request to save SocialOrganization : {}", socialOrganization);
        if (socialOrganization.getId() != null) {
            throw new BadRequestAlertException("A new socialOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialOrganization result = socialOrganizationService.save(socialOrganization);
        return ResponseEntity.created(new URI("/api/social-organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-organizations} : Updates an existing socialOrganization.
     *
     * @param socialOrganization the socialOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialOrganization,
     * or with status {@code 400 (Bad Request)} if the socialOrganization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-organizations")
    public ResponseEntity<SocialOrganization> updateSocialOrganization(@Valid @RequestBody SocialOrganization socialOrganization) throws URISyntaxException {
        log.debug("REST request to update SocialOrganization : {}", socialOrganization);
        if (socialOrganization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SocialOrganization result = socialOrganizationService.save(socialOrganization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialOrganization.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /social-organizations} : get all the socialOrganizations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialOrganizations in body.
     */
    @GetMapping("/social-organizations")
    public ResponseEntity<List<SocialOrganization>> getAllSocialOrganizations(SocialOrganizationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SocialOrganizations by criteria: {}", criteria);
        Page<SocialOrganization> page = socialOrganizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /social-organizations/count} : count all the socialOrganizations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/social-organizations/count")
    public ResponseEntity<Long> countSocialOrganizations(SocialOrganizationCriteria criteria) {
        log.debug("REST request to count SocialOrganizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(socialOrganizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /social-organizations/:id} : get the "id" socialOrganization.
     *
     * @param id the id of the socialOrganization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialOrganization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-organizations/{id}")
    public ResponseEntity<SocialOrganization> getSocialOrganization(@PathVariable Long id) {
        log.debug("REST request to get SocialOrganization : {}", id);
        Optional<SocialOrganization> socialOrganization = socialOrganizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialOrganization);
    }

    /**
     * {@code DELETE  /social-organizations/:id} : delete the "id" socialOrganization.
     *
     * @param id the id of the socialOrganization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-organizations/{id}")
    public ResponseEntity<Void> deleteSocialOrganization(@PathVariable Long id) {
        log.debug("REST request to delete SocialOrganization : {}", id);
        socialOrganizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/social-organizations?query=:query} : search for the socialOrganization corresponding
     * to the query.
     *
     * @param query the query of the socialOrganization search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/social-organizations")
    public ResponseEntity<List<SocialOrganization>> searchSocialOrganizations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SocialOrganizations for query {}", query);
        Page<SocialOrganization> page = socialOrganizationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
