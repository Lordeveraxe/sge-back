package com.work.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistrosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistrosDTO.class);
        RegistrosDTO registrosDTO1 = new RegistrosDTO();
        registrosDTO1.setId(1L);
        RegistrosDTO registrosDTO2 = new RegistrosDTO();
        assertThat(registrosDTO1).isNotEqualTo(registrosDTO2);
        registrosDTO2.setId(registrosDTO1.getId());
        assertThat(registrosDTO1).isEqualTo(registrosDTO2);
        registrosDTO2.setId(2L);
        assertThat(registrosDTO1).isNotEqualTo(registrosDTO2);
        registrosDTO1.setId(null);
        assertThat(registrosDTO1).isNotEqualTo(registrosDTO2);
    }
}
