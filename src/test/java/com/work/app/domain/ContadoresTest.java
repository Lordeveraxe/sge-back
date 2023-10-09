package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

@Feature("Domain Tests")
@Story("Contadores Domain Class Tests")
class ContadoresTest {

    @Test
    @Description("Verify equals method of Contadores class")
    void equalsVerifier() throws Exception {
        equalsVerifierHelper(Contadores.class);
    }

    @Step("Equals verifier for class: {0}")
    void equalsVerifierHelper(Class<?> clazz) throws Exception {
        TestUtil.equalsVerifier(clazz);
        Contadores contadores1 = createContadorWithId(1L);
        Contadores contadores2 = cloneContadorWithSameId(contadores1);

        assertThat(contadores1).isEqualTo(contadores2);

        contadores2.setId(2L);
        assertThat(contadores1).isNotEqualTo(contadores2);

        contadores1.setId(null);
        assertThat(contadores1).isNotEqualTo(contadores2);
    }

    @Step("Create Contador with ID: {0}")
    private Contadores createContadorWithId(Long id) {
        Contadores contadores = new Contadores();
        contadores.setId(id);
        return contadores;
    }

    @Step("Clone Contador with the same ID")
    private Contadores cloneContadorWithSameId(Contadores original) {
        Contadores contadores = new Contadores();
        contadores.setId(original.getId());
        return contadores;
    }
}
