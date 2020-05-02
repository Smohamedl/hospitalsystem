package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Act;
import fr.hospitalsystem.app.domain.MedicalService;
import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.PaymentMethod;
import fr.hospitalsystem.app.repository.ActRepository;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.SessionRepository;
import fr.hospitalsystem.app.repository.search.ActSearchRepository;
import fr.hospitalsystem.app.service.ActService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Integration tests for the {@link ActResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class ActResourceIT {

    private static final String DEFAULT_PATIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_NAME = "BBBBBBBBBB";

    @Autowired
    private ActRepository actRepository;

    @Mock
    private ActRepository actRepositoryMock;

    @Mock
    private ActService actServiceMock;

    @Autowired
    private ActService actService;

    @Autowired
    private  SessionRepository sessionRepository;

    @Autowired
    private ReceiptActRepository receiptActRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.ActSearchRepositoryMockConfiguration
     */
    @Autowired
    private ActSearchRepository mockActSearchRepository;

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

    private MockMvc restActMockMvc;

    private Act act;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActResource actResource = new ActResource(sessionRepository, actService, receiptActRepository);
        this.restActMockMvc = MockMvcBuilders.standaloneSetup(actResource)
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
    public static Act createEntity(EntityManager em) {
        Act act = new Act()
            .patientName(DEFAULT_PATIENT_NAME);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        act.setMedicalService(medicalService);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        act.setDoctor(doctor);
        // Add required entity
        Actype actype;
        if (TestUtil.findAll(em, Actype.class).isEmpty()) {
            actype = ActypeResourceIT.createEntity(em);
            em.persist(actype);
            em.flush();
        } else {
            actype = TestUtil.findAll(em, Actype.class).get(0);
        }
        act.getActypes().add(actype);
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            paymentMethod = PaymentMethodResourceIT.createEntity(em);
            em.persist(paymentMethod);
            em.flush();
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).get(0);
        }
        act.setPaymentMethod(paymentMethod);
        return act;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Act createUpdatedEntity(EntityManager em) {
        Act act = new Act()
            .patientName(UPDATED_PATIENT_NAME);
        // Add required entity
        MedicalService medicalService;
        if (TestUtil.findAll(em, MedicalService.class).isEmpty()) {
            medicalService = MedicalServiceResourceIT.createUpdatedEntity(em);
            em.persist(medicalService);
            em.flush();
        } else {
            medicalService = TestUtil.findAll(em, MedicalService.class).get(0);
        }
        act.setMedicalService(medicalService);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createUpdatedEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        act.setDoctor(doctor);
        // Add required entity
        Actype actype;
        if (TestUtil.findAll(em, Actype.class).isEmpty()) {
            actype = ActypeResourceIT.createUpdatedEntity(em);
            em.persist(actype);
            em.flush();
        } else {
            actype = TestUtil.findAll(em, Actype.class).get(0);
        }
        act.getActypes().add(actype);
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            paymentMethod = PaymentMethodResourceIT.createUpdatedEntity(em);
            em.persist(paymentMethod);
            em.flush();
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).get(0);
        }
        act.setPaymentMethod(paymentMethod);
        return act;
    }

    @BeforeEach
    public void initTest() {
        act = createEntity(em);
    }

    @Test
    @Transactional
    public void createAct() throws Exception {
        int databaseSizeBeforeCreate = actRepository.findAll().size();

        // Create the Act
        restActMockMvc.perform(post("/api/acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(act)))
            .andExpect(status().isCreated());

        // Validate the Act in the database
        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeCreate + 1);
        Act testAct = actList.get(actList.size() - 1);
        assertThat(testAct.getPatientName()).isEqualTo(DEFAULT_PATIENT_NAME);

        // Validate the Act in Elasticsearch
        verify(mockActSearchRepository, times(1)).save(testAct);
    }

    @Test
    @Transactional
    public void createActWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actRepository.findAll().size();

        // Create the Act with an existing ID
        act.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActMockMvc.perform(post("/api/acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(act)))
            .andExpect(status().isBadRequest());

        // Validate the Act in the database
        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeCreate);

        // Validate the Act in Elasticsearch
        verify(mockActSearchRepository, times(0)).save(act);
    }


    @Test
    @Transactional
    public void checkPatientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actRepository.findAll().size();
        // set the field null
        act.setPatientName(null);

        // Create the Act, which fails.

        restActMockMvc.perform(post("/api/acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(act)))
            .andExpect(status().isBadRequest());

        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActs() throws Exception {
        // Initialize the database
        actRepository.saveAndFlush(act);

        // Get all the actList
        restActMockMvc.perform(get("/api/acts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(act.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientName").value(hasItem(DEFAULT_PATIENT_NAME)));
    }
    @SuppressWarnings({"unchecked"})
    public void getAllActsWithEagerRelationshipsIsEnabled() throws Exception {
        ActResource actResource = new ActResource(sessionRepository, actService, receiptActRepository);
        when(actServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restActMockMvc = MockMvcBuilders.standaloneSetup(actResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restActMockMvc.perform(get("/api/acts?eagerload=true"))
        .andExpect(status().isOk());

        verify(actServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllActsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ActResource actResource = new ActResource(sessionRepository, actService, receiptActRepository);
            when(actServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restActMockMvc = MockMvcBuilders.standaloneSetup(actResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restActMockMvc.perform(get("/api/acts?eagerload=true"))
        .andExpect(status().isOk());

            verify(actServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAct() throws Exception {
        // Initialize the database
        actRepository.saveAndFlush(act);

        // Get the act
        restActMockMvc.perform(get("/api/acts/{id}", act.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(act.getId().intValue()))
            .andExpect(jsonPath("$.patientName").value(DEFAULT_PATIENT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingAct() throws Exception {
        // Get the act
        restActMockMvc.perform(get("/api/acts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAct() throws Exception {
        // Initialize the database
        actService.save(act);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockActSearchRepository);

        int databaseSizeBeforeUpdate = actRepository.findAll().size();

        // Update the act
        Act updatedAct = actRepository.findById(act.getId()).get();
        // Disconnect from session so that the updates on updatedAct are not directly saved in db
        em.detach(updatedAct);
        updatedAct
            .patientName(UPDATED_PATIENT_NAME);

        restActMockMvc.perform(put("/api/acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAct)))
            .andExpect(status().isOk());

        // Validate the Act in the database
        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeUpdate);
        Act testAct = actList.get(actList.size() - 1);
        assertThat(testAct.getPatientName()).isEqualTo(UPDATED_PATIENT_NAME);

        // Validate the Act in Elasticsearch
        verify(mockActSearchRepository, times(1)).save(testAct);
    }

    @Test
    @Transactional
    public void updateNonExistingAct() throws Exception {
        int databaseSizeBeforeUpdate = actRepository.findAll().size();

        // Create the Act

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActMockMvc.perform(put("/api/acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(act)))
            .andExpect(status().isBadRequest());

        // Validate the Act in the database
        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Act in Elasticsearch
        verify(mockActSearchRepository, times(0)).save(act);
    }

    @Test
    @Transactional
    public void deleteAct() throws Exception {
        // Initialize the database
        actService.save(act);

        int databaseSizeBeforeDelete = actRepository.findAll().size();

        // Delete the act
        restActMockMvc.perform(delete("/api/acts/{id}", act.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Act> actList = actRepository.findAll();
        assertThat(actList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Act in Elasticsearch
        verify(mockActSearchRepository, times(1)).deleteById(act.getId());
    }

    @Test
    @Transactional
    public void searchAct() throws Exception {
        // Initialize the database
        actService.save(act);
        when(mockActSearchRepository.search(queryStringQuery("id:" + act.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(act), PageRequest.of(0, 1), 1));
        // Search the act
        restActMockMvc.perform(get("/api/_search/acts?query=id:" + act.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(act.getId().intValue())))
            .andExpect(jsonPath("$.[*].patientName").value(hasItem(DEFAULT_PATIENT_NAME)));
    }
}
