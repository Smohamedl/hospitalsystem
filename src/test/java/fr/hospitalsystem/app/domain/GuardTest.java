package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class GuardTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guard.class);
        Guard guard1 = new Guard();
        guard1.setId(1L);
        Guard guard2 = new Guard();
        guard2.setId(guard1.getId());
        assertThat(guard1).isEqualTo(guard2);
        guard2.setId(2L);
        assertThat(guard1).isNotEqualTo(guard2);
        guard1.setId(null);
        assertThat(guard1).isNotEqualTo(guard2);
    }
}
