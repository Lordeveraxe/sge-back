package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.domain.Campos;
import com.work.app.web.rest.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

@Feature("Gestión de Campos")
class CamposTest {

    @Test
    @Story("Verificar igualdad de Campos")
    @Description("Verifica que el método equals de la clase Campos funciona correctamente.")
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campos.class);

        Campos campos1 = createCamposWithId(1L);
        Campos campos2 = cloneCamposWithSameId(campos1);

        assertThat(campos1).isEqualTo(campos2);

        campos2.setId(2L);
        assertThat(campos1).isNotEqualTo(campos2);

        campos1.setId(null);
        assertThat(campos1).isNotEqualTo(campos2);
    }

    @Step("Crear Campos con ID: {0}")
    private Campos createCamposWithId(Long id) {
        Campos campos = new Campos();
        campos.setId(id);
        return campos;
    }

    @Step("Clonar Campos con el mismo ID")
    private Campos cloneCamposWithSameId(Campos original) {
        Campos campos = new Campos();
        campos.setId(original.getId());
        return campos;
    }
}
