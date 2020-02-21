package fr.hospitalsystem.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.hospitalsystem.app.web.rest.TestUtil;

public class GuardScheduleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuardSchedule.class);
        GuardSchedule guardSchedule1 = new GuardSchedule();
        guardSchedule1.setId(1L);
        GuardSchedule guardSchedule2 = new GuardSchedule();
        guardSchedule2.setId(guardSchedule1.getId());
        assertThat(guardSchedule1).isEqualTo(guardSchedule2);
        guardSchedule2.setId(2L);
        assertThat(guardSchedule1).isNotEqualTo(guardSchedule2);
        guardSchedule1.setId(null);
        assertThat(guardSchedule1).isNotEqualTo(guardSchedule2);
    }
}
