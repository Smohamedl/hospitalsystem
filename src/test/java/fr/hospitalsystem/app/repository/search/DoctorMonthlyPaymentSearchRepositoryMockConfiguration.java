package fr.hospitalsystem.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DoctorMonthlyPaymentSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DoctorMonthlyPaymentSearchRepositoryMockConfiguration {

    @MockBean
    private DoctorMonthlyPaymentSearchRepository mockDoctorMonthlyPaymentSearchRepository;

}
