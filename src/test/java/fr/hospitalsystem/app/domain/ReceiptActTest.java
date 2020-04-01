package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class ReceiptActTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceiptAct.class);
        ReceiptAct receiptAct1 = new ReceiptAct();
        receiptAct1.setId(1L);
        ReceiptAct receiptAct2 = new ReceiptAct();
        receiptAct2.setId(receiptAct1.getId());
        assertThat(receiptAct1).isEqualTo(receiptAct2);
        receiptAct2.setId(2L);
        assertThat(receiptAct1).isNotEqualTo(receiptAct2);
        receiptAct1.setId(null);
        assertThat(receiptAct1).isNotEqualTo(receiptAct2);
    }
}
