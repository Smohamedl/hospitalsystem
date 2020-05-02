package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class ActTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Act.class);
        Act act1 = new Act();
        act1.setId(1L);
        Act act2 = new Act();
        act2.setId(act1.getId());
        assertThat(act1).isEqualTo(act2);
        act2.setId(2L);
        assertThat(act1).isNotEqualTo(act2);
        act1.setId(null);
        assertThat(act1).isNotEqualTo(act2);
    }
}
