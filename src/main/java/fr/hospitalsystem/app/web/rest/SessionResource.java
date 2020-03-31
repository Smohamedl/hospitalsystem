package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Authority;
import fr.hospitalsystem.app.domain.Session;
import fr.hospitalsystem.app.domain.User;
import fr.hospitalsystem.app.repository.SessionRepository;
import fr.hospitalsystem.app.repository.UserRepository;
import fr.hospitalsystem.app.repository.search.SessionSearchRepository;
import fr.hospitalsystem.app.security.AuthoritiesConstants;
import fr.hospitalsystem.app.security.SecurityUtils;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Session}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "session";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionRepository sessionRepository;

    private final SessionSearchRepository sessionSearchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    public SessionResource(SessionRepository sessionRepository, SessionSearchRepository sessionSearchRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionSearchRepository = sessionSearchRepository;
    }


    /**
     * {@code GET  /sessions} : get all the sessions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessions in body.
     */
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessions(Pageable pageable) {
        log.debug("REST request to get a page of Sessions");
        Page<Session> page;
        boolean isAdmin = false;
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get().toLowerCase());
        for(Authority auth : user.get().getAuthorities()) {
            if (auth.getName().equals(AuthoritiesConstants.ADMIN)) {
                isAdmin = true;
                break;
            }
        }
        if (isAdmin) {
            page = sessionRepository.findAll(pageable);
        } else {
            page = sessionRepository.findAllByCreated_by(SecurityUtils.getCurrentUserLogin().get().toLowerCase(),pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currentsession} : get the current user session
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the session, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currentsession")
    public ResponseEntity<Session> getSession() {
        HttpSession httpSession = httpSessionFactory.getObject();
         Session session = (Session) httpSession.getAttribute("SessionUser");
        log.debug("REST request to get Session : {}", session.getId());
        Optional<Session> currentsession = Optional.of(session);
        return ResponseUtil.wrapOrNotFound(currentsession);
    }

    /**
     * {@code GET  /sessions/:id} : get the "id" session.
     *
     * @param id the id of the session to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the session, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        log.debug("REST request to get Session : {}", id);
        Optional<Session> session = sessionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(session);
    }

    /**
     * {@code SEARCH  /_search/sessions?query=:query} : search for the session corresponding
     * to the query.
     *
     * @param query the query of the session search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sessions")
    public ResponseEntity<List<Session>> searchSessions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sessions for query {}", query);
        Page<Session> page = sessionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
