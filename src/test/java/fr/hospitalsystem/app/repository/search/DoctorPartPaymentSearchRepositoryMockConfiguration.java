package fr.hospitalsystem.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DoctorPartPaymentSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DoctorPartPaymentSearchRepositoryMockConfiguration {

    @MockBean
    private DoctorPartPaymentSearchRepository mockDoctorPartPaymentSearchRepository;

}
