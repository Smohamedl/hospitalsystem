package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class HospitalizationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hospitalization.class);
        Hospitalization hospitalization1 = new Hospitalization();
        hospitalization1.setId(1L);
        Hospitalization hospitalization2 = new Hospitalization();
        hospitalization2.setId(hospitalization1.getId());
        assertThat(hospitalization1).isEqualTo(hospitalization2);
        hospitalization2.setId(2L);
        assertThat(hospitalization1).isNotEqualTo(hospitalization2);
        hospitalization1.setId(null);
        assertThat(hospitalization1).isNotEqualTo(hospitalization2);
    }
}
