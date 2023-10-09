package com.work.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.work.app.web.rest.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

// Esta clase está destinada a realizar pruebas sobre la clase de dominio "Variables"
@Feature("Pruebas de Dominio")
@Story("Pruebas de Clase de Dominio Variables")
class VariablesTest {

    // Esta prueba verifica el correcto funcionamiento del método "equals" de la clase "Variables"
    @Test
    @Description("Verificar método equals de la clase Variables")
    void equalsVerifier() throws Exception {
        equalsVerifierHelper(Variables.class);
    }

    // Paso de verificación de igualdad para la clase proporcionada
    // Se usa un helper para poder anotarlo como un paso en el reporte de Allure
    @Step("Verificador de igualdad para la clase: {0}")
    void equalsVerifierHelper(Class<?> clazz) throws Exception {
        // Utilidad para verificar la implementación estándar del método "equals"
        TestUtil.equalsVerifier(clazz);

        // Creación de dos instancias de "Variables" para comparación
        Variables variables1 = createVariableWithId(1L);
        Variables variables2 = cloneVariableWithSameId(variables1);

        // Asegurando que ambas instancias sean iguales dado que tienen el mismo ID
        assertThat(variables1).isEqualTo(variables2);

        // Cambio de ID para la segunda instancia y verificación de que ya no son iguales
        variables2.setId(2L);
        assertThat(variables1).isNotEqualTo(variables2);

        // Establecimiento de ID nulo para la primera instancia y verificación de no igualdad
        variables1.setId(null);
        assertThat(variables1).isNotEqualTo(variables2);
    }

    // Método para crear una nueva instancia de "Variables" con un ID específico
    @Step("Crear Variable con ID: {0}")
    private Variables createVariableWithId(Long id) {
        Variables variables = new Variables();
        variables.setId(id);
        return variables;
    }

    // Método para clonar una instancia de "Variables" manteniendo el mismo ID
    @Step("Clonar Variable con el mismo ID")
    private Variables cloneVariableWithSameId(Variables original) {
        Variables variables = new Variables();
        variables.setId(original.getId());
        return variables;
    }
}
