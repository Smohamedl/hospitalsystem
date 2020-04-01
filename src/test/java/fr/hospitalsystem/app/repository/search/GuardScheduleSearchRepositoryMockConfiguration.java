package fr.hospitalsystem.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link GuardScheduleSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GuardScheduleSearchRepositoryMockConfiguration {

    @MockBean
    private GuardScheduleSearchRepository mockGuardScheduleSearchRepository;

}