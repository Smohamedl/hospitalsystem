package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.repository.ActypeRepository;
import fr.hospitalsystem.app.repository.search.ActypeSearchRepository;
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
 * Integration tests for the {@link ActypeResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class ActypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ActypeRepository actypeRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.ActypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ActypeSearchRepository mockActypeSearchRepository;

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

    private MockMvc restActypeMockMvc;

    private Actype actype;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActypeResource actypeResource = new ActypeResource(actypeRepository, mockActypeSearchRepository);
        this.restActypeMockMvc = MockMvcBuilders.standaloneSetup(actypeResource)
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
    public static Actype createEntity(EntityManager em) {
        Actype actype = new Actype()
            .name(DEFAULT_NAME);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        actype.setMedicalService(medicalService);
        return actype;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actype createUpdatedEntity(EntityManager em) {
        Actype actype = new Actype()
            .name(UPDATED_NAME);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createUpdatedEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        actype.setMedicalService(medicalService);
        return actype;
    }

    @BeforeEach
    public void initTest() {
        actype = createEntity(em);
    }

    @Test
    @Transactional
    public void createActype() throws Exception {
        int databaseSizeBeforeCreate = actypeRepository.findAll().size();

        // Create the Actype
        restActypeMockMvc.perform(post("/api/actypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actype)))
            .andExpect(status().isCreated());

        // Validate the Actype in the database
        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeCreate + 1);
        Actype testActype = actypeList.get(actypeList.size() - 1);
        assertThat(testActype.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Actype in Elasticsearch
        verify(mockActypeSearchRepository, times(1)).save(testActype);
    }

    @Test
    @Transactional
    public void createActypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actypeRepository.findAll().size();

        // Create the Actype with an existing ID
        actype.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActypeMockMvc.perform(post("/api/actypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actype)))
            .andExpect(status().isBadRequest());

        // Validate the Actype in the database
        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Actype in Elasticsearch
        verify(mockActypeSearchRepository, times(0)).save(actype);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actypeRepository.findAll().size();
        // set the field null
        actype.setName(null);

        // Create the Actype, which fails.

        restActypeMockMvc.perform(post("/api/actypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actype)))
            .andExpect(status().isBadRequest());

        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActypes() throws Exception {
        // Initialize the database
        actypeRepository.saveAndFlush(actype);

        // Get all the actypeList
        restActypeMockMvc.perform(get("/api/actypes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getActype() throws Exception {
        // Initialize the database
        actypeRepository.saveAndFlush(actype);

        // Get the actype
        restActypeMockMvc.perform(get("/api/actypes/{id}", actype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingActype() throws Exception {
        // Get the actype
        restActypeMockMvc.perform(get("/api/actypes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActype() throws Exception {
        // Initialize the database
        actypeRepository.saveAndFlush(actype);

        int databaseSizeBeforeUpdate = actypeRepository.findAll().size();

        // Update the actype
        Actype updatedActype = actypeRepository.findById(actype.getId()).get();
        // Disconnect from session so that the updates on updatedActype are not directly saved in db
        em.detach(updatedActype);
        updatedActype
            .name(UPDATED_NAME);

        restActypeMockMvc.perform(put("/api/actypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActype)))
            .andExpect(status().isOk());

        // Validate the Actype in the database
        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeUpdate);
        Actype testActype = actypeList.get(actypeList.size() - 1);
        assertThat(testActype.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Actype in Elasticsearch
        verify(mockActypeSearchRepository, times(1)).save(testActype);
    }

    @Test
    @Transactional
    public void updateNonExistingActype() throws Exception {
        int databaseSizeBeforeUpdate = actypeRepository.findAll().size();

        // Create the Actype

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActypeMockMvc.perform(put("/api/actypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actype)))
            .andExpect(status().isBadRequest());

        // Validate the Actype in the database
        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Actype in Elasticsearch
        verify(mockActypeSearchRepository, times(0)).save(actype);
    }

    @Test
    @Transactional
    public void deleteActype() throws Exception {
        // Initialize the database
        actypeRepository.saveAndFlush(actype);

        int databaseSizeBeforeDelete = actypeRepository.findAll().size();

        // Delete the actype
        restActypeMockMvc.perform(delete("/api/actypes/{id}", actype.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actype> actypeList = actypeRepository.findAll();
        assertThat(actypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Actype in Elasticsearch
        verify(mockActypeSearchRepository, times(1)).deleteById(actype.getId());
    }

    @Test
    @Transactional
    public void searchActype() throws Exception {
        // Initialize the database
        actypeRepository.saveAndFlush(actype);
        when(mockActypeSearchRepository.search(queryStringQuery("id:" + actype.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(actype), PageRequest.of(0, 1), 1));
        // Search the actype
        restActypeMockMvc.perform(get("/api/_search/actypes?query=id:" + actype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
