package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class SocialOrganizationDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialOrganizationDetails.class);
        SocialOrganizationDetails socialOrganizationDetails1 = new SocialOrganizationDetails();
        socialOrganizationDetails1.setId(1L);
        SocialOrganizationDetails socialOrganizationDetails2 = new SocialOrganizationDetails();
        socialOrganizationDetails2.setId(socialOrganizationDetails1.getId());
        assertThat(socialOrganizationDetails1).isEqualTo(socialOrganizationDetails2);
        socialOrganizationDetails2.setId(2L);
        assertThat(socialOrganizationDetails1).isNotEqualTo(socialOrganizationDetails2);
        socialOrganizationDetails1.setId(null);
        assertThat(socialOrganizationDetails1).isNotEqualTo(socialOrganizationDetails2);
    }
}
