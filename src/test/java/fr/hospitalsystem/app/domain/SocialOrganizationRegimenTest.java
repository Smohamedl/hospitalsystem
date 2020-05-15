package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class SocialOrganizationRegimenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialOrganizationRegimen.class);
        SocialOrganizationRegimen socialOrganizationRegimen1 = new SocialOrganizationRegimen();
        socialOrganizationRegimen1.setId(1L);
        SocialOrganizationRegimen socialOrganizationRegimen2 = new SocialOrganizationRegimen();
        socialOrganizationRegimen2.setId(socialOrganizationRegimen1.getId());
        assertThat(socialOrganizationRegimen1).isEqualTo(socialOrganizationRegimen2);
        socialOrganizationRegimen2.setId(2L);
        assertThat(socialOrganizationRegimen1).isNotEqualTo(socialOrganizationRegimen2);
        socialOrganizationRegimen1.setId(null);
        assertThat(socialOrganizationRegimen1).isNotEqualTo(socialOrganizationRegimen2);
    }
}
