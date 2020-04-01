package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.SocialOrganization;
import fr.hospitalsystem.app.repository.SocialOrganizationRepository;
import fr.hospitalsystem.app.repository.search.SocialOrganizationSearchRepository;
import fr.hospitalsystem.app.service.SocialOrganizationService;
import fr.hospitalsystem.app.web.rest.errors.ExceptionTranslator;
import fr.hospitalsystem.app.service.dto.SocialOrganizationCriteria;
import fr.hospitalsystem.app.service.SocialOrganizationQueryService;

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
 * Integration tests for the {@link SocialOrganizationResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class SocialOrganizationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SocialOrganizationRepository socialOrganizationRepository;

    @Autowired
    private SocialOrganizationService socialOrganizationService;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.SocialOrganizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private SocialOrganizationSearchRepository mockSocialOrganizationSearchRepository;

    @Autowired
    private SocialOrganizationQueryService socialOrganizationQueryService;

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

    private MockMvc restSocialOrganizationMockMvc;

    private SocialOrganization socialOrganization;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SocialOrganizationResource socialOrganizationResource = new SocialOrganizationResource(socialOrganizationService, socialOrganizationQueryService);
        this.restSocialOrganizationMockMvc = MockMvcBuilders.standaloneSetup(socialOrganizationResource)
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
    public static SocialOrganization createEntity(EntityManager em) {
        SocialOrganization socialOrganization = new SocialOrganization()
            .name(DEFAULT_NAME);
        return socialOrganization;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialOrganization createUpdatedEntity(EntityManager em) {
        SocialOrganization socialOrganization = new SocialOrganization()
            .name(UPDATED_NAME);
        return socialOrganization;
    }

    @BeforeEach
    public void initTest() {
        socialOrganization = createEntity(em);
    }

    @Test
    @Transactional
    public void createSocialOrganization() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationRepository.findAll().size();

        // Create the SocialOrganization
        restSocialOrganizationMockMvc.perform(post("/api/social-organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganization)))
            .andExpect(status().isCreated());

        // Validate the SocialOrganization in the database
        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        SocialOrganization testSocialOrganization = socialOrganizationList.get(socialOrganizationList.size() - 1);
        assertThat(testSocialOrganization.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the SocialOrganization in Elasticsearch
        verify(mockSocialOrganizationSearchRepository, times(1)).save(testSocialOrganization);
    }

    @Test
    @Transactional
    public void createSocialOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = socialOrganizationRepository.findAll().size();

        // Create the SocialOrganization with an existing ID
        socialOrganization.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialOrganizationMockMvc.perform(post("/api/social-organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganization)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganization in the database
        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeCreate);

        // Validate the SocialOrganization in Elasticsearch
        verify(mockSocialOrganizationSearchRepository, times(0)).save(socialOrganization);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialOrganizationRepository.findAll().size();
        // set the field null
        socialOrganization.setName(null);

        // Create the SocialOrganization, which fails.

        restSocialOrganizationMockMvc.perform(post("/api/social-organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganization)))
            .andExpect(status().isBadRequest());

        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizations() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSocialOrganization() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get the socialOrganization
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations/{id}", socialOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(socialOrganization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getSocialOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        Long id = socialOrganization.getId();

        defaultSocialOrganizationShouldBeFound("id.equals=" + id);
        defaultSocialOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultSocialOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSocialOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultSocialOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSocialOrganizationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSocialOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name equals to DEFAULT_NAME
        defaultSocialOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the socialOrganizationList where name equals to UPDATED_NAME
        defaultSocialOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name not equals to DEFAULT_NAME
        defaultSocialOrganizationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the socialOrganizationList where name not equals to UPDATED_NAME
        defaultSocialOrganizationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSocialOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the socialOrganizationList where name equals to UPDATED_NAME
        defaultSocialOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name is not null
        defaultSocialOrganizationShouldBeFound("name.specified=true");

        // Get all the socialOrganizationList where name is null
        defaultSocialOrganizationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSocialOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name contains DEFAULT_NAME
        defaultSocialOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the socialOrganizationList where name contains UPDATED_NAME
        defaultSocialOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSocialOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        socialOrganizationRepository.saveAndFlush(socialOrganization);

        // Get all the socialOrganizationList where name does not contain DEFAULT_NAME
        defaultSocialOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the socialOrganizationList where name does not contain UPDATED_NAME
        defaultSocialOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocialOrganizationShouldBeFound(String filter) throws Exception {
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocialOrganizationShouldNotBeFound(String filter) throws Exception {
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSocialOrganization() throws Exception {
        // Get the socialOrganization
        restSocialOrganizationMockMvc.perform(get("/api/social-organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialOrganization() throws Exception {
        // Initialize the database
        socialOrganizationService.save(socialOrganization);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSocialOrganizationSearchRepository);

        int databaseSizeBeforeUpdate = socialOrganizationRepository.findAll().size();

        // Update the socialOrganization
        SocialOrganization updatedSocialOrganization = socialOrganizationRepository.findById(socialOrganization.getId()).get();
        // Disconnect from session so that the updates on updatedSocialOrganization are not directly saved in db
        em.detach(updatedSocialOrganization);
        updatedSocialOrganization
            .name(UPDATED_NAME);

        restSocialOrganizationMockMvc.perform(put("/api/social-organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSocialOrganization)))
            .andExpect(status().isOk());

        // Validate the SocialOrganization in the database
        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeUpdate);
        SocialOrganization testSocialOrganization = socialOrganizationList.get(socialOrganizationList.size() - 1);
        assertThat(testSocialOrganization.getName()).isEqualTo(UPDATED_NAME);

        // Validate the SocialOrganization in Elasticsearch
        verify(mockSocialOrganizationSearchRepository, times(1)).save(testSocialOrganization);
    }

    @Test
    @Transactional
    public void updateNonExistingSocialOrganization() throws Exception {
        int databaseSizeBeforeUpdate = socialOrganizationRepository.findAll().size();

        // Create the SocialOrganization

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialOrganizationMockMvc.perform(put("/api/social-organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(socialOrganization)))
            .andExpect(status().isBadRequest());

        // Validate the SocialOrganization in the database
        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SocialOrganization in Elasticsearch
        verify(mockSocialOrganizationSearchRepository, times(0)).save(socialOrganization);
    }

    @Test
    @Transactional
    public void deleteSocialOrganization() throws Exception {
        // Initialize the database
        socialOrganizationService.save(socialOrganization);

        int databaseSizeBeforeDelete = socialOrganizationRepository.findAll().size();

        // Delete the socialOrganization
        restSocialOrganizationMockMvc.perform(delete("/api/social-organizations/{id}", socialOrganization.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialOrganization> socialOrganizationList = socialOrganizationRepository.findAll();
        assertThat(socialOrganizationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SocialOrganization in Elasticsearch
        verify(mockSocialOrganizationSearchRepository, times(1)).deleteById(socialOrganization.getId());
    }

    @Test
    @Transactional
    public void searchSocialOrganization() throws Exception {
        // Initialize the database
        socialOrganizationService.save(socialOrganization);
        when(mockSocialOrganizationSearchRepository.search(queryStringQuery("id:" + socialOrganization.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(socialOrganization), PageRequest.of(0, 1), 1));
        // Search the socialOrganization
        restSocialOrganizationMockMvc.perform(get("/api/_search/social-organizations?query=id:" + socialOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
