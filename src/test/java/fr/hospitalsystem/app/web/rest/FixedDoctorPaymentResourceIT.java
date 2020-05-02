package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.repository.FixedDoctorPaymentRepository;
import fr.hospitalsystem.app.repository.search.FixedDoctorPaymentSearchRepository;
import fr.hospitalsystem.app.service.FixedDoctorPaymentService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;
import fr.hospitalsystem.app.service.dto.FixedDoctorPaymentCriteria;
import fr.hospitalsystem.app.service.FixedDoctorPaymentQueryService;

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
 * Integration tests for the {@link FixedDoctorPaymentResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class FixedDoctorPaymentResourceIT {

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private FixedDoctorPaymentRepository fixedDoctorPaymentRepository;

    @Autowired
    private FixedDoctorPaymentService fixedDoctorPaymentService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.FixedDoctorPaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private FixedDoctorPaymentSearchRepository mockFixedDoctorPaymentSearchRepository;

    @Autowired
    private FixedDoctorPaymentQueryService fixedDoctorPaymentQueryService;

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

    private MockMvc restFixedDoctorPaymentMockMvc;

    private FixedDoctorPayment fixedDoctorPayment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FixedDoctorPaymentResource fixedDoctorPaymentResource = new FixedDoctorPaymentResource(fixedDoctorPaymentService, fixedDoctorPaymentQueryService);
        this.restFixedDoctorPaymentMockMvc = MockMvcBuilders.standaloneSetup(fixedDoctorPaymentResource)
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
    public static FixedDoctorPayment createEntity(EntityManager em) {
        FixedDoctorPayment fixedDoctorPayment = new FixedDoctorPayment()
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
        fixedDoctorPayment.setDoctor(doctor);
        return fixedDoctorPayment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedDoctorPayment createUpdatedEntity(EntityManager em) {
        FixedDoctorPayment fixedDoctorPayment = new FixedDoctorPayment()
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
        fixedDoctorPayment.setDoctor(doctor);
        return fixedDoctorPayment;
    }

    @BeforeEach
    public void initTest() {
        fixedDoctorPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixedDoctorPayment() throws Exception {
        int databaseSizeBeforeCreate = fixedDoctorPaymentRepository.findAll().size();

        // Create the FixedDoctorPayment
        restFixedDoctorPaymentMockMvc.perform(post("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedDoctorPayment)))
            .andExpect(status().isCreated());

        // Validate the FixedDoctorPayment in the database
        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        FixedDoctorPayment testFixedDoctorPayment = fixedDoctorPaymentList.get(fixedDoctorPaymentList.size() - 1);
        assertThat(testFixedDoctorPayment.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testFixedDoctorPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFixedDoctorPayment.getReference()).isEqualTo(DEFAULT_REFERENCE);

        // Validate the FixedDoctorPayment in Elasticsearch
        verify(mockFixedDoctorPaymentSearchRepository, times(1)).save(testFixedDoctorPayment);
    }

    @Test
    @Transactional
    public void createFixedDoctorPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fixedDoctorPaymentRepository.findAll().size();

        // Create the FixedDoctorPayment with an existing ID
        fixedDoctorPayment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedDoctorPaymentMockMvc.perform(post("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedDoctorPayment)))
            .andExpect(status().isBadRequest());

        // Validate the FixedDoctorPayment in the database
        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the FixedDoctorPayment in Elasticsearch
        verify(mockFixedDoctorPaymentSearchRepository, times(0)).save(fixedDoctorPayment);
    }


    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixedDoctorPaymentRepository.findAll().size();
        // set the field null
        fixedDoctorPayment.setPaid(null);

        // Create the FixedDoctorPayment, which fails.

        restFixedDoctorPaymentMockMvc.perform(post("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedDoctorPayment)))
            .andExpect(status().isBadRequest());

        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixedDoctorPaymentRepository.findAll().size();
        // set the field null
        fixedDoctorPayment.setDate(null);

        // Create the FixedDoctorPayment, which fails.

        restFixedDoctorPaymentMockMvc.perform(post("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedDoctorPayment)))
            .andExpect(status().isBadRequest());

        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPayments() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedDoctorPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));
    }
    
    @Test
    @Transactional
    public void getFixedDoctorPayment() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get the fixedDoctorPayment
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments/{id}", fixedDoctorPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fixedDoctorPayment.getId().intValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE));
    }


    @Test
    @Transactional
    public void getFixedDoctorPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        Long id = fixedDoctorPayment.getId();

        defaultFixedDoctorPaymentShouldBeFound("id.equals=" + id);
        defaultFixedDoctorPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultFixedDoctorPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedDoctorPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedDoctorPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedDoctorPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where paid equals to DEFAULT_PAID
        defaultFixedDoctorPaymentShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the fixedDoctorPaymentList where paid equals to UPDATED_PAID
        defaultFixedDoctorPaymentShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where paid not equals to DEFAULT_PAID
        defaultFixedDoctorPaymentShouldNotBeFound("paid.notEquals=" + DEFAULT_PAID);

        // Get all the fixedDoctorPaymentList where paid not equals to UPDATED_PAID
        defaultFixedDoctorPaymentShouldBeFound("paid.notEquals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultFixedDoctorPaymentShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the fixedDoctorPaymentList where paid equals to UPDATED_PAID
        defaultFixedDoctorPaymentShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where paid is not null
        defaultFixedDoctorPaymentShouldBeFound("paid.specified=true");

        // Get all the fixedDoctorPaymentList where paid is null
        defaultFixedDoctorPaymentShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date equals to DEFAULT_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date equals to UPDATED_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date not equals to DEFAULT_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date not equals to UPDATED_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the fixedDoctorPaymentList where date equals to UPDATED_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date is not null
        defaultFixedDoctorPaymentShouldBeFound("date.specified=true");

        // Get all the fixedDoctorPaymentList where date is null
        defaultFixedDoctorPaymentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date is greater than or equal to DEFAULT_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date is greater than or equal to UPDATED_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date is less than or equal to DEFAULT_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date is less than or equal to SMALLER_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date is less than DEFAULT_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date is less than UPDATED_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where date is greater than DEFAULT_DATE
        defaultFixedDoctorPaymentShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the fixedDoctorPaymentList where date is greater than SMALLER_DATE
        defaultFixedDoctorPaymentShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference equals to DEFAULT_REFERENCE
        defaultFixedDoctorPaymentShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the fixedDoctorPaymentList where reference equals to UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference not equals to DEFAULT_REFERENCE
        defaultFixedDoctorPaymentShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the fixedDoctorPaymentList where reference not equals to UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the fixedDoctorPaymentList where reference equals to UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference is not null
        defaultFixedDoctorPaymentShouldBeFound("reference.specified=true");

        // Get all the fixedDoctorPaymentList where reference is null
        defaultFixedDoctorPaymentShouldNotBeFound("reference.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference contains DEFAULT_REFERENCE
        defaultFixedDoctorPaymentShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the fixedDoctorPaymentList where reference contains UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);

        // Get all the fixedDoctorPaymentList where reference does not contain DEFAULT_REFERENCE
        defaultFixedDoctorPaymentShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the fixedDoctorPaymentList where reference does not contain UPDATED_REFERENCE
        defaultFixedDoctorPaymentShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }


    @Test
    @Transactional
    public void getAllFixedDoctorPaymentsByDoctorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Doctor doctor = fixedDoctorPayment.getDoctor();
        fixedDoctorPaymentRepository.saveAndFlush(fixedDoctorPayment);
        Long doctorId = doctor.getId();

        // Get all the fixedDoctorPaymentList where doctor equals to doctorId
        defaultFixedDoctorPaymentShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the fixedDoctorPaymentList where doctor equals to doctorId + 1
        defaultFixedDoctorPaymentShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedDoctorPaymentShouldBeFound(String filter) throws Exception {
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedDoctorPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));

        // Check, that the count call also returns 1
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedDoctorPaymentShouldNotBeFound(String filter) throws Exception {
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFixedDoctorPayment() throws Exception {
        // Get the fixedDoctorPayment
        restFixedDoctorPaymentMockMvc.perform(get("/api/fixed-doctor-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixedDoctorPayment() throws Exception {
        // Initialize the database
        fixedDoctorPaymentService.save(fixedDoctorPayment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFixedDoctorPaymentSearchRepository);

        int databaseSizeBeforeUpdate = fixedDoctorPaymentRepository.findAll().size();

        // Update the fixedDoctorPayment
        FixedDoctorPayment updatedFixedDoctorPayment = fixedDoctorPaymentRepository.findById(fixedDoctorPayment.getId()).get();
        // Disconnect from session so that the updates on updatedFixedDoctorPayment are not directly saved in db
        em.detach(updatedFixedDoctorPayment);
        updatedFixedDoctorPayment
            .paid(UPDATED_PAID)
            .date(UPDATED_DATE)
            .reference(UPDATED_REFERENCE);

        restFixedDoctorPaymentMockMvc.perform(put("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFixedDoctorPayment)))
            .andExpect(status().isOk());

        // Validate the FixedDoctorPayment in the database
        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeUpdate);
        FixedDoctorPayment testFixedDoctorPayment = fixedDoctorPaymentList.get(fixedDoctorPaymentList.size() - 1);
        assertThat(testFixedDoctorPayment.isPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testFixedDoctorPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFixedDoctorPayment.getReference()).isEqualTo(UPDATED_REFERENCE);

        // Validate the FixedDoctorPayment in Elasticsearch
        verify(mockFixedDoctorPaymentSearchRepository, times(1)).save(testFixedDoctorPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingFixedDoctorPayment() throws Exception {
        int databaseSizeBeforeUpdate = fixedDoctorPaymentRepository.findAll().size();

        // Create the FixedDoctorPayment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedDoctorPaymentMockMvc.perform(put("/api/fixed-doctor-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedDoctorPayment)))
            .andExpect(status().isBadRequest());

        // Validate the FixedDoctorPayment in the database
        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedDoctorPayment in Elasticsearch
        verify(mockFixedDoctorPaymentSearchRepository, times(0)).save(fixedDoctorPayment);
    }

    @Test
    @Transactional
    public void deleteFixedDoctorPayment() throws Exception {
        // Initialize the database
        fixedDoctorPaymentService.save(fixedDoctorPayment);

        int databaseSizeBeforeDelete = fixedDoctorPaymentRepository.findAll().size();

        // Delete the fixedDoctorPayment
        restFixedDoctorPaymentMockMvc.perform(delete("/api/fixed-doctor-payments/{id}", fixedDoctorPayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedDoctorPayment> fixedDoctorPaymentList = fixedDoctorPaymentRepository.findAll();
        assertThat(fixedDoctorPaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FixedDoctorPayment in Elasticsearch
        verify(mockFixedDoctorPaymentSearchRepository, times(1)).deleteById(fixedDoctorPayment.getId());
    }

    @Test
    @Transactional
    public void searchFixedDoctorPayment() throws Exception {
        // Initialize the database
        fixedDoctorPaymentService.save(fixedDoctorPayment);
        when(mockFixedDoctorPaymentSearchRepository.search(queryStringQuery("id:" + fixedDoctorPayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fixedDoctorPayment), PageRequest.of(0, 1), 1));
        // Search the fixedDoctorPayment
        restFixedDoctorPaymentMockMvc.perform(get("/api/_search/fixed-doctor-payments?query=id:" + fixedDoctorPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedDoctorPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)));
    }
}
