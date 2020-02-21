package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class Horaire_gardeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Horaire_garde.class);
        Horaire_garde horaire_garde1 = new Horaire_garde();
        horaire_garde1.setId(1L);
        Horaire_garde horaire_garde2 = new Horaire_garde();
        horaire_garde2.setId(horaire_garde1.getId());
        assertThat(horaire_garde1).isEqualTo(horaire_garde2);
        horaire_garde2.setId(2L);
        assertThat(horaire_garde1).isNotEqualTo(horaire_garde2);
        horaire_garde1.setId(null);
        assertThat(horaire_garde1).isNotEqualTo(horaire_garde2);
    }
}
