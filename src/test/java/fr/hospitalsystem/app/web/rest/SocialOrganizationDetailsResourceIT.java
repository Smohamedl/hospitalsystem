package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.SocialOrganizationDetails;
import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import fr.hospitalsystem.app.repository.SocialOrganizationDetailsRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationDetailsSearchRepository;
import fr.hospitalsystem.app.service.SocialOrganizationDetailsService;
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
 * Integration tests for the {@link SocialOrganizationDetailsResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class SocialOrganizationDetailsResourceIT {

    private static final Long DEFAULT_REGISTRATION_NUMBER = 1L;
    private static final Long UPDATED_REGISTRATION_NUMBER = 2L;

    private static final String DEFAULT_MATRICULE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private SocialOrganizationDetailsRepository socialOrganizationDetailsRepository;

    @Autowired
    private SocialOrganizationDetailsService socialOrganizationDetailsService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.SocialOrganizationDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SocialOrganizationDetailsSearchRepository mockSocialOrganizationDetailsSearchRepository;

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

    private MockMvc restSocialOrganizationDetailsMockMvc;

    private SocialOrganizationDetails socialOrganizationDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SocialOrganizationDetailsResource socialOrganizationDetailsResource = new SocialOrganizationDetailsResource(socialOrganizationDetailsService);
        this.restSocialOrganizationDetailsMockMvc = MockMvcBuilders.standaloneSetup(socialOrganizationDetailsResource)
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
    public static SocialOrganizationDetails createEntity(EntityManager em) {
        SocialOrganizationDetails socialOrganizationDetails = new SocialOrganizationDetails()
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .matriculeNumber(DEFAULT_MATRICULE_NUMBER);
        // Add required entity
        SocialOrganizationRegimen socialOrganizationRegimen;
        if (TestUtil.findAll(em, SocialOrganizationRegimen.class).isEmpty()) {
            socialOrganizationRegimen = SocialOrganizationRegimenResourceIT.createEntity(em);
            em.persist(socialOrganizationRegimen);
            em.flush();
        } else {
            socialOrganizationRegimen = TestUtil.findAll(em, SocialOrganizationRegimen.class).get(0);
        }
        socialOrganizationDetails.setSocialOrganizationRegimen(socialOrganizationRegimen);
        return socialOrganizationDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialOrganizationDetails createUpdatedEntity(EntityManager em) {
        SocialOrganizationDetails socialOrganizationDetails = new SocialOrganizationDetails()
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .matriculeNumber(UPDATED_MATRICULE_NUMBER);
        // Add required entity
        SocialOrganizationRegimen socialOrganizationRegimen;
        if (TestUtil.findAll(em, SocialOrganizationRegimen.class).isEmpty()) {
            socialOrganizationRegimen = SocialOrganizationRegimenResourceIT.createUpdatedEntity(em);
            em.persist(socialOrganizationRegimen);
            em.flush();
        } else {
            socialOrganizationRegimen = TestUtil.findAll(em, SocialOrganizationRegimen.class).get(0);
        }
        socialOrganizationDetails.setSocialOrganizationRegimen(socialOrganizationRegimen);
        return socialOrganizationDetails;
    }

    @BeforeEach
    public void initTest() {
        socialOrganizationDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createSocialOrganizationDetails() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationDetailsRepository.findAll().size();

        // Create the SocialOrganizationDetails
        restSocialOrganizationDetailsMockMvc.perform(post("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationDetails)))
            .andExpect(status().isCreated());

        // Validate the SocialOrganizationDetails in the database
        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SocialOrganizationDetails testSocialOrganizationDetails = socialOrganizationDetailsList.get(socialOrganizationDetailsList.size() - 1);
        assertThat(testSocialOrganizationDetails.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testSocialOrganizationDetails.getMatriculeNumber()).isEqualTo(DEFAULT_MATRICULE_NUMBER);

        // Validate the SocialOrganizationDetails in Elasticsearch
        verify(mockSocialOrganizationDetailsSearchRepository, times(1)).save(testSocialOrganizationDetails);
    }

    @Test
    @Transactional
    public void createSocialOrganizationDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationDetailsRepository.findAll().size();

        // Create the SocialOrganizationDetails with an existing ID
        socialOrganizationDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialOrganizationDetailsMockMvc.perform(post("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganizationDetails in the database
        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SocialOrganizationDetails in Elasticsearch
        verify(mockSocialOrganizationDetailsSearchRepository, times(0)).save(socialOrganizationDetails);
    }


    @Test
    @Transactional
    public void checkRegistrationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialOrganizationDetailsRepository.findAll().size();
        // set the field null
        socialOrganizationDetails.setRegistrationNumber(null);

        // Create the SocialOrganizationDetails, which fails.

        restSocialOrganizationDetailsMockMvc.perform(post("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationDetails)))
            .andExpect(status().isBadRequest());

        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMatriculeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialOrganizationDetailsRepository.findAll().size();
        // set the field null
        socialOrganizationDetails.setMatriculeNumber(null);

        // Create the SocialOrganizationDetails, which fails.

        restSocialOrganizationDetailsMockMvc.perform(post("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationDetails)))
            .andExpect(status().isBadRequest());

        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationDetails() throws Exception {
        // Initialize the database
        socialOrganizationDetailsRepository.saveAndFlush(socialOrganizationDetails);

        // Get all the socialOrganizationDetailsList
        restSocialOrganizationDetailsMockMvc.perform(get("/api/social-organization-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganizationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].matriculeNumber").value(hasItem(DEFAULT_MATRICULE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getSocialOrganizationDetails() throws Exception {
        // Initialize the database
        socialOrganizationDetailsRepository.saveAndFlush(socialOrganizationDetails);

        // Get the socialOrganizationDetails
        restSocialOrganizationDetailsMockMvc.perform(get("/api/social-organization-details/{id}", socialOrganizationDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(socialOrganizationDetails.getId().intValue()))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER.intValue()))
            .andExpect(jsonPath("$.matriculeNumber").value(DEFAULT_MATRICULE_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingSocialOrganizationDetails() throws Exception {
        // Get the socialOrganizationDetails
        restSocialOrganizationDetailsMockMvc.perform(get("/api/social-organization-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialOrganizationDetails() throws Exception {
        // Initialize the database
        socialOrganizationDetailsService.save(socialOrganizationDetails);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSocialOrganizationDetailsSearchRepository);

        int databaseSizeBeforeUpdate = socialOrganizationDetailsRepository.findAll().size();

        // Update the socialOrganizationDetails
        SocialOrganizationDetails updatedSocialOrganizationDetails = socialOrganizationDetailsRepository.findById(socialOrganizationDetails.getId()).get();
        // Disconnect from session so that the updates on updatedSocialOrganizationDetails are not directly saved in db
        em.detach(updatedSocialOrganizationDetails);
        updatedSocialOrganizationDetails
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .matriculeNumber(UPDATED_MATRICULE_NUMBER);

        restSocialOrganizationDetailsMockMvc.perform(put("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSocialOrganizationDetails)))
            .andExpect(status().isOk());

        // Validate the SocialOrganizationDetails in the database
        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeUpdate);
        SocialOrganizationDetails testSocialOrganizationDetails = socialOrganizationDetailsList.get(socialOrganizationDetailsList.size() - 1);
        assertThat(testSocialOrganizationDetails.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testSocialOrganizationDetails.getMatriculeNumber()).isEqualTo(UPDATED_MATRICULE_NUMBER);

        // Validate the SocialOrganizationDetails in Elasticsearch
        verify(mockSocialOrganizationDetailsSearchRepository, times(1)).save(testSocialOrganizationDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingSocialOrganizationDetails() throws Exception {
        int databaseSizeBeforeUpdate = socialOrganizationDetailsRepository.findAll().size();

        // Create the SocialOrganizationDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialOrganizationDetailsMockMvc.perform(put("/api/social-organization-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganizationDetails in the database
        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SocialOrganizationDetails in Elasticsearch
        verify(mockSocialOrganizationDetailsSearchRepository, times(0)).save(socialOrganizationDetails);
    }

    @Test
    @Transactional
    public void deleteSocialOrganizationDetails() throws Exception {
        // Initialize the database
        socialOrganizationDetailsService.save(socialOrganizationDetails);

        int databaseSizeBeforeDelete = socialOrganizationDetailsRepository.findAll().size();

        // Delete the socialOrganizationDetails
        restSocialOrganizationDetailsMockMvc.perform(delete("/api/social-organization-details/{id}", socialOrganizationDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialOrganizationDetails> socialOrganizationDetailsList = socialOrganizationDetailsRepository.findAll();
        assertThat(socialOrganizationDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SocialOrganizationDetails in Elasticsearch
        verify(mockSocialOrganizationDetailsSearchRepository, times(1)).deleteById(socialOrganizationDetails.getId());
    }

    @Test
    @Transactional
    public void searchSocialOrganizationDetails() throws Exception {
        // Initialize the database
        socialOrganizationDetailsService.save(socialOrganizationDetails);
        when(mockSocialOrganizationDetailsSearchRepository.search(queryStringQuery("id:" + socialOrganizationDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(socialOrganizationDetails), PageRequest.of(0, 1), 1));
        // Search the socialOrganizationDetails
        restSocialOrganizationDetailsMockMvc.perform(get("/api/_search/social-organization-details?query=id:" + socialOrganizationDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganizationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].matriculeNumber").value(hasItem(DEFAULT_MATRICULE_NUMBER)));
    }
}
