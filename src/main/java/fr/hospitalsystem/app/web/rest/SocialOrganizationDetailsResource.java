package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.SocialOrganizationDetails;
import fr.hospitalsystem.app.service.SocialOrganizationDetailsService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.SocialOrganizationDetails}.
 */
@RestController
@RequestMapping("/api")
public class SocialOrganizationDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SocialOrganizationDetailsResource.class);

    private static final String ENTITY_NAME = "socialOrganizationDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialOrganizationDetailsService socialOrganizationDetailsService;

    public SocialOrganizationDetailsResource(SocialOrganizationDetailsService socialOrganizationDetailsService) {
        this.socialOrganizationDetailsService = socialOrganizationDetailsService;
    }

    /**
     * {@code POST  /social-organization-details} : Create a new socialOrganizationDetails.
     *
     * @param socialOrganizationDetails the socialOrganizationDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialOrganizationDetails, or with status {@code 400 (Bad Request)} if the socialOrganizationDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-organization-details")
    public ResponseEntity<SocialOrganizationDetails> createSocialOrganizationDetails(@Valid @RequestBody SocialOrganizationDetails socialOrganizationDetails) throws URISyntaxException {
        log.debug("REST request to save SocialOrganizationDetails : {}", socialOrganizationDetails);
        if (socialOrganizationDetails.getId() != null) {
            throw new BadRequestAlertException("A new socialOrganizationDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialOrganizationDetails result = socialOrganizationDetailsService.save(socialOrganizationDetails);
        return ResponseEntity.created(new URI("/api/social-organization-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-organization-details} : Updates an existing socialOrganizationDetails.
     *
     * @param socialOrganizationDetails the socialOrganizationDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialOrganizationDetails,
     * or with status {@code 400 (Bad Request)} if the socialOrganizationDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialOrganizationDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-organization-details")
    public ResponseEntity<SocialOrganizationDetails> updateSocialOrganizationDetails(@Valid @RequestBody SocialOrganizationDetails socialOrganizationDetails) throws URISyntaxException {
        log.debug("REST request to update SocialOrganizationDetails : {}", socialOrganizationDetails);
        if (socialOrganizationDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SocialOrganizationDetails result = socialOrganizationDetailsService.save(socialOrganizationDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialOrganizationDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /social-organization-details} : get all the socialOrganizationDetails.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialOrganizationDetails in body.
     */
    @GetMapping("/social-organization-details")
    public ResponseEntity<List<SocialOrganizationDetails>> getAllSocialOrganizationDetails(Pageable pageable) {
        log.debug("REST request to get a page of SocialOrganizationDetails");
        Page<SocialOrganizationDetails> page = socialOrganizationDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /social-organization-details/:id} : get the "id" socialOrganizationDetails.
     *
     * @param id the id of the socialOrganizationDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialOrganizationDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-organization-details/{id}")
    public ResponseEntity<SocialOrganizationDetails> getSocialOrganizationDetails(@PathVariable Long id) {
        log.debug("REST request to get SocialOrganizationDetails : {}", id);
        Optional<SocialOrganizationDetails> socialOrganizationDetails = socialOrganizationDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialOrganizationDetails);
    }

    /**
     * {@code DELETE  /social-organization-details/:id} : delete the "id" socialOrganizationDetails.
     *
     * @param id the id of the socialOrganizationDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-organization-details/{id}")
    public ResponseEntity<Void> deleteSocialOrganizationDetails(@PathVariable Long id) {
        log.debug("REST request to delete SocialOrganizationDetails : {}", id);
        socialOrganizationDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/social-organization-details?query=:query} : search for the socialOrganizationDetails corresponding
     * to the query.
     *
     * @param query the query of the socialOrganizationDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/social-organization-details")
    public ResponseEntity<List<SocialOrganizationDetails>> searchSocialOrganizationDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SocialOrganizationDetails for query {}", query);
        Page<SocialOrganizationDetails> page = socialOrganizationDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
