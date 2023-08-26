package com.work.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VariablesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VariablesDTO.class);
        VariablesDTO variablesDTO1 = new VariablesDTO();
        variablesDTO1.setId(1L);
        VariablesDTO variablesDTO2 = new VariablesDTO();
        assertThat(variablesDTO1).isNotEqualTo(variablesDTO2);
        variablesDTO2.setId(variablesDTO1.getId());
        assertThat(variablesDTO1).isEqualTo(variablesDTO2);
        variablesDTO2.setId(2L);
        assertThat(variablesDTO1).isNotEqualTo(variablesDTO2);
        variablesDTO1.setId(null);
        assertThat(variablesDTO1).isNotEqualTo(variablesDTO2);
    }
}
