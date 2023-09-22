package com.work.app.security.jwt;

import static com.work.app.security.jwt.JwtAuthenticationTestUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Clase de prueba de integración para la autenticación basada en tokens JWT.
 */
@AutoConfigureMockMvc
@AuthenticationIntegrationTest
class TokenAuthenticationIT {

    @Autowired
    private MockMvc mvc;

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    /**
     * Prueba de inicio de sesión con un token válido.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testLoginWithValidToken() throws Exception {
        expectOk(createValidToken(jwtKey));
    }

    /**
     * Prueba que devuelve falso cuando el token JWT tiene una firma inválida.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testReturnFalseWhenJWThasInvalidSignature() throws Exception {
        expectUnauthorized(createTokenWithDifferentSignature());
    }

    /**
     * Prueba que devuelve falso cuando el token JWT está mal formado.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testReturnFalseWhenJWTisMalformed() throws Exception {
        expectUnauthorized(createSignedInvalidJwt(jwtKey));
    }

    /**
     * Prueba que devuelve falso cuando el token JWT ha expirado.
     *
     * @throws Exception Si ocurre un error durante la prueba.
     */
    @Test
    void testReturnFalseWhenJWTisExpired() throws Exception {
        expectUnauthorized(createExpiredToken(jwtKey));
    }

    /**
     * Realiza una solicitud HTTP GET con un token válido y espera una respuesta exitosa (código de estado 200 OK).
     *
     * @param token Token JWT válido.
     * @throws Exception Si ocurre un error durante la prueba.
     */
    private void expectOk(String token) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/authenticate").header(AUTHORIZATION, BEARER + token)).andExpect(status().isOk());
    }

    /**
     * Realiza una solicitud HTTP GET con un token no válido y espera una respuesta no autorizada (código de estado 401 Unauthorized).
     *
     * @param token Token JWT no válido.
     * @throws Exception Si ocurre un error durante la prueba.
     */
    private void expectUnauthorized(String token) throws Exception {
        mvc
            .perform(MockMvcRequestBuilders.get("/api/authenticate").header(AUTHORIZATION, BEARER + token))
            .andExpect(status().isUnauthorized());
    }
}
