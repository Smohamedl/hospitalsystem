package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.repository.MedicalServiceRepository;
import fr.hospitalsystem.app.repository.search.MedicalServiceSearchRepository;
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
 * Integration tests for the {@link MedicalServiceResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class MedicalServiceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MedicalServiceRepository medicalServiceRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.MedicalServiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private MedicalServiceSearchRepository mockMedicalServiceSearchRepository;

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

    private MockMvc restMedicalServiceMockMvc;

    private MedicalService medicalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicalServiceResource medicalServiceResource = new MedicalServiceResource(medicalServiceRepository, mockMedicalServiceSearchRepository);
        this.restMedicalServiceMockMvc = MockMvcBuilders.standaloneSetup(medicalServiceResource)
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
    public static MedicalService createEntity(EntityManager em) {
        MedicalService medicalService = new MedicalService()
            .name(DEFAULT_NAME);
        return medicalService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalService createUpdatedEntity(EntityManager em) {
        MedicalService medicalService = new MedicalService()
            .name(UPDATED_NAME);
        return medicalService;
    }

    @BeforeEach
    public void initTest() {
        medicalService = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalService() throws Exception {
        int databaseSizeBeforeCreate = medicalServiceRepository.findAll().size();

        // Create the MedicalService
        restMedicalServiceMockMvc.perform(post("/api/medical-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalService)))
            .andExpect(status().isCreated());

        // Validate the MedicalService in the database
        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalService testMedicalService = medicalServiceList.get(medicalServiceList.size() - 1);
        assertThat(testMedicalService.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the MedicalService in Elasticsearch
        verify(mockMedicalServiceSearchRepository, times(1)).save(testMedicalService);
    }

    @Test
    @Transactional
    public void createMedicalServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalServiceRepository.findAll().size();

        // Create the MedicalService with an existing ID
        medicalService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalServiceMockMvc.perform(post("/api/medical-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalService)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalService in the database
        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the MedicalService in Elasticsearch
        verify(mockMedicalServiceSearchRepository, times(0)).save(medicalService);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicalServiceRepository.findAll().size();
        // set the field null
        medicalService.setName(null);

        // Create the MedicalService, which fails.

        restMedicalServiceMockMvc.perform(post("/api/medical-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalService)))
            .andExpect(status().isBadRequest());

        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicalServices() throws Exception {
        // Initialize the database
        medicalServiceRepository.saveAndFlush(medicalService);

        // Get all the medicalServiceList
        restMedicalServiceMockMvc.perform(get("/api/medical-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getMedicalService() throws Exception {
        // Initialize the database
        medicalServiceRepository.saveAndFlush(medicalService);

        // Get the medicalService
        restMedicalServiceMockMvc.perform(get("/api/medical-services/{id}", medicalService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalService() throws Exception {
        // Get the medicalService
        restMedicalServiceMockMvc.perform(get("/api/medical-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalService() throws Exception {
        // Initialize the database
        medicalServiceRepository.saveAndFlush(medicalService);

        int databaseSizeBeforeUpdate = medicalServiceRepository.findAll().size();

        // Update the medicalService
        MedicalService updatedMedicalService = medicalServiceRepository.findById(medicalService.getId()).get();
        // Disconnect from session so that the updates on updatedMedicalService are not directly saved in db
        em.detach(updatedMedicalService);
        updatedMedicalService
            .name(UPDATED_NAME);

        restMedicalServiceMockMvc.perform(put("/api/medical-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalService)))
            .andExpect(status().isOk());

        // Validate the MedicalService in the database
        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeUpdate);
        MedicalService testMedicalService = medicalServiceList.get(medicalServiceList.size() - 1);
        assertThat(testMedicalService.getName()).isEqualTo(UPDATED_NAME);

        // Validate the MedicalService in Elasticsearch
        verify(mockMedicalServiceSearchRepository, times(1)).save(testMedicalService);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalService() throws Exception {
        int databaseSizeBeforeUpdate = medicalServiceRepository.findAll().size();

        // Create the MedicalService

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalServiceMockMvc.perform(put("/api/medical-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalService)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalService in the database
        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MedicalService in Elasticsearch
        verify(mockMedicalServiceSearchRepository, times(0)).save(medicalService);
    }

    @Test
    @Transactional
    public void deleteMedicalService() throws Exception {
        // Initialize the database
        medicalServiceRepository.saveAndFlush(medicalService);

        int databaseSizeBeforeDelete = medicalServiceRepository.findAll().size();

        // Delete the medicalService
        restMedicalServiceMockMvc.perform(delete("/api/medical-services/{id}", medicalService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MedicalService> medicalServiceList = medicalServiceRepository.findAll();
        assertThat(medicalServiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MedicalService in Elasticsearch
        verify(mockMedicalServiceSearchRepository, times(1)).deleteById(medicalService.getId());
    }

    @Test
    @Transactional
    public void searchMedicalService() throws Exception {
        // Initialize the database
        medicalServiceRepository.saveAndFlush(medicalService);
        when(mockMedicalServiceSearchRepository.search(queryStringQuery("id:" + medicalService.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(medicalService), PageRequest.of(0, 1), 1));
        // Search the medicalService
        restMedicalServiceMockMvc.perform(get("/api/_search/medical-services?query=id:" + medicalService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
