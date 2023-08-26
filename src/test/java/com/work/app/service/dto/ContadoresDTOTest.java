package com.work.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContadoresDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContadoresDTO.class);
        ContadoresDTO contadoresDTO1 = new ContadoresDTO();
        contadoresDTO1.setId(1L);
        ContadoresDTO contadoresDTO2 = new ContadoresDTO();
        assertThat(contadoresDTO1).isNotEqualTo(contadoresDTO2);
        contadoresDTO2.setId(contadoresDTO1.getId());
        assertThat(contadoresDTO1).isEqualTo(contadoresDTO2);
        contadoresDTO2.setId(2L);
        assertThat(contadoresDTO1).isNotEqualTo(contadoresDTO2);
        contadoresDTO1.setId(null);
        assertThat(contadoresDTO1).isNotEqualTo(contadoresDTO2);
    }
}
