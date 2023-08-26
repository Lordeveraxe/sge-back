package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VariablesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Variables.class);
        Variables variables1 = new Variables();
        variables1.setId(1L);
        Variables variables2 = new Variables();
        variables2.setId(variables1.getId());
        assertThat(variables1).isEqualTo(variables2);
        variables2.setId(2L);
        assertThat(variables1).isNotEqualTo(variables2);
        variables1.setId(null);
        assertThat(variables1).isNotEqualTo(variables2);
    }
}
