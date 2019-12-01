package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class ActypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actype.class);
        Actype actype1 = new Actype();
        actype1.setId(1L);
        Actype actype2 = new Actype();
        actype2.setId(actype1.getId());
        assertThat(actype1).isEqualTo(actype2);
        actype2.setId(2L);
        assertThat(actype1).isNotEqualTo(actype2);
        actype1.setId(null);
        assertThat(actype1).isNotEqualTo(actype2);
    }
}
