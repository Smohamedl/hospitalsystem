package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Hospitalization;
import fr.hospitalsystem.app.domain.Patient;
import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.repository.HospitalizationRepository;
import fr.hospitalsystem.app.repository.search.HospitalizationSearchRepository;
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
 * Integration tests for the {@link HospitalizationResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class HospitalizationResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private HospitalizationRepository hospitalizationRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.HospitalizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private HospitalizationSearchRepository mockHospitalizationSearchRepository;

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

    private MockMvc restHospitalizationMockMvc;

    private Hospitalization hospitalization;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HospitalizationResource hospitalizationResource = new HospitalizationResource(hospitalizationRepository, mockHospitalizationSearchRepository);
        this.restHospitalizationMockMvc = MockMvcBuilders.standaloneSetup(hospitalizationResource)
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
    public static Hospitalization createEntity(EntityManager em) {
        Hospitalization hospitalization = new Hospitalization()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        hospitalization.setPatient(patient);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        hospitalization.setMedicalService(medicalService);
        return hospitalization;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospitalization createUpdatedEntity(EntityManager em) {
        Hospitalization hospitalization = new Hospitalization()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        hospitalization.setPatient(patient);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createUpdatedEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        hospitalization.setMedicalService(medicalService);
        return hospitalization;
    }

    @BeforeEach
    public void initTest() {
        hospitalization = createEntity(em);
    }

    @Test
    @Transactional
    public void createHospitalization() throws Exception {
        int databaseSizeBeforeCreate = hospitalizationRepository.findAll().size();

        // Create the Hospitalization
        restHospitalizationMockMvc.perform(post("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hospitalization)))
            .andExpect(status().isCreated());

        // Validate the Hospitalization in the database
        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeCreate + 1);
        Hospitalization testHospitalization = hospitalizationList.get(hospitalizationList.size() - 1);
        assertThat(testHospitalization.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHospitalization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Hospitalization in Elasticsearch
        verify(mockHospitalizationSearchRepository, times(1)).save(testHospitalization);
    }

    @Test
    @Transactional
    public void createHospitalizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hospitalizationRepository.findAll().size();

        // Create the Hospitalization with an existing ID
        hospitalization.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalizationMockMvc.perform(post("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hospitalization)))
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hospitalization in Elasticsearch
        verify(mockHospitalizationSearchRepository, times(0)).save(hospitalization);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalizationRepository.findAll().size();
        // set the field null
        hospitalization.setDate(null);

        // Create the Hospitalization, which fails.

        restHospitalizationMockMvc.perform(post("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hospitalization)))
            .andExpect(status().isBadRequest());

        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalizationRepository.findAll().size();
        // set the field null
        hospitalization.setDescription(null);

        // Create the Hospitalization, which fails.

        restHospitalizationMockMvc.perform(post("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hospitalization)))
            .andExpect(status().isBadRequest());

        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHospitalizations() throws Exception {
        // Initialize the database
        hospitalizationRepository.saveAndFlush(hospitalization);

        // Get all the hospitalizationList
        restHospitalizationMockMvc.perform(get("/api/hospitalizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospitalization.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getHospitalization() throws Exception {
        // Initialize the database
        hospitalizationRepository.saveAndFlush(hospitalization);

        // Get the hospitalization
        restHospitalizationMockMvc.perform(get("/api/hospitalizations/{id}", hospitalization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hospitalization.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingHospitalization() throws Exception {
        // Get the hospitalization
        restHospitalizationMockMvc.perform(get("/api/hospitalizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHospitalization() throws Exception {
        // Initialize the database
        hospitalizationRepository.saveAndFlush(hospitalization);

        int databaseSizeBeforeUpdate = hospitalizationRepository.findAll().size();

        // Update the hospitalization
        Hospitalization updatedHospitalization = hospitalizationRepository.findById(hospitalization.getId()).get();
        // Disconnect from session so that the updates on updatedHospitalization are not directly saved in db
        em.detach(updatedHospitalization);
        updatedHospitalization
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);

        restHospitalizationMockMvc.perform(put("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHospitalization)))
            .andExpect(status().isOk());

        // Validate the Hospitalization in the database
        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeUpdate);
        Hospitalization testHospitalization = hospitalizationList.get(hospitalizationList.size() - 1);
        assertThat(testHospitalization.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHospitalization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Hospitalization in Elasticsearch
        verify(mockHospitalizationSearchRepository, times(1)).save(testHospitalization);
    }

    @Test
    @Transactional
    public void updateNonExistingHospitalization() throws Exception {
        int databaseSizeBeforeUpdate = hospitalizationRepository.findAll().size();

        // Create the Hospitalization

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc.perform(put("/api/hospitalizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hospitalization)))
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hospitalization in Elasticsearch
        verify(mockHospitalizationSearchRepository, times(0)).save(hospitalization);
    }

    @Test
    @Transactional
    public void deleteHospitalization() throws Exception {
        // Initialize the database
        hospitalizationRepository.saveAndFlush(hospitalization);

        int databaseSizeBeforeDelete = hospitalizationRepository.findAll().size();

        // Delete the hospitalization
        restHospitalizationMockMvc.perform(delete("/api/hospitalizations/{id}", hospitalization.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospitalization> hospitalizationList = hospitalizationRepository.findAll();
        assertThat(hospitalizationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hospitalization in Elasticsearch
        verify(mockHospitalizationSearchRepository, times(1)).deleteById(hospitalization.getId());
    }

    @Test
    @Transactional
    public void searchHospitalization() throws Exception {
        // Initialize the database
        hospitalizationRepository.saveAndFlush(hospitalization);
        when(mockHospitalizationSearchRepository.search(queryStringQuery("id:" + hospitalization.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(hospitalization), PageRequest.of(0, 1), 1));
        // Search the hospitalization
        restHospitalizationMockMvc.perform(get("/api/_search/hospitalizations?query=id:" + hospitalization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospitalization.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
