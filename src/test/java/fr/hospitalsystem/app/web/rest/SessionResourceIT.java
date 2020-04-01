package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Session;
import fr.hospitalsystem.app.repository.SessionRepository;
import fr.hospitalsystem.app.repository.search.SessionSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static fr.hospitalsystem.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SessionResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class SessionResourceIT {

    private static final Integer DEFAULT_TOTAL_CASH = 1;
    private static final Integer UPDATED_TOTAL_CASH = 2;

    private static final Integer DEFAULT_TOTAL_PC = 1;
    private static final Integer UPDATED_TOTAL_PC = 2;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Integer DEFAULT_TOTAL_CHECK = 1;
    private static final Integer UPDATED_TOTAL_CHECK = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.SessionSearchRepositoryMockConfiguration
     */
    @Autowired
    private SessionSearchRepository mockSessionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSessionMockMvc;

    private Session session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessionResource sessionResource = new SessionResource(sessionRepository, mockSessionSearchRepository);
        this.restSessionMockMvc = MockMvcBuilders.standaloneSetup(sessionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createEntity(EntityManager em) {
        Session session = new Session()
            .totalCash(DEFAULT_TOTAL_CASH)
            .totalPC(DEFAULT_TOTAL_PC)
            .total(DEFAULT_TOTAL)
            .totalCheck(DEFAULT_TOTAL_CHECK)
            .created_by(DEFAULT_CREATED_BY)
            .created_date(DEFAULT_CREATED_DATE);
        return session;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createUpdatedEntity(EntityManager em) {
        Session session = new Session()
            .totalCash(UPDATED_TOTAL_CASH)
            .totalPC(UPDATED_TOTAL_PC)
            .total(UPDATED_TOTAL)
            .totalCheck(UPDATED_TOTAL_CHECK)
            .created_by(UPDATED_CREATED_BY)
            .created_date(UPDATED_CREATED_DATE);
        return session;
    }

    @BeforeEach
    public void initTest() {
        session = createEntity(em);
    }

    @Test
    @Transactional
    public void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getTotalCash()).isEqualTo(DEFAULT_TOTAL_CASH);
        assertThat(testSession.getTotalPC()).isEqualTo(DEFAULT_TOTAL_PC);
        assertThat(testSession.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testSession.getTotalCheck()).isEqualTo(DEFAULT_TOTAL_CHECK);
        assertThat(testSession.getCreated_by()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSession.getCreated_date()).isEqualTo(DEFAULT_CREATED_DATE);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).save(testSession);
    }

    @Test
    @Transactional
    public void createSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session with an existing ID
        session.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(0)).save(session);
    }


    @Test
    @Transactional
    public void checkTotalCashIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setTotalCash(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPCIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setTotalPC(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setTotal(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalCheckIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setTotalCheck(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreated_byIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setCreated_by(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreated_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setCreated_date(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalCash").value(hasItem(DEFAULT_TOTAL_CASH)))
            .andExpect(jsonPath("$.[*].totalPC").value(hasItem(DEFAULT_TOTAL_PC)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].totalCheck").value(hasItem(DEFAULT_TOTAL_CHECK)))
            .andExpect(jsonPath("$.[*].created_by").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.totalCash").value(DEFAULT_TOTAL_CASH))
            .andExpect(jsonPath("$.totalPC").value(DEFAULT_TOTAL_PC))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.totalCheck").value(DEFAULT_TOTAL_CHECK))
            .andExpect(jsonPath("$.created_by").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.created_date").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).get();
        // Disconnect from session so that the updates on updatedSession are not directly saved in db
        em.detach(updatedSession);
        updatedSession
            .totalCash(UPDATED_TOTAL_CASH)
            .totalPC(UPDATED_TOTAL_PC)
            .total(UPDATED_TOTAL)
            .totalCheck(UPDATED_TOTAL_CHECK)
            .created_by(UPDATED_CREATED_BY)
            .created_date(UPDATED_CREATED_DATE);

        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSession)))
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getTotalCash()).isEqualTo(UPDATED_TOTAL_CASH);
        assertThat(testSession.getTotalPC()).isEqualTo(UPDATED_TOTAL_PC);
        assertThat(testSession.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testSession.getTotalCheck()).isEqualTo(UPDATED_TOTAL_CHECK);
        assertThat(testSession.getCreated_by()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSession.getCreated_date()).isEqualTo(UPDATED_CREATED_DATE);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).save(testSession);
    }

    @Test
    @Transactional
    public void updateNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Create the Session

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(0)).save(session);
    }

    @Test
    @Transactional
    public void deleteSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Delete the session
        restSessionMockMvc.perform(delete("/api/sessions/{id}", session.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).deleteById(session.getId());
    }

    @Test
    @Transactional
    public void searchSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);
        when(mockSessionSearchRepository.search(queryStringQuery("id:" + session.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(session), PageRequest.of(0, 1), 1));
        // Search the session
        restSessionMockMvc.perform(get("/api/_search/sessions?query=id:" + session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalCash").value(hasItem(DEFAULT_TOTAL_CASH)))
            .andExpect(jsonPath("$.[*].totalPC").value(hasItem(DEFAULT_TOTAL_PC)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].totalCheck").value(hasItem(DEFAULT_TOTAL_CHECK)))
            .andExpect(jsonPath("$.[*].created_by").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
}
