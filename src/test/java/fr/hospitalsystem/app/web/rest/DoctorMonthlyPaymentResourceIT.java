package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;
import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.repository.DoctorMonthlyPaymentRepository;
import fr.hospitalsystem.app.repository.search.DoctorMonthlyPaymentSearchRepository;
import fr.hospitalsystem.app.service.DoctorMonthlyPaymentService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;
import fr.hospitalsystem.app.service.dto.DoctorMonthlyPaymentCriteria;
import fr.hospitalsystem.app.service.DoctorMonthlyPaymentQueryService;

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
 * Integration tests for the {@link DoctorMonthlyPaymentResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class DoctorMonthlyPaymentResourceIT {

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private DoctorMonthlyPaymentRepository doctorMonthlyPaymentRepository;

    @Autowired
    private DoctorMonthlyPaymentService doctorMonthlyPaymentService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.DoctorMonthlyPaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorMonthlyPaymentSearchRepository mockDoctorMonthlyPaymentSearchRepository;

    @Autowired
    private DoctorMonthlyPaymentQueryService doctorMonthlyPaymentQueryService;

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

    private MockMvc restDoctorMonthlyPaymentMockMvc;

    private DoctorMonthlyPayment doctorMonthlyPayment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorMonthlyPaymentResource doctorMonthlyPaymentResource = new DoctorMonthlyPaymentResource(doctorMonthlyPaymentService, doctorMonthlyPaymentQueryService);
        this.restDoctorMonthlyPaymentMockMvc = MockMvcBuilders.standaloneSetup(doctorMonthlyPaymentResource)
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
    public static DoctorMonthlyPayment createEntity(EntityManager em) {
        DoctorMonthlyPayment doctorMonthlyPayment = new DoctorMonthlyPayment()
            .paid(DEFAULT_PAID)
            .date(DEFAULT_DATE)
            .reference(DEFAULT_REFERENCE);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        doctorMonthlyPayment.setDoctor(doctor);
        return doctorMonthlyPayment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorMonthlyPayment createUpdatedEntity(EntityManager em) {
        DoctorMonthlyPayment doctorMonthlyPayment = new DoctorMonthlyPayment()
            .paid(UPDATED_PAID)
            .date(UPDATED_DATE)
            .reference(UPDATED_REFERENCE);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createUpdatedEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        doctorMonthlyPayment.setDoctor(doctor);
        return doctorMonthlyPayment;
    }

    @BeforeEach
    public void initTest() {
        doctorMonthlyPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorMonthlyPayment() throws Exception {
        int databaseSizeBeforeCreate = doctorMonthlyPaymentRepository.findAll().size();

        // Create the DoctorMonthlyPayment
        restDoctorMonthlyPaymentMockMvc.perform(post("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorMonthlyPayment)))
            .andExpect(status().isCreated());

        // Validate the DoctorMonthlyPayment in the database
        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorMonthlyPayment testDoctorMonthlyPayment = doctorMonthlyPaymentList.get(doctorMonthlyPaymentList.size() - 1);
        assertThat(testDoctorMonthlyPayment.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testDoctorMonthlyPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDoctorMonthlyPayment.getReference()).isEqualTo(DEFAULT_REFERENCE);

        // Validate the DoctorMonthlyPayment in Elasticsearch
        verify(mockDoctorMonthlyPaymentSearchRepository, times(1)).save(testDoctorMonthlyPayment);
    }

    @Test
    @Transactional
    public void createDoctorMonthlyPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorMonthlyPaymentRepository.findAll().size();

        // Create the DoctorMonthlyPayment with an existing ID
        doctorMonthlyPayment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMonthlyPaymentMockMvc.perform(post("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorMonthlyPayment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorMonthlyPayment in the database
        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorMonthlyPayment in Elasticsearch
        verify(mockDoctorMonthlyPaymentSearchRepository, times(0)).save(doctorMonthlyPayment);
    }


    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorMonthlyPaymentRepository.findAll().size();
        // set the field null
        doctorMonthlyPayment.setPaid(null);

        // Create the DoctorMonthlyPayment, which fails.

        restDoctorMonthlyPaymentMockMvc.perform(post("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorMonthlyPayment)))
            .andExpect(status().isBadRequest());

        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorMonthlyPaymentRepository.findAll().size();
        // set the field null
        doctorMonthlyPayment.setDate(null);

        // Create the DoctorMonthlyPayment, which fails.

        restDoctorMonthlyPaymentMockMvc.perform(post("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorMonthlyPayment)))
            .andExpect(status().isBadRequest());

        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPayments() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorMonthlyPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));
    }
    
    @Test
    @Transactional
    public void getDoctorMonthlyPayment() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get the doctorMonthlyPayment
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments/{id}", doctorMonthlyPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorMonthlyPayment.getId().intValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE));
    }


    @Test
    @Transactional
    public void getDoctorMonthlyPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        Long id = doctorMonthlyPayment.getId();

        defaultDoctorMonthlyPaymentShouldBeFound("id.equals=" + id);
        defaultDoctorMonthlyPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorMonthlyPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorMonthlyPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorMonthlyPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorMonthlyPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where paid equals to DEFAULT_PAID
        defaultDoctorMonthlyPaymentShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the doctorMonthlyPaymentList where paid equals to UPDATED_PAID
        defaultDoctorMonthlyPaymentShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where paid not equals to DEFAULT_PAID
        defaultDoctorMonthlyPaymentShouldNotBeFound("paid.notEquals=" + DEFAULT_PAID);

        // Get all the doctorMonthlyPaymentList where paid not equals to UPDATED_PAID
        defaultDoctorMonthlyPaymentShouldBeFound("paid.notEquals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultDoctorMonthlyPaymentShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the doctorMonthlyPaymentList where paid equals to UPDATED_PAID
        defaultDoctorMonthlyPaymentShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where paid is not null
        defaultDoctorMonthlyPaymentShouldBeFound("paid.specified=true");

        // Get all the doctorMonthlyPaymentList where paid is null
        defaultDoctorMonthlyPaymentShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date equals to DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date equals to UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date not equals to DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date not equals to UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the doctorMonthlyPaymentList where date equals to UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date is not null
        defaultDoctorMonthlyPaymentShouldBeFound("date.specified=true");

        // Get all the doctorMonthlyPaymentList where date is null
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date is greater than or equal to DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date is greater than or equal to UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date is less than or equal to DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date is less than or equal to SMALLER_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date is less than DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date is less than UPDATED_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where date is greater than DEFAULT_DATE
        defaultDoctorMonthlyPaymentShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the doctorMonthlyPaymentList where date is greater than SMALLER_DATE
        defaultDoctorMonthlyPaymentShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference equals to DEFAULT_REFERENCE
        defaultDoctorMonthlyPaymentShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the doctorMonthlyPaymentList where reference equals to UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference not equals to DEFAULT_REFERENCE
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the doctorMonthlyPaymentList where reference not equals to UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the doctorMonthlyPaymentList where reference equals to UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference is not null
        defaultDoctorMonthlyPaymentShouldBeFound("reference.specified=true");

        // Get all the doctorMonthlyPaymentList where reference is null
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference contains DEFAULT_REFERENCE
        defaultDoctorMonthlyPaymentShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the doctorMonthlyPaymentList where reference contains UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);

        // Get all the doctorMonthlyPaymentList where reference does not contain DEFAULT_REFERENCE
        defaultDoctorMonthlyPaymentShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the doctorMonthlyPaymentList where reference does not contain UPDATED_REFERENCE
        defaultDoctorMonthlyPaymentShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }


    @Test
    @Transactional
    public void getAllDoctorMonthlyPaymentsByDoctorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Doctor doctor = doctorMonthlyPayment.getDoctor();
        doctorMonthlyPaymentRepository.saveAndFlush(doctorMonthlyPayment);
        Long doctorId = doctor.getId();

        // Get all the doctorMonthlyPaymentList where doctor equals to doctorId
        defaultDoctorMonthlyPaymentShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the doctorMonthlyPaymentList where doctor equals to doctorId + 1
        defaultDoctorMonthlyPaymentShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorMonthlyPaymentShouldBeFound(String filter) throws Exception {
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorMonthlyPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));

        // Check, that the count call also returns 1
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorMonthlyPaymentShouldNotBeFound(String filter) throws Exception {
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDoctorMonthlyPayment() throws Exception {
        // Get the doctorMonthlyPayment
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/doctor-monthly-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorMonthlyPayment() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentService.save(doctorMonthlyPayment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDoctorMonthlyPaymentSearchRepository);

        int databaseSizeBeforeUpdate = doctorMonthlyPaymentRepository.findAll().size();

        // Update the doctorMonthlyPayment
        DoctorMonthlyPayment updatedDoctorMonthlyPayment = doctorMonthlyPaymentRepository.findById(doctorMonthlyPayment.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorMonthlyPayment are not directly saved in db
        em.detach(updatedDoctorMonthlyPayment);
        updatedDoctorMonthlyPayment
            .paid(UPDATED_PAID)
            .date(UPDATED_DATE)
            .reference(UPDATED_REFERENCE);

        restDoctorMonthlyPaymentMockMvc.perform(put("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorMonthlyPayment)))
            .andExpect(status().isOk());

        // Validate the DoctorMonthlyPayment in the database
        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeUpdate);
        DoctorMonthlyPayment testDoctorMonthlyPayment = doctorMonthlyPaymentList.get(doctorMonthlyPaymentList.size() - 1);
        assertThat(testDoctorMonthlyPayment.isPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testDoctorMonthlyPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDoctorMonthlyPayment.getReference()).isEqualTo(UPDATED_REFERENCE);

        // Validate the DoctorMonthlyPayment in Elasticsearch
        verify(mockDoctorMonthlyPaymentSearchRepository, times(1)).save(testDoctorMonthlyPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorMonthlyPayment() throws Exception {
        int databaseSizeBeforeUpdate = doctorMonthlyPaymentRepository.findAll().size();

        // Create the DoctorMonthlyPayment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMonthlyPaymentMockMvc.perform(put("/api/doctor-monthly-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorMonthlyPayment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorMonthlyPayment in the database
        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorMonthlyPayment in Elasticsearch
        verify(mockDoctorMonthlyPaymentSearchRepository, times(0)).save(doctorMonthlyPayment);
    }

    @Test
    @Transactional
    public void deleteDoctorMonthlyPayment() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentService.save(doctorMonthlyPayment);

        int databaseSizeBeforeDelete = doctorMonthlyPaymentRepository.findAll().size();

        // Delete the doctorMonthlyPayment
        restDoctorMonthlyPaymentMockMvc.perform(delete("/api/doctor-monthly-payments/{id}", doctorMonthlyPayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoctorMonthlyPayment> doctorMonthlyPaymentList = doctorMonthlyPaymentRepository.findAll();
        assertThat(doctorMonthlyPaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorMonthlyPayment in Elasticsearch
        verify(mockDoctorMonthlyPaymentSearchRepository, times(1)).deleteById(doctorMonthlyPayment.getId());
    }

    @Test
    @Transactional
    public void searchDoctorMonthlyPayment() throws Exception {
        // Initialize the database
        doctorMonthlyPaymentService.save(doctorMonthlyPayment);
        when(mockDoctorMonthlyPaymentSearchRepository.search(queryStringQuery("id:" + doctorMonthlyPayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorMonthlyPayment), PageRequest.of(0, 1), 1));
        // Search the doctorMonthlyPayment
        restDoctorMonthlyPaymentMockMvc.perform(get("/api/_search/doctor-monthly-payments?query=id:" + doctorMonthlyPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorMonthlyPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));
    }
}
