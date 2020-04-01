package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class SocialOrganizationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialOrganization.class);
        SocialOrganization socialOrganization1 = new SocialOrganization();
        socialOrganization1.setId(1L);
        SocialOrganization socialOrganization2 = new SocialOrganization();
        socialOrganization2.setId(socialOrganization1.getId());
        assertThat(socialOrganization1).isEqualTo(socialOrganization2);
        socialOrganization2.setId(2L);
        assertThat(socialOrganization1).isNotEqualTo(socialOrganization2);
        socialOrganization1.setId(null);
        assertThat(socialOrganization1).isNotEqualTo(socialOrganization2);
    }
}
