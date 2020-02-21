package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Horaire_garde;
import fr.hospitalsystem.app.repository.Horaire_gardeRepository;
import fr.hospitalsystem.app.repository.search.Horaire_gardeSearchRepository;
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
 * Integration tests for the {@link Horaire_gardeResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class Horaire_gardeResourceIT {

    private static final Integer DEFAULT_START = 1;
    private static final Integer UPDATED_START = 2;

    private static final Integer DEFAULT_END = 1;
    private static final Integer UPDATED_END = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private Horaire_gardeRepository horaire_gardeRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.Horaire_gardeSearchRepositoryMockConfiguration
     */
    @Autowired
    private Horaire_gardeSearchRepository mockHoraire_gardeSearchRepository;

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

    private MockMvc restHoraire_gardeMockMvc;

    private Horaire_garde horaire_garde;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Horaire_gardeResource horaire_gardeResource = new Horaire_gardeResource(horaire_gardeRepository, mockHoraire_gardeSearchRepository);
        this.restHoraire_gardeMockMvc = MockMvcBuilders.standaloneSetup(horaire_gardeResource)
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
    public static Horaire_garde createEntity(EntityManager em) {
        Horaire_garde horaire_garde = new Horaire_garde()
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .name(DEFAULT_NAME);
        return horaire_garde;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horaire_garde createUpdatedEntity(EntityManager em) {
        Horaire_garde horaire_garde = new Horaire_garde()
            .start(UPDATED_START)
            .end(UPDATED_END)
            .name(UPDATED_NAME);
        return horaire_garde;
    }

    @BeforeEach
    public void initTest() {
        horaire_garde = createEntity(em);
    }

    @Test
    @Transactional
    public void createHoraire_garde() throws Exception {
        int databaseSizeBeforeCreate = horaire_gardeRepository.findAll().size();

        // Create the Horaire_garde
        restHoraire_gardeMockMvc.perform(post("/api/horaire-gardes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horaire_garde)))
            .andExpect(status().isCreated());

        // Validate the Horaire_garde in the database
        List<Horaire_garde> horaire_gardeList = horaire_gardeRepository.findAll();
        assertThat(horaire_gardeList).hasSize(databaseSizeBeforeCreate + 1);
        Horaire_garde testHoraire_garde = horaire_gardeList.get(horaire_gardeList.size() - 1);
        assertThat(testHoraire_garde.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testHoraire_garde.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testHoraire_garde.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Horaire_garde in Elasticsearch
        verify(mockHoraire_gardeSearchRepository, times(1)).save(testHoraire_garde);
    }

    @Test
    @Transactional
    public void createHoraire_gardeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = horaire_gardeRepository.findAll().size();

        // Create the Horaire_garde with an existing ID
        horaire_garde.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoraire_gardeMockMvc.perform(post("/api/horaire-gardes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horaire_garde)))
            .andExpect(status().isBadRequest());

        // Validate the Horaire_garde in the database
        List<Horaire_garde> horaire_gardeList = horaire_gardeRepository.findAll();
        assertThat(horaire_gardeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Horaire_garde in Elasticsearch
        verify(mockHoraire_gardeSearchRepository, times(0)).save(horaire_garde);
    }


    @Test
    @Transactional
    public void getAllHoraire_gardes() throws Exception {
        // Initialize the database
        horaire_gardeRepository.saveAndFlush(horaire_garde);

        // Get all the horaire_gardeList
        restHoraire_gardeMockMvc.perform(get("/api/horaire-gardes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horaire_garde.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getHoraire_garde() throws Exception {
        // Initialize the database
        horaire_gardeRepository.saveAndFlush(horaire_garde);

        // Get the horaire_garde
        restHoraire_gardeMockMvc.perform(get("/api/horaire-gardes/{id}", horaire_garde.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(horaire_garde.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START))
            .andExpect(jsonPath("$.end").value(DEFAULT_END))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingHoraire_garde() throws Exception {
        // Get the horaire_garde
        restHoraire_gardeMockMvc.perform(get("/api/horaire-gardes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHoraire_garde() throws Exception {
        // Initialize the database
        horaire_gardeRepository.saveAndFlush(horaire_garde);

        int databaseSizeBeforeUpdate = horaire_gardeRepository.findAll().size();

        // Update the horaire_garde
        Horaire_garde updatedHoraire_garde = horaire_gardeRepository.findById(horaire_garde.getId()).get();
        // Disconnect from session so that the updates on updatedHoraire_garde are not directly saved in db
        em.detach(updatedHoraire_garde);
        updatedHoraire_garde
            .start(UPDATED_START)
            .end(UPDATED_END)
            .name(UPDATED_NAME);

        restHoraire_gardeMockMvc.perform(put("/api/horaire-gardes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHoraire_garde)))
            .andExpect(status().isOk());

        // Validate the Horaire_garde in the database
        List<Horaire_garde> horaire_gardeList = horaire_gardeRepository.findAll();
        assertThat(horaire_gardeList).hasSize(databaseSizeBeforeUpdate);
        Horaire_garde testHoraire_garde = horaire_gardeList.get(horaire_gardeList.size() - 1);
        assertThat(testHoraire_garde.getStart()).isEqualTo(UPDATED_START);
        assertThat(testHoraire_garde.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testHoraire_garde.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Horaire_garde in Elasticsearch
        verify(mockHoraire_gardeSearchRepository, times(1)).save(testHoraire_garde);
    }

    @Test
    @Transactional
    public void updateNonExistingHoraire_garde() throws Exception {
        int databaseSizeBeforeUpdate = horaire_gardeRepository.findAll().size();

        // Create the Horaire_garde

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraire_gardeMockMvc.perform(put("/api/horaire-gardes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horaire_garde)))
            .andExpect(status().isBadRequest());

        // Validate the Horaire_garde in the database
        List<Horaire_garde> horaire_gardeList = horaire_gardeRepository.findAll();
        assertThat(horaire_gardeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Horaire_garde in Elasticsearch
        verify(mockHoraire_gardeSearchRepository, times(0)).save(horaire_garde);
    }

    @Test
    @Transactional
    public void deleteHoraire_garde() throws Exception {
        // Initialize the database
        horaire_gardeRepository.saveAndFlush(horaire_garde);

        int databaseSizeBeforeDelete = horaire_gardeRepository.findAll().size();

        // Delete the horaire_garde
        restHoraire_gardeMockMvc.perform(delete("/api/horaire-gardes/{id}", horaire_garde.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horaire_garde> horaire_gardeList = horaire_gardeRepository.findAll();
        assertThat(horaire_gardeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Horaire_garde in Elasticsearch
        verify(mockHoraire_gardeSearchRepository, times(1)).deleteById(horaire_garde.getId());
    }

    @Test
    @Transactional
    public void searchHoraire_garde() throws Exception {
        // Initialize the database
        horaire_gardeRepository.saveAndFlush(horaire_garde);
        when(mockHoraire_gardeSearchRepository.search(queryStringQuery("id:" + horaire_garde.getId())))
            .thenReturn(Collections.singletonList(horaire_garde));
        // Search the horaire_garde
        restHoraire_gardeMockMvc.perform(get("/api/_search/horaire-gardes?query=id:" + horaire_garde.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horaire_garde.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
