package com.work.app.management;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Feature("Pruebas de Gestión")
@Story("Pruebas de Servicio de Métricas de Seguridad")
class SecurityMetersServiceTests {

    private static final String INVALID_TOKENS_METER_EXPECTED_NAME = "security.authentication.invalid-tokens";

    private MeterRegistry meterRegistry;
    private SecurityMetersService securityMetersService;

    @BeforeEach
    @Step("Configuración inicial para las pruebas")
    public void setup() {
        meterRegistry = new SimpleMeterRegistry();
        securityMetersService = new SecurityMetersService(meterRegistry);
    }

    @Test
    @Description("Verifica la creación de contadores para diferentes causas de tokens JWT no válidos en el registro de métricas")
    void testInvalidTokensCountersByCauseAreCreated() {
        meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).counter();
        meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "expired").counter();
        meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "unsupported").counter();
        meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "invalid-signature").counter();
        meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter();

        Collection<Counter> counters = meterRegistry.find(INVALID_TOKENS_METER_EXPECTED_NAME).counters();
        assertThat(counters).hasSize(4);
    }

    @Test
    @Description("Verifica que los métodos de conteo estén vinculados a los contadores correctos")
    void testCountMethodsShouldBeBoundToCorrectCounters() {
        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "expired").counter().count()).isZero();
        securityMetersService.trackTokenExpired();
        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "expired").counter().count()).isEqualTo(1);

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "unsupported").counter().count()).isZero();
        securityMetersService.trackTokenUnsupported();
        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "unsupported").counter().count()).isEqualTo(1);

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "invalid-signature").counter().count()).isZero();
        securityMetersService.trackTokenInvalidSignature();
        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "invalid-signature").counter().count()).isEqualTo(1);

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count()).isZero();
        securityMetersService.trackTokenMalformed();
        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count()).isEqualTo(1);
    }
}
