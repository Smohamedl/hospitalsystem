package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class DoctorMonthlyPaymentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorMonthlyPayment.class);
        DoctorMonthlyPayment doctorMonthlyPayment1 = new DoctorMonthlyPayment();
        doctorMonthlyPayment1.setId(1L);
        DoctorMonthlyPayment doctorMonthlyPayment2 = new DoctorMonthlyPayment();
        doctorMonthlyPayment2.setId(doctorMonthlyPayment1.getId());
        assertThat(doctorMonthlyPayment1).isEqualTo(doctorMonthlyPayment2);
        doctorMonthlyPayment2.setId(2L);
        assertThat(doctorMonthlyPayment1).isNotEqualTo(doctorMonthlyPayment2);
        doctorMonthlyPayment1.setId(null);
        assertThat(doctorMonthlyPayment1).isNotEqualTo(doctorMonthlyPayment2);
    }
}
