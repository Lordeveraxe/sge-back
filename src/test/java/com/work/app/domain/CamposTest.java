package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CamposTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campos.class);
        Campos campos1 = new Campos();
        campos1.setId(1L);
        Campos campos2 = new Campos();
        campos2.setId(campos1.getId());
        assertThat(campos1).isEqualTo(campos2);
        campos2.setId(2L);
        assertThat(campos1).isNotEqualTo(campos2);
        campos1.setId(null);
        assertThat(campos1).isNotEqualTo(campos2);
    }
}
