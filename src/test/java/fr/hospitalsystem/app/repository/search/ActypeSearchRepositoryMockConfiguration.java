package fr.hospitalsystem.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ActypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ActypeSearchRepositoryMockConfiguration {

    @MockBean
    private ActypeSearchRepository mockActypeSearchRepository;

}
