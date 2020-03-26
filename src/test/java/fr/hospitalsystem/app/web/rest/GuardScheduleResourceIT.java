package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.GuardSchedule;
import fr.hospitalsystem.app.repository.GuardScheduleRepository;
import fr.hospitalsystem.app.repository.search.GuardScheduleSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
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
 * Integration tests for the {@link GuardScheduleResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class GuardScheduleResourceIT {

    private static final Double DEFAULT_PAYEMENT = 1D;
    private static final Double UPDATED_PAYEMENT = 2D;

    private static final Integer DEFAULT_START = 1;
    private static final Integer UPDATED_START = 2;

    private static final Integer DEFAULT_END = 1;
    private static final Integer UPDATED_END = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private GuardScheduleRepository guardScheduleRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.GuardScheduleSearchRepositoryMockConfiguration
     */
    @Autowired
    private GuardScheduleSearchRepository mockGuardScheduleSearchRepository;

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

    private MockMvc restGuardScheduleMockMvc;

    private GuardSchedule guardSchedule;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuardScheduleResource guardScheduleResource = new GuardScheduleResource(guardScheduleRepository, mockGuardScheduleSearchRepository);
        this.restGuardScheduleMockMvc = MockMvcBuilders.standaloneSetup(guardScheduleResource)
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
    public static GuardSchedule createEntity(EntityManager em) {
        GuardSchedule guardSchedule = new GuardSchedule()
            .payement(DEFAULT_PAYEMENT)
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .name(DEFAULT_NAME);
        return guardSchedule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuardSchedule createUpdatedEntity(EntityManager em) {
        GuardSchedule guardSchedule = new GuardSchedule()
            .payement(UPDATED_PAYEMENT)
            .start(UPDATED_START)
            .end(UPDATED_END)
            .name(UPDATED_NAME);
        return guardSchedule;
    }

    @BeforeEach
    public void initTest() {
        guardSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuardSchedule() throws Exception {
        int databaseSizeBeforeCreate = guardScheduleRepository.findAll().size();

        // Create the GuardSchedule
        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isCreated());

        // Validate the GuardSchedule in the database
        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        GuardSchedule testGuardSchedule = guardScheduleList.get(guardScheduleList.size() - 1);
        assertThat(testGuardSchedule.getPayement()).isEqualTo(DEFAULT_PAYEMENT);
        assertThat(testGuardSchedule.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testGuardSchedule.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testGuardSchedule.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the GuardSchedule in Elasticsearch
        verify(mockGuardScheduleSearchRepository, times(1)).save(testGuardSchedule);
    }

    @Test
    @Transactional
    public void createGuardScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guardScheduleRepository.findAll().size();

        // Create the GuardSchedule with an existing ID
        guardSchedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the GuardSchedule in the database
        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeCreate);

        // Validate the GuardSchedule in Elasticsearch
        verify(mockGuardScheduleSearchRepository, times(0)).save(guardSchedule);
    }


    @Test
    @Transactional
    public void checkPayementIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardScheduleRepository.findAll().size();
        // set the field null
        guardSchedule.setPayement(null);

        // Create the GuardSchedule, which fails.

        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardScheduleRepository.findAll().size();
        // set the field null
        guardSchedule.setStart(null);

        // Create the GuardSchedule, which fails.

        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardScheduleRepository.findAll().size();
        // set the field null
        guardSchedule.setEnd(null);

        // Create the GuardSchedule, which fails.

        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardScheduleRepository.findAll().size();
        // set the field null
        guardSchedule.setName(null);

        // Create the GuardSchedule, which fails.

        restGuardScheduleMockMvc.perform(post("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuardSchedules() throws Exception {
        // Initialize the database
        guardScheduleRepository.saveAndFlush(guardSchedule);

        // Get all the guardScheduleList
        restGuardScheduleMockMvc.perform(get("/api/guard-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].payement").value(hasItem(DEFAULT_PAYEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getGuardSchedule() throws Exception {
        // Initialize the database
        guardScheduleRepository.saveAndFlush(guardSchedule);

        // Get the guardSchedule
        restGuardScheduleMockMvc.perform(get("/api/guard-schedules/{id}", guardSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guardSchedule.getId().intValue()))
            .andExpect(jsonPath("$.payement").value(DEFAULT_PAYEMENT.doubleValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START))
            .andExpect(jsonPath("$.end").value(DEFAULT_END))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingGuardSchedule() throws Exception {
        // Get the guardSchedule
        restGuardScheduleMockMvc.perform(get("/api/guard-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuardSchedule() throws Exception {
        // Initialize the database
        guardScheduleRepository.saveAndFlush(guardSchedule);

        int databaseSizeBeforeUpdate = guardScheduleRepository.findAll().size();

        // Update the guardSchedule
        GuardSchedule updatedGuardSchedule = guardScheduleRepository.findById(guardSchedule.getId()).get();
        // Disconnect from session so that the updates on updatedGuardSchedule are not directly saved in db
        em.detach(updatedGuardSchedule);
        updatedGuardSchedule
            .payement(UPDATED_PAYEMENT)
            .start(UPDATED_START)
            .end(UPDATED_END)
            .name(UPDATED_NAME);

        restGuardScheduleMockMvc.perform(put("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuardSchedule)))
            .andExpect(status().isOk());

        // Validate the GuardSchedule in the database
        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeUpdate);
        GuardSchedule testGuardSchedule = guardScheduleList.get(guardScheduleList.size() - 1);
        assertThat(testGuardSchedule.getPayement()).isEqualTo(UPDATED_PAYEMENT);
        assertThat(testGuardSchedule.getStart()).isEqualTo(UPDATED_START);
        assertThat(testGuardSchedule.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testGuardSchedule.getName()).isEqualTo(UPDATED_NAME);

        // Validate the GuardSchedule in Elasticsearch
        verify(mockGuardScheduleSearchRepository, times(1)).save(testGuardSchedule);
    }

    @Test
    @Transactional
    public void updateNonExistingGuardSchedule() throws Exception {
        int databaseSizeBeforeUpdate = guardScheduleRepository.findAll().size();

        // Create the GuardSchedule

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardScheduleMockMvc.perform(put("/api/guard-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guardSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the GuardSchedule in the database
        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GuardSchedule in Elasticsearch
        verify(mockGuardScheduleSearchRepository, times(0)).save(guardSchedule);
    }

    @Test
    @Transactional
    public void deleteGuardSchedule() throws Exception {
        // Initialize the database
        guardScheduleRepository.saveAndFlush(guardSchedule);

        int databaseSizeBeforeDelete = guardScheduleRepository.findAll().size();

        // Delete the guardSchedule
        restGuardScheduleMockMvc.perform(delete("/api/guard-schedules/{id}", guardSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuardSchedule> guardScheduleList = guardScheduleRepository.findAll();
        assertThat(guardScheduleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GuardSchedule in Elasticsearch
        verify(mockGuardScheduleSearchRepository, times(1)).deleteById(guardSchedule.getId());
    }

    @Test
    @Transactional
    public void searchGuardSchedule() throws Exception {
        // Initialize the database
        guardScheduleRepository.saveAndFlush(guardSchedule);
        when(mockGuardScheduleSearchRepository.search(queryStringQuery("id:" + guardSchedule.getId())))
            .thenReturn(Collections.singletonList(guardSchedule));
        // Search the guardSchedule
        restGuardScheduleMockMvc.perform(get("/api/_search/guard-schedules?query=id:" + guardSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].payement").value(hasItem(DEFAULT_PAYEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
