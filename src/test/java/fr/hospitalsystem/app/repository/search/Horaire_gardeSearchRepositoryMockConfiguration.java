package fr.hospitalsystem.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Horaire_gardeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Horaire_gardeSearchRepositoryMockConfiguration {

    @MockBean
    private Horaire_gardeSearchRepository mockHoraire_gardeSearchRepository;

}
