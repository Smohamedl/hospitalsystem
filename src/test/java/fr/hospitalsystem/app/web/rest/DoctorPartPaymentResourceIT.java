package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.DoctorPartPayment;
import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.repository.DoctorPartPaymentRepository;
import fr.hospitalsystem.app.repository.search.DoctorPartPaymentSearchRepository;
import fr.hospitalsystem.app.service.DoctorPartPaymentService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;
import fr.hospitalsystem.app.service.dto.DoctorPartPaymentCriteria;
import fr.hospitalsystem.app.service.DoctorPartPaymentQueryService;

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
 * Integration tests for the {@link DoctorPartPaymentResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class DoctorPartPaymentResourceIT {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;
    private static final Double SMALLER_TOTAL = 1D - 1D;

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private DoctorPartPaymentRepository doctorPartPaymentRepository;

    @Autowired
    private DoctorPartPaymentService doctorPartPaymentService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.DoctorPartPaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorPartPaymentSearchRepository mockDoctorPartPaymentSearchRepository;

    @Autowired
    private DoctorPartPaymentQueryService doctorPartPaymentQueryService;

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

    private MockMvc restDoctorPartPaymentMockMvc;

    private DoctorPartPayment doctorPartPayment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorPartPaymentResource doctorPartPaymentResource = new DoctorPartPaymentResource(doctorPartPaymentService, doctorPartPaymentQueryService);
        this.restDoctorPartPaymentMockMvc = MockMvcBuilders.standaloneSetup(doctorPartPaymentResource)
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
    public static DoctorPartPayment createEntity(EntityManager em) {
        DoctorPartPayment doctorPartPayment = new DoctorPartPayment()
            .total(DEFAULT_TOTAL)
            .reference(DEFAULT_REFERENCE)
            .date(DEFAULT_DATE);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        doctorPartPayment.setDoctor(doctor);
        return doctorPartPayment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorPartPayment createUpdatedEntity(EntityManager em) {
        DoctorPartPayment doctorPartPayment = new DoctorPartPayment()
            .total(UPDATED_TOTAL)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createUpdatedEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        doctorPartPayment.setDoctor(doctor);
        return doctorPartPayment;
    }

    @BeforeEach
    public void initTest() {
        doctorPartPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorPartPayment() throws Exception {
        int databaseSizeBeforeCreate = doctorPartPaymentRepository.findAll().size();

        // Create the DoctorPartPayment
        restDoctorPartPaymentMockMvc.perform(post("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPartPayment)))
            .andExpect(status().isCreated());

        // Validate the DoctorPartPayment in the database
        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorPartPayment testDoctorPartPayment = doctorPartPaymentList.get(doctorPartPaymentList.size() - 1);
        assertThat(testDoctorPartPayment.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDoctorPartPayment.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testDoctorPartPayment.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the DoctorPartPayment in Elasticsearch
        verify(mockDoctorPartPaymentSearchRepository, times(1)).save(testDoctorPartPayment);
    }

    @Test
    @Transactional
    public void createDoctorPartPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorPartPaymentRepository.findAll().size();

        // Create the DoctorPartPayment with an existing ID
        doctorPartPayment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorPartPaymentMockMvc.perform(post("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPartPayment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPartPayment in the database
        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorPartPayment in Elasticsearch
        verify(mockDoctorPartPaymentSearchRepository, times(0)).save(doctorPartPayment);
    }


    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorPartPaymentRepository.findAll().size();
        // set the field null
        doctorPartPayment.setTotal(null);

        // Create the DoctorPartPayment, which fails.

        restDoctorPartPaymentMockMvc.perform(post("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPartPayment)))
            .andExpect(status().isBadRequest());

        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorPartPaymentRepository.findAll().size();
        // set the field null
        doctorPartPayment.setDate(null);

        // Create the DoctorPartPayment, which fails.

        restDoctorPartPaymentMockMvc.perform(post("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPartPayment)))
            .andExpect(status().isBadRequest());

        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPayments() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPartPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getDoctorPartPayment() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get the doctorPartPayment
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments/{id}", doctorPartPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorPartPayment.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }


    @Test
    @Transactional
    public void getDoctorPartPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        Long id = doctorPartPayment.getId();

        defaultDoctorPartPaymentShouldBeFound("id.equals=" + id);
        defaultDoctorPartPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorPartPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorPartPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorPartPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorPartPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total equals to DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total equals to UPDATED_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total not equals to DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total not equals to UPDATED_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the doctorPartPaymentList where total equals to UPDATED_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total is not null
        defaultDoctorPartPaymentShouldBeFound("total.specified=true");

        // Get all the doctorPartPaymentList where total is null
        defaultDoctorPartPaymentShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total is greater than or equal to DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total is greater than or equal to UPDATED_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total is less than or equal to DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total is less than or equal to SMALLER_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total is less than DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total is less than UPDATED_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where total is greater than DEFAULT_TOTAL
        defaultDoctorPartPaymentShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the doctorPartPaymentList where total is greater than SMALLER_TOTAL
        defaultDoctorPartPaymentShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }


    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference equals to DEFAULT_REFERENCE
        defaultDoctorPartPaymentShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the doctorPartPaymentList where reference equals to UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference not equals to DEFAULT_REFERENCE
        defaultDoctorPartPaymentShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the doctorPartPaymentList where reference not equals to UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the doctorPartPaymentList where reference equals to UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference is not null
        defaultDoctorPartPaymentShouldBeFound("reference.specified=true");

        // Get all the doctorPartPaymentList where reference is null
        defaultDoctorPartPaymentShouldNotBeFound("reference.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference contains DEFAULT_REFERENCE
        defaultDoctorPartPaymentShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the doctorPartPaymentList where reference contains UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where reference does not contain DEFAULT_REFERENCE
        defaultDoctorPartPaymentShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the doctorPartPaymentList where reference does not contain UPDATED_REFERENCE
        defaultDoctorPartPaymentShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }


    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date equals to DEFAULT_DATE
        defaultDoctorPartPaymentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date equals to UPDATED_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date not equals to DEFAULT_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date not equals to UPDATED_DATE
        defaultDoctorPartPaymentShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDoctorPartPaymentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the doctorPartPaymentList where date equals to UPDATED_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date is not null
        defaultDoctorPartPaymentShouldBeFound("date.specified=true");

        // Get all the doctorPartPaymentList where date is null
        defaultDoctorPartPaymentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date is greater than or equal to DEFAULT_DATE
        defaultDoctorPartPaymentShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date is greater than or equal to UPDATED_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date is less than or equal to DEFAULT_DATE
        defaultDoctorPartPaymentShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date is less than or equal to SMALLER_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date is less than DEFAULT_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date is less than UPDATED_DATE
        defaultDoctorPartPaymentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);

        // Get all the doctorPartPaymentList where date is greater than DEFAULT_DATE
        defaultDoctorPartPaymentShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the doctorPartPaymentList where date is greater than SMALLER_DATE
        defaultDoctorPartPaymentShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllDoctorPartPaymentsByDoctorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Doctor doctor = doctorPartPayment.getDoctor();
        doctorPartPaymentRepository.saveAndFlush(doctorPartPayment);
        Long doctorId = doctor.getId();

        // Get all the doctorPartPaymentList where doctor equals to doctorId
        defaultDoctorPartPaymentShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the doctorPartPaymentList where doctor equals to doctorId + 1
        defaultDoctorPartPaymentShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorPartPaymentShouldBeFound(String filter) throws Exception {
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPartPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorPartPaymentShouldNotBeFound(String filter) throws Exception {
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDoctorPartPayment() throws Exception {
        // Get the doctorPartPayment
        restDoctorPartPaymentMockMvc.perform(get("/api/doctor-part-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorPartPayment() throws Exception {
        // Initialize the database
        doctorPartPaymentService.save(doctorPartPayment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDoctorPartPaymentSearchRepository);

        int databaseSizeBeforeUpdate = doctorPartPaymentRepository.findAll().size();

        // Update the doctorPartPayment
        DoctorPartPayment updatedDoctorPartPayment = doctorPartPaymentRepository.findById(doctorPartPayment.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorPartPayment are not directly saved in db
        em.detach(updatedDoctorPartPayment);
        updatedDoctorPartPayment
            .total(UPDATED_TOTAL)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE);

        restDoctorPartPaymentMockMvc.perform(put("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorPartPayment)))
            .andExpect(status().isOk());

        // Validate the DoctorPartPayment in the database
        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeUpdate);
        DoctorPartPayment testDoctorPartPayment = doctorPartPaymentList.get(doctorPartPaymentList.size() - 1);
        assertThat(testDoctorPartPayment.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDoctorPartPayment.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testDoctorPartPayment.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the DoctorPartPayment in Elasticsearch
        verify(mockDoctorPartPaymentSearchRepository, times(1)).save(testDoctorPartPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorPartPayment() throws Exception {
        int databaseSizeBeforeUpdate = doctorPartPaymentRepository.findAll().size();

        // Create the DoctorPartPayment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorPartPaymentMockMvc.perform(put("/api/doctor-part-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPartPayment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPartPayment in the database
        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorPartPayment in Elasticsearch
        verify(mockDoctorPartPaymentSearchRepository, times(0)).save(doctorPartPayment);
    }

    @Test
    @Transactional
    public void deleteDoctorPartPayment() throws Exception {
        // Initialize the database
        doctorPartPaymentService.save(doctorPartPayment);

        int databaseSizeBeforeDelete = doctorPartPaymentRepository.findAll().size();

        // Delete the doctorPartPayment
        restDoctorPartPaymentMockMvc.perform(delete("/api/doctor-part-payments/{id}", doctorPartPayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoctorPartPayment> doctorPartPaymentList = doctorPartPaymentRepository.findAll();
        assertThat(doctorPartPaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorPartPayment in Elasticsearch
        verify(mockDoctorPartPaymentSearchRepository, times(1)).deleteById(doctorPartPayment.getId());
    }

    @Test
    @Transactional
    public void searchDoctorPartPayment() throws Exception {
        // Initialize the database
        doctorPartPaymentService.save(doctorPartPayment);
        when(mockDoctorPartPaymentSearchRepository.search(queryStringQuery("id:" + doctorPartPayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorPartPayment), PageRequest.of(0, 1), 1));
        // Search the doctorPartPayment
        restDoctorPartPaymentMockMvc.perform(get("/api/_search/doctor-part-payments?query=id:" + doctorPartPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPartPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
