package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

@Feature("Pruebas de Dominio")
@Story("Pruebas de Clase de Dominio Registros")
class RegistrosTest {

    @Test
    @Description("Verificar método equals de la clase Registros")
    void equalsVerifier() throws Exception {
        equalsVerifierHelper(Registros.class);
    }

    @Step("Verificador de igualdad para la clase: {0}")
    void equalsVerifierHelper(Class<?> clazz) throws Exception {
        TestUtil.equalsVerifier(clazz);
        Registros registros1 = createRegistroWithId(1L);
        Registros registros2 = cloneRegistroWithSameId(registros1);

        assertThat(registros1).isEqualTo(registros2);

        registros2.setId(2L);
        assertThat(registros1).isNotEqualTo(registros2);

        registros1.setId(null);
        assertThat(registros1).isNotEqualTo(registros2);
    }

    @Step("Crear Registro con ID: {0}")
    private Registros createRegistroWithId(Long id) {
        Registros registros = new Registros();
        registros.setId(id);
        return registros;
    }

    @Step("Clonar Registro con el mismo ID")
    private Registros cloneRegistroWithSameId(Registros original) {
        Registros registros = new Registros();
        registros.setId(original.getId());
        return registros;
    }
}
