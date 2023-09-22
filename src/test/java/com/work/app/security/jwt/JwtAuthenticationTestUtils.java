package com.work.app.security.jwt;

import static com.work.app.security.SecurityUtils.AUTHORITIES_KEY;
import static com.work.app.security.SecurityUtils.JWT_ALGORITHM;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.time.Instant;
import java.util.Collections;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Clase de utilidad para pruebas de autenticación JWT.
 */
public class JwtAuthenticationTestUtils {

    public static final String BEARER = "Bearer ";

    @Bean
    private HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    private MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    /**
     * Crea un token JWT válido con el token proporcionado.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Token JWT válido.
     */
    public static String createValidToken(String jwtKey) {
        return createValidTokenForUser(jwtKey, "anonymous");
    }

    /**
     * Crea un token JWT válido para un usuario específico con el token proporcionado.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @param user   Nombre de usuario para el que se crea el token.
     * @return Token JWT válido para el usuario especificado.
     */
    public static String createValidTokenForUser(String jwtKey, String user) {
        JwtEncoder encoder = jwtEncoder(jwtKey);

        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuedAt(now)
            .expiresAt(now.plusSeconds(60))
            .subject(user)
            .claims(customClaims -> customClaims.put(AUTHORITIES_KEY, Collections.singletonList("ROLE_ADMIN")))
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Crea un token JWT con una firma diferente al proporcionado.
     *
     * @return Token JWT con firma diferente.
     */
    public static String createTokenWithDifferentSignature() {
        JwtEncoder encoder = jwtEncoder("Xfd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8");

        var now = Instant.now();
        var past = now.plusSeconds(60);

        JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(now).expiresAt(past).subject("anonymous").build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Crea un token JWT que ha expirado.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Token JWT que ha expirado.
     */
    public static String createExpiredToken(String jwtKey) {
        JwtEncoder encoder = jwtEncoder(jwtKey);

        var now = Instant.now();
        var past = now.minusSeconds(600);

        JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(past).expiresAt(past.plusSeconds(1)).subject("anonymous").build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Crea un token JWT inválido quitando el primer carácter del token válido.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Token JWT inválido.
     * @throws Exception Si ocurre un error al crear el token inválido.
     */
    public static String createInvalidToken(String jwtKey) throws Exception {
        return createValidToken(jwtKey).substring(1);
    }

    /**
     * Crea un token JWT firmado inválido.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Token JWT firmado inválido.
     * @throws Exception Si ocurre un error al crear el token inválido.
     */
    public static String createSignedInvalidJwt(String jwtKey) throws Exception {
        return calculateHMAC("foo", jwtKey);
    }

    /**
     * Crea un codificador JWT utilizando la clave JWT proporcionada.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Codificador JWT configurado con la clave proporcionada.
     */
    private static JwtEncoder jwtEncoder(String jwtKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey(jwtKey)));
    }

    /**
     * Obtiene la clave secreta a partir de la clave JWT proporcionada.
     *
     * @param jwtKey Clave JWT para firmar el token.
     * @return Clave secreta derivada de la clave JWT.
     */
    private static SecretKey getSecretKey(String jwtKey) {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    /**
     * Calcula el HMAC (Código de Autenticación de Mensajes Basado en Hash) de un dato utilizando la clave proporcionada.
     *
     * @param data Datos para calcular el HMAC.
     * @param key  Clave secreta para el cálculo del HMAC.
     * @return HMAC calculado como una cadena hexadecimal.
     * @throws Exception Si ocurre un error durante el cálculo HMAC.
     */
    private static String calculateHMAC(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.from(key).decode(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return String.copyValueOf(Hex.encode(mac.doFinal(data.getBytes())));
    }
}
