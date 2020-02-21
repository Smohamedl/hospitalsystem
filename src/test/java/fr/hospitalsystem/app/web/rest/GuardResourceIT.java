package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Guard;
import fr.hospitalsystem.app.repository.GuardRepository;
import fr.hospitalsystem.app.repository.search.GuardSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link GuardResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class GuardResourceIT {

    private static final Float DEFAULT_PAY = 1F;
    private static final Float UPDATED_PAY = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private GuardRepository guardRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.GuardSearchRepositoryMockConfiguration
     */
    @Autowired
    private GuardSearchRepository mockGuardSearchRepository;

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

    private MockMvc restGuardMockMvc;

    private Guard guard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuardResource guardResource = new GuardResource(guardRepository, mockGuardSearchRepository);
        this.restGuardMockMvc = MockMvcBuilders.standaloneSetup(guardResource)
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
    public static Guard createEntity(EntityManager em) {
        Guard guard = new Guard()
            .pay(DEFAULT_PAY)
            .date(DEFAULT_DATE);
        return guard;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guard createUpdatedEntity(EntityManager em) {
        Guard guard = new Guard()
            .pay(UPDATED_PAY)
            .date(UPDATED_DATE);
        return guard;
    }

    @BeforeEach
    public void initTest() {
        guard = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuard() throws Exception {
        int databaseSizeBeforeCreate = guardRepository.findAll().size();

        // Create the Guard
        restGuardMockMvc.perform(post("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guard)))
            .andExpect(status().isCreated());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeCreate + 1);
        Guard testGuard = guardList.get(guardList.size() - 1);
        assertThat(testGuard.getPay()).isEqualTo(DEFAULT_PAY);
        assertThat(testGuard.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(1)).save(testGuard);
    }

    @Test
    @Transactional
    public void createGuardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guardRepository.findAll().size();

        // Create the Guard with an existing ID
        guard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardMockMvc.perform(post("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guard)))
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeCreate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }


    @Test
    @Transactional
    public void checkPayIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardRepository.findAll().size();
        // set the field null
        guard.setPay(null);

        // Create the Guard, which fails.

        restGuardMockMvc.perform(post("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guard)))
            .andExpect(status().isBadRequest());

        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardRepository.findAll().size();
        // set the field null
        guard.setDate(null);

        // Create the Guard, which fails.

        restGuardMockMvc.perform(post("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guard)))
            .andExpect(status().isBadRequest());

        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuards() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        // Get all the guardList
        restGuardMockMvc.perform(get("/api/guards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guard.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        // Get the guard
        restGuardMockMvc.perform(get("/api/guards/{id}", guard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guard.getId().intValue()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGuard() throws Exception {
        // Get the guard
        restGuardMockMvc.perform(get("/api/guards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeUpdate = guardRepository.findAll().size();

        // Update the guard
        Guard updatedGuard = guardRepository.findById(guard.getId()).get();
        // Disconnect from session so that the updates on updatedGuard are not directly saved in db
        em.detach(updatedGuard);
        updatedGuard
            .pay(UPDATED_PAY)
            .date(UPDATED_DATE);

        restGuardMockMvc.perform(put("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuard)))
            .andExpect(status().isOk());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);
        Guard testGuard = guardList.get(guardList.size() - 1);
        assertThat(testGuard.getPay()).isEqualTo(UPDATED_PAY);
        assertThat(testGuard.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(1)).save(testGuard);
    }

    @Test
    @Transactional
    public void updateNonExistingGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();

        // Create the Guard

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardMockMvc.perform(put("/api/guards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guard)))
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    public void deleteGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeDelete = guardRepository.findAll().size();

        // Delete the guard
        restGuardMockMvc.perform(delete("/api/guards/{id}", guard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(1)).deleteById(guard.getId());
    }

    @Test
    @Transactional
    public void searchGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);
        when(mockGuardSearchRepository.search(queryStringQuery("id:" + guard.getId())))
            .thenReturn(Collections.singletonList(guard));
        // Search the guard
        restGuardMockMvc.perform(get("/api/_search/guards?query=id:" + guard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guard.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
