package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class QuantityPriceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuantityPrice.class);
        QuantityPrice quantityPrice1 = new QuantityPrice();
        quantityPrice1.setId(1L);
        QuantityPrice quantityPrice2 = new QuantityPrice();
        quantityPrice2.setId(quantityPrice1.getId());
        assertThat(quantityPrice1).isEqualTo(quantityPrice2);
        quantityPrice2.setId(2L);
        assertThat(quantityPrice1).isNotEqualTo(quantityPrice2);
        quantityPrice1.setId(null);
        assertThat(quantityPrice1).isNotEqualTo(quantityPrice2);
    }
}
