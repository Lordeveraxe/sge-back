package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistrosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registros.class);
        Registros registros1 = new Registros();
        registros1.setId(1L);
        Registros registros2 = new Registros();
        registros2.setId(registros1.getId());
        assertThat(registros1).isEqualTo(registros2);
        registros2.setId(2L);
        assertThat(registros1).isNotEqualTo(registros2);
        registros1.setId(null);
        assertThat(registros1).isNotEqualTo(registros2);
    }
}
