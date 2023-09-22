package com.work.app.security.jwt;

import static com.work.app.security.jwt.JwtAuthenticationTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Clase de prueba de integración para las métricas de seguridad de autenticación basada en tokens JWT.
 */
@AutoConfigureMockMvc
@AuthenticationIntegrationTest
class TokenAuthenticationSecurityMetersIT {

    private static final String INVALID_TOKENS_METER_EXPECTED_NAME = "security.authentication.invalid-tokens";

    @Autowired
    private MockMvc mvc;

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * Prueba que un token válido no debe contar en las métricas.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testValidTokenShouldNotCountAnything() throws Exception {
        Collection<Counter> counters = meterRegistry.find(INVALID_TOKENS_METER_EXPECTED_NAME).counters();

        var count = aggregate(counters);

        tryToAuthenticate(createValidToken(jwtKey));

        assertThat(aggregate(counters)).isEqualTo(count);
    }

    /**
     * Prueba que un token JWT caducado se cuenta en las métricas.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testTokenExpiredCount() throws Exception {
        var count = meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "expired").counter().count();

        tryToAuthenticate(createExpiredToken(jwtKey));

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "expired").counter().count()).isEqualTo(count + 1);
    }

    /**
     * Prueba que un token JWT con firma inválida se cuenta en las métricas.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testTokenSignatureInvalidCount() throws Exception {
        var count = meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "invalid-signature").counter().count();

        tryToAuthenticate(createTokenWithDifferentSignature());

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "invalid-signature").counter().count())
            .isEqualTo(count + 1);
    }

    /**
     * Prueba que un token JWT mal formado se cuenta en las métricas.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testTokenMalformedCount() throws Exception {
        var count = meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count();

        tryToAuthenticate(createSignedInvalidJwt(jwtKey));

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count()).isEqualTo(count + 1);
    }

    /**
     * Prueba que un token JWT inválido se cuenta en las métricas.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testTokenInvalidCount() throws Exception {
        var count = meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count();

        tryToAuthenticate(createInvalidToken(jwtKey));

        assertThat(meterRegistry.get(INVALID_TOKENS_METER_EXPECTED_NAME).tag("cause", "malformed").counter().count()).isEqualTo(count + 1);
    }

    /**
     * Realiza una solicitud HTTP GET con un token y registra métricas.
     *
     * @param token Token JWT para la solicitud.
     * @throws Exception Si ocurre un error durante la prueba.
     */
    private void tryToAuthenticate(String token) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/authenticate").header(AUTHORIZATION, BEARER + token));
    }

    /**
     * Realiza una suma agregada de contadores en una colección de métricas.
     *
     * @param counters Colección de contadores a sumar.
     * @return Suma de contadores.
     */
    private double aggregate(Collection<Counter> counters) {
        return counters.stream().mapToDouble(Counter::count).sum();
    }
}
