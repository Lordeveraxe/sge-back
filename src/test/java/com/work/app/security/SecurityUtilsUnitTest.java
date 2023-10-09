package com.work.app.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Feature("Seguridad")
@Story("Utilidades de Seguridad")
public class SecurityUtilsUnitTest {

    @BeforeEach
    @AfterEach
    public void cleanup() {
        // Limpiamos el contexto de seguridad después de cada prueba para evitar efectos secundarios
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetCurrentUserLogin() {
        // Establecer el contexto de seguridad para el test
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Obtener el login actual y verificarlo
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        assertThat(login).contains("admin");
    }

    @Test
    public void testGetCurrentUserJWT() {
        // Establecer el contexto de seguridad para el test
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
        SecurityContextHolder.setContext(securityContext);

        // Obtener el JWT actual y verificarlo
        Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
        assertThat(jwt).contains("token");
    }

    @Test
    public void testIsAuthenticated() {
        // Establecer el contexto de seguridad para el test
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Verificar si el usuario está autenticado
        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    public void testAnonymousIsNotAuthenticated() {
        // Establecer el contexto de seguridad para el test
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
        SecurityContextHolder.setContext(securityContext);

        // Verificar que el usuario anónimo no esté autenticado
        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        assertThat(isAuthenticated).isFalse();
    }

    @Test
    public void testHasCurrentUserThisAuthority() {
        // Establecer el contexto de seguridad para el test
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
        SecurityContextHolder.setContext(securityContext);

        // Verificar los permisos del usuario
        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)).isTrue();
        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)).isFalse();
    }
    // ... Resto de tus pruebas
}
