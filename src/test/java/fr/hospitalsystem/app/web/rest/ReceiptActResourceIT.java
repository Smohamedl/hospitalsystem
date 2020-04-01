package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.ReceiptAct;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.search.ReceiptActSearchRepository;
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
 * Integration tests for the {@link ReceiptActResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class ReceiptActResourceIT {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final Boolean DEFAULT_PAID_DOCTOR = false;
    private static final Boolean UPDATED_PAID_DOCTOR = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ReceiptActRepository receiptActRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.ReceiptActSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReceiptActSearchRepository mockReceiptActSearchRepository;

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

    private MockMvc restReceiptActMockMvc;

    private ReceiptAct receiptAct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReceiptActResource receiptActResource = new ReceiptActResource(receiptActRepository, mockReceiptActSearchRepository);
        this.restReceiptActMockMvc = MockMvcBuilders.standaloneSetup(receiptActResource)
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
    public static ReceiptAct createEntity(EntityManager em) {
        ReceiptAct receiptAct = new ReceiptAct()
            .total(DEFAULT_TOTAL)
            .paid(DEFAULT_PAID)
            .paidDoctor(DEFAULT_PAID_DOCTOR)
            .date(DEFAULT_DATE);
        return receiptAct;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceiptAct createUpdatedEntity(EntityManager em) {
        ReceiptAct receiptAct = new ReceiptAct()
            .total(UPDATED_TOTAL)
            .paid(UPDATED_PAID)
            .paidDoctor(UPDATED_PAID_DOCTOR)
            .date(UPDATED_DATE);
        return receiptAct;
    }

    @BeforeEach
    public void initTest() {
        receiptAct = createEntity(em);
    }

    @Test
    @Transactional
    public void createReceiptAct() throws Exception {
        int databaseSizeBeforeCreate = receiptActRepository.findAll().size();

        // Create the ReceiptAct
        restReceiptActMockMvc.perform(post("/api/receipt-acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receiptAct)))
            .andExpect(status().isCreated());

        // Validate the ReceiptAct in the database
        List<ReceiptAct> receiptActList = receiptActRepository.findAll();
        assertThat(receiptActList).hasSize(databaseSizeBeforeCreate + 1);
        ReceiptAct testReceiptAct = receiptActList.get(receiptActList.size() - 1);
        assertThat(testReceiptAct.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testReceiptAct.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testReceiptAct.isPaidDoctor()).isEqualTo(DEFAULT_PAID_DOCTOR);
        assertThat(testReceiptAct.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the ReceiptAct in Elasticsearch
        verify(mockReceiptActSearchRepository, times(1)).save(testReceiptAct);
    }

    @Test
    @Transactional
    public void createReceiptActWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = receiptActRepository.findAll().size();

        // Create the ReceiptAct with an existing ID
        receiptAct.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceiptActMockMvc.perform(post("/api/receipt-acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receiptAct)))
            .andExpect(status().isBadRequest());

        // Validate the ReceiptAct in the database
        List<ReceiptAct> receiptActList = receiptActRepository.findAll();
        assertThat(receiptActList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReceiptAct in Elasticsearch
        verify(mockReceiptActSearchRepository, times(0)).save(receiptAct);
    }


    @Test
    @Transactional
    public void getAllReceiptActs() throws Exception {
        // Initialize the database
        receiptActRepository.saveAndFlush(receiptAct);

        // Get all the receiptActList
        restReceiptActMockMvc.perform(get("/api/receipt-acts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receiptAct.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].paidDoctor").value(hasItem(DEFAULT_PAID_DOCTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getReceiptAct() throws Exception {
        // Initialize the database
        receiptActRepository.saveAndFlush(receiptAct);

        // Get the receiptAct
        restReceiptActMockMvc.perform(get("/api/receipt-acts/{id}", receiptAct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(receiptAct.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.paidDoctor").value(DEFAULT_PAID_DOCTOR.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReceiptAct() throws Exception {
        // Get the receiptAct
        restReceiptActMockMvc.perform(get("/api/receipt-acts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReceiptAct() throws Exception {
        // Initialize the database
        receiptActRepository.saveAndFlush(receiptAct);

        int databaseSizeBeforeUpdate = receiptActRepository.findAll().size();

        // Update the receiptAct
        ReceiptAct updatedReceiptAct = receiptActRepository.findById(receiptAct.getId()).get();
        // Disconnect from session so that the updates on updatedReceiptAct are not directly saved in db
        em.detach(updatedReceiptAct);
        updatedReceiptAct
            .total(UPDATED_TOTAL)
            .paid(UPDATED_PAID)
            .paidDoctor(UPDATED_PAID_DOCTOR)
            .date(UPDATED_DATE);

        restReceiptActMockMvc.perform(put("/api/receipt-acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReceiptAct)))
            .andExpect(status().isOk());

        // Validate the ReceiptAct in the database
        List<ReceiptAct> receiptActList = receiptActRepository.findAll();
        assertThat(receiptActList).hasSize(databaseSizeBeforeUpdate);
        ReceiptAct testReceiptAct = receiptActList.get(receiptActList.size() - 1);
        assertThat(testReceiptAct.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testReceiptAct.isPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testReceiptAct.isPaidDoctor()).isEqualTo(UPDATED_PAID_DOCTOR);
        assertThat(testReceiptAct.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the ReceiptAct in Elasticsearch
        verify(mockReceiptActSearchRepository, times(1)).save(testReceiptAct);
    }

    @Test
    @Transactional
    public void updateNonExistingReceiptAct() throws Exception {
        int databaseSizeBeforeUpdate = receiptActRepository.findAll().size();

        // Create the ReceiptAct

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptActMockMvc.perform(put("/api/receipt-acts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receiptAct)))
            .andExpect(status().isBadRequest());

        // Validate the ReceiptAct in the database
        List<ReceiptAct> receiptActList = receiptActRepository.findAll();
        assertThat(receiptActList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReceiptAct in Elasticsearch
        verify(mockReceiptActSearchRepository, times(0)).save(receiptAct);
    }

    @Test
    @Transactional
    public void deleteReceiptAct() throws Exception {
        // Initialize the database
        receiptActRepository.saveAndFlush(receiptAct);

        int databaseSizeBeforeDelete = receiptActRepository.findAll().size();

        // Delete the receiptAct
        restReceiptActMockMvc.perform(delete("/api/receipt-acts/{id}", receiptAct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReceiptAct> receiptActList = receiptActRepository.findAll();
        assertThat(receiptActList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReceiptAct in Elasticsearch
        verify(mockReceiptActSearchRepository, times(1)).deleteById(receiptAct.getId());
    }

    @Test
    @Transactional
    public void searchReceiptAct() throws Exception {
        // Initialize the database
        receiptActRepository.saveAndFlush(receiptAct);
        when(mockReceiptActSearchRepository.search(queryStringQuery("id:" + receiptAct.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(receiptAct), PageRequest.of(0, 1), 1));
        // Search the receiptAct
        restReceiptActMockMvc.perform(get("/api/_search/receipt-acts?query=id:" + receiptAct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receiptAct.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].paidDoctor").value(hasItem(DEFAULT_PAID_DOCTOR.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
