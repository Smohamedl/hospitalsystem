package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class DoctorPartPaymentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorPartPayment.class);
        DoctorPartPayment doctorPartPayment1 = new DoctorPartPayment();
        doctorPartPayment1.setId(1L);
        DoctorPartPayment doctorPartPayment2 = new DoctorPartPayment();
        doctorPartPayment2.setId(doctorPartPayment1.getId());
        assertThat(doctorPartPayment1).isEqualTo(doctorPartPayment2);
        doctorPartPayment2.setId(2L);
        assertThat(doctorPartPayment1).isNotEqualTo(doctorPartPayment2);
        doctorPartPayment1.setId(null);
        assertThat(doctorPartPayment1).isNotEqualTo(doctorPartPayment2);
    }
}
