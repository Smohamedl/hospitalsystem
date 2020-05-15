package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import fr.hospitalsystem.app.domain.SocialOrganization;
import fr.hospitalsystem.app.repository.SocialOrganizationRegimenRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationRegimenSearchRepository;
import fr.hospitalsystem.app.service.SocialOrganizationRegimenService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;
import fr.hospitalsystem.app.service.dto.SocialOrganizationRegimenCriteria;
import fr.hospitalsystem.app.service.SocialOrganizationRegimenQueryService;

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
 * Integration tests for the {@link SocialOrganizationRegimenResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class SocialOrganizationRegimenResourceIT {

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;
    private static final Double SMALLER_PERCENTAGE = 1D - 1D;

    @Autowired
    private SocialOrganizationRegimenRepository socialOrganizationRegimenRepository;

    @Autowired
    private SocialOrganizationRegimenService socialOrganizationRegimenService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.SocialOrganizationRegimenSearchRepositoryMockConfiguration
     */
    @Autowired
    private SocialOrganizationRegimenSearchRepository mockSocialOrganizationRegimenSearchRepository;

    @Autowired
    private SocialOrganizationRegimenQueryService socialOrganizationRegimenQueryService;

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

    private MockMvc restSocialOrganizationRegimenMockMvc;

    private SocialOrganizationRegimen socialOrganizationRegimen;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SocialOrganizationRegimenResource socialOrganizationRegimenResource = new SocialOrganizationRegimenResource(socialOrganizationRegimenService, socialOrganizationRegimenQueryService);
        this.restSocialOrganizationRegimenMockMvc = MockMvcBuilders.standaloneSetup(socialOrganizationRegimenResource)
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
    public static SocialOrganizationRegimen createEntity(EntityManager em) {
        SocialOrganizationRegimen socialOrganizationRegimen = new SocialOrganizationRegimen()
            .percentage(DEFAULT_PERCENTAGE);
        // Add required entity
        SocialOrganization socialOrganization;
        if (TestUtil.findAll(em, SocialOrganization.class).isEmpty()) {
            socialOrganization = SocialOrganizationResourceIT.createEntity(em);
            em.persist(socialOrganization);
            em.flush();
        } else {
            socialOrganization = TestUtil.findAll(em, SocialOrganization.class).get(0);
        }
        socialOrganizationRegimen.setSocialOrganization(socialOrganization);
        return socialOrganizationRegimen;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialOrganizationRegimen createUpdatedEntity(EntityManager em) {
        SocialOrganizationRegimen socialOrganizationRegimen = new SocialOrganizationRegimen()
            .percentage(UPDATED_PERCENTAGE);
        // Add required entity
        SocialOrganization socialOrganization;
        if (TestUtil.findAll(em, SocialOrganization.class).isEmpty()) {
            socialOrganization = SocialOrganizationResourceIT.createUpdatedEntity(em);
            em.persist(socialOrganization);
            em.flush();
        } else {
            socialOrganization = TestUtil.findAll(em, SocialOrganization.class).get(0);
        }
        socialOrganizationRegimen.setSocialOrganization(socialOrganization);
        return socialOrganizationRegimen;
    }

    @BeforeEach
    public void initTest() {
        socialOrganizationRegimen = createEntity(em);
    }

    @Test
    @Transactional
    public void createSocialOrganizationRegimen() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationRegimenRepository.findAll().size();

        // Create the SocialOrganizationRegimen
        restSocialOrganizationRegimenMockMvc.perform(post("/api/social-organization-regimen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationRegimen)))
            .andExpect(status().isCreated());

        // Validate the SocialOrganizationRegimen in the database
        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeCreate + 1);
        SocialOrganizationRegimen testSocialOrganizationRegimen = socialOrganizationRegimenList.get(socialOrganizationRegimenList.size() - 1);
        assertThat(testSocialOrganizationRegimen.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);

        // Validate the SocialOrganizationRegimen in Elasticsearch
        verify(mockSocialOrganizationRegimenSearchRepository, times(1)).save(testSocialOrganizationRegimen);
    }

    @Test
    @Transactional
    public void createSocialOrganizationRegimenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationRegimenRepository.findAll().size();

        // Create the SocialOrganizationRegimen with an existing ID
        socialOrganizationRegimen.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialOrganizationRegimenMockMvc.perform(post("/api/social-organization-regimen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationRegimen)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganizationRegimen in the database
        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeCreate);

        // Validate the SocialOrganizationRegimen in Elasticsearch
        verify(mockSocialOrganizationRegimenSearchRepository, times(0)).save(socialOrganizationRegimen);
    }


    @Test
    @Transactional
    public void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialOrganizationRegimenRepository.findAll().size();
        // set the field null
        socialOrganizationRegimen.setPercentage(null);

        // Create the SocialOrganizationRegimen, which fails.

        restSocialOrganizationRegimenMockMvc.perform(post("/api/social-organization-regimen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationRegimen)))
            .andExpect(status().isBadRequest());

        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimen() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganizationRegimen.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSocialOrganizationRegimen() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get the socialOrganizationRegimen
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen/{id}", socialOrganizationRegimen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(socialOrganizationRegimen.getId().intValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()));
    }


    @Test
    @Transactional
    public void getSocialOrganizationRegimenByIdFiltering() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        Long id = socialOrganizationRegimen.getId();

        defaultSocialOrganizationRegimenShouldBeFound("id.equals=" + id);
        defaultSocialOrganizationRegimenShouldNotBeFound("id.notEquals=" + id);

        defaultSocialOrganizationRegimenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSocialOrganizationRegimenShouldNotBeFound("id.greaterThan=" + id);

        defaultSocialOrganizationRegimenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSocialOrganizationRegimenShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage equals to DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage equals to UPDATED_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage not equals to DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.notEquals=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage not equals to UPDATED_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.notEquals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage equals to UPDATED_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage is not null
        defaultSocialOrganizationRegimenShouldBeFound("percentage.specified=true");

        // Get all the socialOrganizationRegimenList where percentage is null
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage is greater than or equal to DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.greaterThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage is greater than or equal to (DEFAULT_PERCENTAGE + 1)
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.greaterThanOrEqual=" + (DEFAULT_PERCENTAGE + 1));
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage is less than or equal to DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.lessThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage is less than or equal to SMALLER_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.lessThanOrEqual=" + SMALLER_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage is less than DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.lessThan=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage is less than (DEFAULT_PERCENTAGE + 1)
        defaultSocialOrganizationRegimenShouldBeFound("percentage.lessThan=" + (DEFAULT_PERCENTAGE + 1));
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenByPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);

        // Get all the socialOrganizationRegimenList where percentage is greater than DEFAULT_PERCENTAGE
        defaultSocialOrganizationRegimenShouldNotBeFound("percentage.greaterThan=" + DEFAULT_PERCENTAGE);

        // Get all the socialOrganizationRegimenList where percentage is greater than SMALLER_PERCENTAGE
        defaultSocialOrganizationRegimenShouldBeFound("percentage.greaterThan=" + SMALLER_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllSocialOrganizationRegimenBySocialOrganizationIsEqualToSomething() throws Exception {
        // Get already existing entity
        SocialOrganization socialOrganization = socialOrganizationRegimen.getSocialOrganization();
        socialOrganizationRegimenRepository.saveAndFlush(socialOrganizationRegimen);
        Long socialOrganizationId = socialOrganization.getId();

        // Get all the socialOrganizationRegimenList where socialOrganization equals to socialOrganizationId
        defaultSocialOrganizationRegimenShouldBeFound("socialOrganizationId.equals=" + socialOrganizationId);

        // Get all the socialOrganizationRegimenList where socialOrganization equals to socialOrganizationId + 1
        defaultSocialOrganizationRegimenShouldNotBeFound("socialOrganizationId.equals=" + (socialOrganizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocialOrganizationRegimenShouldBeFound(String filter) throws Exception {
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganizationRegimen.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));

        // Check, that the count call also returns 1
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocialOrganizationRegimenShouldNotBeFound(String filter) throws Exception {
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSocialOrganizationRegimen() throws Exception {
        // Get the socialOrganizationRegimen
        restSocialOrganizationRegimenMockMvc.perform(get("/api/social-organization-regimen/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialOrganizationRegimen() throws Exception {
        // Initialize the database
        socialOrganizationRegimenService.save(socialOrganizationRegimen);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSocialOrganizationRegimenSearchRepository);

        int databaseSizeBeforeUpdate = socialOrganizationRegimenRepository.findAll().size();

        // Update the socialOrganizationRegimen
        SocialOrganizationRegimen updatedSocialOrganizationRegimen = socialOrganizationRegimenRepository.findById(socialOrganizationRegimen.getId()).get();
        // Disconnect from session so that the updates on updatedSocialOrganizationRegimen are not directly saved in db
        em.detach(updatedSocialOrganizationRegimen);
        updatedSocialOrganizationRegimen
            .percentage(UPDATED_PERCENTAGE);

        restSocialOrganizationRegimenMockMvc.perform(put("/api/social-organization-regimen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSocialOrganizationRegimen)))
            .andExpect(status().isOk());

        // Validate the SocialOrganizationRegimen in the database
        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeUpdate);
        SocialOrganizationRegimen testSocialOrganizationRegimen = socialOrganizationRegimenList.get(socialOrganizationRegimenList.size() - 1);
        assertThat(testSocialOrganizationRegimen.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);

        // Validate the SocialOrganizationRegimen in Elasticsearch
        verify(mockSocialOrganizationRegimenSearchRepository, times(1)).save(testSocialOrganizationRegimen);
    }

    @Test
    @Transactional
    public void updateNonExistingSocialOrganizationRegimen() throws Exception {
        int databaseSizeBeforeUpdate = socialOrganizationRegimenRepository.findAll().size();

        // Create the SocialOrganizationRegimen

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialOrganizationRegimenMockMvc.perform(put("/api/social-organization-regimen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganizationRegimen)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganizationRegimen in the database
        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SocialOrganizationRegimen in Elasticsearch
        verify(mockSocialOrganizationRegimenSearchRepository, times(0)).save(socialOrganizationRegimen);
    }

    @Test
    @Transactional
    public void deleteSocialOrganizationRegimen() throws Exception {
        // Initialize the database
        socialOrganizationRegimenService.save(socialOrganizationRegimen);

        int databaseSizeBeforeDelete = socialOrganizationRegimenRepository.findAll().size();

        // Delete the socialOrganizationRegimen
        restSocialOrganizationRegimenMockMvc.perform(delete("/api/social-organization-regimen/{id}", socialOrganizationRegimen.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialOrganizationRegimen> socialOrganizationRegimenList = socialOrganizationRegimenRepository.findAll();
        assertThat(socialOrganizationRegimenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SocialOrganizationRegimen in Elasticsearch
        verify(mockSocialOrganizationRegimenSearchRepository, times(1)).deleteById(socialOrganizationRegimen.getId());
    }

    @Test
    @Transactional
    public void searchSocialOrganizationRegimen() throws Exception {
        // Initialize the database
        socialOrganizationRegimenService.save(socialOrganizationRegimen);
        when(mockSocialOrganizationRegimenSearchRepository.search(queryStringQuery("id:" + socialOrganizationRegimen.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(socialOrganizationRegimen), PageRequest.of(0, 1), 1));
        // Search the socialOrganizationRegimen
        restSocialOrganizationRegimenMockMvc.perform(get("/api/_search/social-organization-regimen?query=id:" + socialOrganizationRegimen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganizationRegimen.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));
    }
}
