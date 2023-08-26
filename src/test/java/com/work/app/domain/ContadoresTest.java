package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContadoresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contadores.class);
        Contadores contadores1 = new Contadores();
        contadores1.setId(1L);
        Contadores contadores2 = new Contadores();
        contadores2.setId(contadores1.getId());
        assertThat(contadores1).isEqualTo(contadores2);
        contadores2.setId(2L);
        assertThat(contadores1).isNotEqualTo(contadores2);
        contadores1.setId(null);
        assertThat(contadores1).isNotEqualTo(contadores2);
    }
}
