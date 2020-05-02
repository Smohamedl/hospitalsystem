package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.Provider;
import fr.hospitalsystem.app.repository.ProviderRepository;
import fr.hospitalsystem.app.repository.search.ProviderSearchRepository;
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
 * Integration tests for the {@link ProviderResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class ProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    @Autowired
    private ProviderRepository providerRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.ProvidedrSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProviderSearchRepository mockProviderSearchRepository;

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

    private MockMvc restProvidedrMockMvc;

    private Provider provider;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProviderResource providerResource = new ProviderResource(providerRepository, mockProviderSearchRepository);
        this.restProvidedrMockMvc = MockMvcBuilders.standaloneSetup(providerResource)
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
    public static Provider createEntity(EntityManager em) {
        Provider provider = new Provider()
            .name(DEFAULT_NAME)
            .tel(DEFAULT_TEL)
            .adress(DEFAULT_ADRESS);
        return provider;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createUpdatedEntity(EntityManager em) {
        Provider provider = new Provider()
            .name(UPDATED_NAME)
            .tel(UPDATED_TEL)
            .adress(UPDATED_ADRESS);
        return provider;
    }

    @BeforeEach
    public void initTest() {
        provider = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvidedr() throws Exception {
        int databaseSizeBeforeCreate = providerRepository.findAll().size();

        // Create the Provider
        restProvidedrMockMvc.perform(post("/api/providedrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isCreated());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeCreate + 1);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvider.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testProvider.getAdress()).isEqualTo(DEFAULT_ADRESS);

        // Validate the Provider in Elasticsearch
        verify(mockProviderSearchRepository, times(1)).save(testProvider);
    }

    @Test
    @Transactional
    public void createProvidedrWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = providerRepository.findAll().size();

        // Create the Provider with an existing ID
        provider.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvidedrMockMvc.perform(post("/api/providedrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Provider in Elasticsearch
        verify(mockProviderSearchRepository, times(0)).save(provider);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setName(null);

        // Create the Provider, which fails.

        restProvidedrMockMvc.perform(post("/api/providedrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvidedrs() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get all the providedrList
        restProvidedrMockMvc.perform(get("/api/providedrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }

    @Test
    @Transactional
    public void getProvidedr() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get the provider
        restProvidedrMockMvc.perform(get("/api/providedrs/{id}", provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(provider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS));
    }

    @Test
    @Transactional
    public void getNonExistingProvidedr() throws Exception {
        // Get the provider
        restProvidedrMockMvc.perform(get("/api/providedrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvidedr() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider
        Provider updatedProvider = providerRepository.findById(provider.getId()).get();
        // Disconnect from session so that the updates on updatedProvider are not directly saved in db
        em.detach(updatedProvider);
        updatedProvider
            .name(UPDATED_NAME)
            .tel(UPDATED_TEL)
            .adress(UPDATED_ADRESS);

        restProvidedrMockMvc.perform(put("/api/providedrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProvider)))
            .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testProvider.getAdress()).isEqualTo(UPDATED_ADRESS);

        // Validate the Provider in Elasticsearch
        verify(mockProviderSearchRepository, times(1)).save(testProvider);
    }

    @Test
    @Transactional
    public void updateNonExistingProvidedr() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Create the Provider

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvidedrMockMvc.perform(put("/api/providedrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Provider in Elasticsearch
        verify(mockProviderSearchRepository, times(0)).save(provider);
    }

    @Test
    @Transactional
    public void deleteProvidedr() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeDelete = providerRepository.findAll().size();

        // Delete the provider
        restProvidedrMockMvc.perform(delete("/api/providedrs/{id}", provider.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Provider in Elasticsearch
        verify(mockProviderSearchRepository, times(1)).deleteById(provider.getId());
    }

    @Test
    @Transactional
    public void searchProvidedr() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);
        when(mockProviderSearchRepository.search(queryStringQuery("id:" + provider.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(provider), PageRequest.of(0, 1), 1));
        // Search the provider
        restProvidedrMockMvc.perform(get("/api/_search/providedrs?query=id:" + provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }
}
