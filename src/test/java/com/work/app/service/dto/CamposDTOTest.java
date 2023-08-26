package com.work.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CamposDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CamposDTO.class);
        CamposDTO camposDTO1 = new CamposDTO();
        camposDTO1.setId(1L);
        CamposDTO camposDTO2 = new CamposDTO();
        assertThat(camposDTO1).isNotEqualTo(camposDTO2);
        camposDTO2.setId(camposDTO1.getId());
        assertThat(camposDTO1).isEqualTo(camposDTO2);
        camposDTO2.setId(2L);
        assertThat(camposDTO1).isNotEqualTo(camposDTO2);
        camposDTO1.setId(null);
        assertThat(camposDTO1).isNotEqualTo(camposDTO2);
    }
}
