package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.HospitalsystemApp;
import fr.hospitalsystem.app.domain.QuantityPrice;
import fr.hospitalsystem.app.domain.Product;
import fr.hospitalsystem.app.domain.Order;
import fr.hospitalsystem.app.repository.QuantityPriceRepository;
import fr.hospitalsystem.app.repository.search.QuantityPriceSearchRepository;
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
 * Integration tests for the {@link QuantityPriceResource} REST controller.
 */
@SpringBootTest(classes = HospitalsystemApp.class)
public class QuantityPriceResourceIT {

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    @Autowired
    private QuantityPriceRepository quantityPriceRepository;

    /**
     * This repository is mocked in the fr.hospitalsystem.app.repository.search test package.
     *
     * @see fr.hospitalsystem.app.repository.search.QuantityPriceSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuantityPriceSearchRepository mockQuantityPriceSearchRepository;

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

    private MockMvc restQuantityPriceMockMvc;

    private QuantityPrice quantityPrice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuantityPriceResource quantityPriceResource = new QuantityPriceResource(quantityPriceRepository, mockQuantityPriceSearchRepository);
        this.restQuantityPriceMockMvc = MockMvcBuilders.standaloneSetup(quantityPriceResource)
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
    public static QuantityPrice createEntity(EntityManager em) {
        QuantityPrice quantityPrice = new QuantityPrice()
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        quantityPrice.setProduct(product);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        quantityPrice.setOrder(order);
        return quantityPrice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuantityPrice createUpdatedEntity(EntityManager em) {
        QuantityPrice quantityPrice = new QuantityPrice()
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        quantityPrice.setProduct(product);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        quantityPrice.setOrder(order);
        return quantityPrice;
    }

    @BeforeEach
    public void initTest() {
        quantityPrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuantityPrice() throws Exception {
        int databaseSizeBeforeCreate = quantityPriceRepository.findAll().size();

        // Create the QuantityPrice
        restQuantityPriceMockMvc.perform(post("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quantityPrice)))
            .andExpect(status().isCreated());

        // Validate the QuantityPrice in the database
        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeCreate + 1);
        QuantityPrice testQuantityPrice = quantityPriceList.get(quantityPriceList.size() - 1);
        assertThat(testQuantityPrice.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testQuantityPrice.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the QuantityPrice in Elasticsearch
        verify(mockQuantityPriceSearchRepository, times(1)).save(testQuantityPrice);
    }

    @Test
    @Transactional
    public void createQuantityPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quantityPriceRepository.findAll().size();

        // Create the QuantityPrice with an existing ID
        quantityPrice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuantityPriceMockMvc.perform(post("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quantityPrice)))
            .andExpect(status().isBadRequest());

        // Validate the QuantityPrice in the database
        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuantityPrice in Elasticsearch
        verify(mockQuantityPriceSearchRepository, times(0)).save(quantityPrice);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = quantityPriceRepository.findAll().size();
        // set the field null
        quantityPrice.setQuantity(null);

        // Create the QuantityPrice, which fails.

        restQuantityPriceMockMvc.perform(post("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quantityPrice)))
            .andExpect(status().isBadRequest());

        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = quantityPriceRepository.findAll().size();
        // set the field null
        quantityPrice.setPrice(null);

        // Create the QuantityPrice, which fails.

        restQuantityPriceMockMvc.perform(post("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quantityPrice)))
            .andExpect(status().isBadRequest());

        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuantityPrices() throws Exception {
        // Initialize the database
        quantityPriceRepository.saveAndFlush(quantityPrice);

        // Get all the quantityPriceList
        restQuantityPriceMockMvc.perform(get("/api/quantity-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quantityPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getQuantityPrice() throws Exception {
        // Initialize the database
        quantityPriceRepository.saveAndFlush(quantityPrice);

        // Get the quantityPrice
        restQuantityPriceMockMvc.perform(get("/api/quantity-prices/{id}", quantityPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quantityPrice.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingQuantityPrice() throws Exception {
        // Get the quantityPrice
        restQuantityPriceMockMvc.perform(get("/api/quantity-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuantityPrice() throws Exception {
        // Initialize the database
        quantityPriceRepository.saveAndFlush(quantityPrice);

        int databaseSizeBeforeUpdate = quantityPriceRepository.findAll().size();

        // Update the quantityPrice
        QuantityPrice updatedQuantityPrice = quantityPriceRepository.findById(quantityPrice.getId()).get();
        // Disconnect from session so that the updates on updatedQuantityPrice are not directly saved in db
        em.detach(updatedQuantityPrice);
        updatedQuantityPrice
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);

        restQuantityPriceMockMvc.perform(put("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuantityPrice)))
            .andExpect(status().isOk());

        // Validate the QuantityPrice in the database
        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeUpdate);
        QuantityPrice testQuantityPrice = quantityPriceList.get(quantityPriceList.size() - 1);
        assertThat(testQuantityPrice.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testQuantityPrice.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the QuantityPrice in Elasticsearch
        verify(mockQuantityPriceSearchRepository, times(1)).save(testQuantityPrice);
    }

    @Test
    @Transactional
    public void updateNonExistingQuantityPrice() throws Exception {
        int databaseSizeBeforeUpdate = quantityPriceRepository.findAll().size();

        // Create the QuantityPrice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuantityPriceMockMvc.perform(put("/api/quantity-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quantityPrice)))
            .andExpect(status().isBadRequest());

        // Validate the QuantityPrice in the database
        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuantityPrice in Elasticsearch
        verify(mockQuantityPriceSearchRepository, times(0)).save(quantityPrice);
    }

    @Test
    @Transactional
    public void deleteQuantityPrice() throws Exception {
        // Initialize the database
        quantityPriceRepository.saveAndFlush(quantityPrice);

        int databaseSizeBeforeDelete = quantityPriceRepository.findAll().size();

        // Delete the quantityPrice
        restQuantityPriceMockMvc.perform(delete("/api/quantity-prices/{id}", quantityPrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuantityPrice> quantityPriceList = quantityPriceRepository.findAll();
        assertThat(quantityPriceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuantityPrice in Elasticsearch
        verify(mockQuantityPriceSearchRepository, times(1)).deleteById(quantityPrice.getId());
    }

    @Test
    @Transactional
    public void searchQuantityPrice() throws Exception {
        // Initialize the database
        quantityPriceRepository.saveAndFlush(quantityPrice);
        when(mockQuantityPriceSearchRepository.search(queryStringQuery("id:" + quantityPrice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(quantityPrice), PageRequest.of(0, 1), 1));
        // Search the quantityPrice
        restQuantityPriceMockMvc.perform(get("/api/_search/quantity-prices?query=id:" + quantityPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quantityPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
}
