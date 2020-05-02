package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class FixedDoctorPaymentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedDoctorPayment.class);
        FixedDoctorPayment fixedDoctorPayment1 = new FixedDoctorPayment();
        fixedDoctorPayment1.setId(1L);
        FixedDoctorPayment fixedDoctorPayment2 = new FixedDoctorPayment();
        fixedDoctorPayment2.setId(fixedDoctorPayment1.getId());
        assertThat(fixedDoctorPayment1).isEqualTo(fixedDoctorPayment2);
        fixedDoctorPayment2.setId(2L);
        assertThat(fixedDoctorPayment1).isNotEqualTo(fixedDoctorPayment2);
        fixedDoctorPayment1.setId(null);
        assertThat(fixedDoctorPayment1).isNotEqualTo(fixedDoctorPayment2);
    }
}
