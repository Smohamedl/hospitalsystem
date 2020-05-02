package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.repository.DoctorRepository;
import fr.hospitalsystem.app.repository.search.DoctorSearchRepository;
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
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class DoctorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SPECIALIST = false;
    private static final Boolean UPDATED_SPECIALIST = true;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.DoctorSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorSearchRepository mockDoctorSearchRepository;

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

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorResource doctorResource = new DoctorResource(doctorRepository, mockDoctorSearchRepository);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource)
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
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .name(DEFAULT_NAME)
            .firstname(DEFAULT_FIRSTNAME)
            .job(DEFAULT_JOB)
            .specialist(DEFAULT_SPECIALIST)
            .address(DEFAULT_ADDRESS)
            .tel(DEFAULT_TEL);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        doctor.setMedicalService(medicalService);
        return doctor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .name(UPDATED_NAME)
            .firstname(UPDATED_FIRSTNAME)
            .job(UPDATED_JOB)
            .specialist(UPDATED_SPECIALIST)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createUpdatedEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        doctor.setMedicalService(medicalService);
        return doctor;
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDoctor.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testDoctor.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testDoctor.isSpecialist()).isEqualTo(DEFAULT_SPECIALIST);
        assertThat(testDoctor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDoctor.getTel()).isEqualTo(DEFAULT_TEL);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).save(testDoctor);
    }

    @Test
    @Transactional
    public void createDoctorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor with an existing ID
        doctor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(0)).save(doctor);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setName(null);

        // Create the Doctor, which fails.

        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setFirstname(null);

        // Create the Doctor, which fails.

        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setJob(null);

        // Create the Doctor, which fails.

        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].specialist").value(hasItem(DEFAULT_SPECIALIST.booleanValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }
    
    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.specialist").value(DEFAULT_SPECIALIST.booleanValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .name(UPDATED_NAME)
            .firstname(UPDATED_FIRSTNAME)
            .job(UPDATED_JOB)
            .specialist(UPDATED_SPECIALIST)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL);

        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctor)))
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDoctor.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testDoctor.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testDoctor.isSpecialist()).isEqualTo(UPDATED_SPECIALIST);
        assertThat(testDoctor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDoctor.getTel()).isEqualTo(UPDATED_TEL);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).save(testDoctor);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Create the Doctor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(0)).save(doctor);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Delete the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).deleteById(doctor.getId());
    }

    @Test
    @Transactional
    public void searchDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        when(mockDoctorSearchRepository.search(queryStringQuery("id:" + doctor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctor), PageRequest.of(0, 1), 1));
        // Search the doctor
        restDoctorMockMvc.perform(get("/api/_search/doctors?query=id:" + doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].specialist").value(hasItem(DEFAULT_SPECIALIST.booleanValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }
}
