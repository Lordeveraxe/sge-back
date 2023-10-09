package com.work.app.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.work.app.IntegrationTest;
import com.work.app.config.Constants;
import com.work.app.domain.User;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import tech.jhipster.config.JHipsterProperties;

@Feature("Servicio de correo")
@IntegrationTest
public class MailServiceIT {

    private static final String[] idiomas = {};

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @MockBean
    private JavaMailSender javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> capturadorMensajes;

    @Autowired
    private MailService servicioCorreo;

    @BeforeEach
    @Step("Configuración inicial antes de cada prueba.")
    public void configuracionInicial() {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @Test
    @Story("Enviar correo simple.")
    @Description("Prueba el envío de un correo simple.")
    void pruebaEnviarCorreo() throws Exception {
        servicioCorreo.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getSubject()).isEqualTo("testSubject");
        assertThat(mensaje.getAllRecipients()[0]).hasToString("john.doe@example.com");
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent()).isInstanceOf(String.class);
        assertThat(mensaje.getContent()).hasToString("testContent");
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    @Story("Enviar correo HTML.")
    @Description("Prueba el envío de un correo en formato HTML.")
    void pruebaEnviarCorreoHTML() throws Exception {
        servicioCorreo.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getSubject()).isEqualTo("testSubject");
        assertThat(mensaje.getAllRecipients()[0]).hasToString("john.doe@example.com");
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent()).isInstanceOf(String.class);
        assertThat(mensaje.getContent()).hasToString("testContent");
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @Story("Enviar correo multi-partes.")
    @Description("Prueba el envío de un correo con múltiples partes.")
    void pruebaEnviarCorreoMultipartes() throws Exception {
        servicioCorreo.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        MimeMultipart mp = (MimeMultipart) mensaje.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(mensaje.getSubject()).isEqualTo("testSubject");
        assertThat(mensaje.getAllRecipients()[0]).hasToString("john.doe@example.com");
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos).hasToString("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    @Story("Envío de correo desde una plantilla.")
    @Description("Prueba el envío de un correo desde una plantilla.")
    void pruebaEnviarCorreoDesdePlantilla() throws Exception {
        User usuario = new User();
        usuario.setLangKey(Constants.DEFAULT_LANGUAGE);
        usuario.setLogin("john");
        usuario.setEmail("john.doe@example.com");
        servicioCorreo.sendEmailFromTemplate(usuario, "mail/testEmail", "email.test.title");
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getSubject()).isEqualTo("test title");
        assertThat(mensaje.getAllRecipients()[0]).hasToString(usuario.getEmail());
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent().toString()).isEqualToNormalizingNewlines("<html>test title, http://127.0.0.1:8080, john</html>\n");
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @Story("Envío de correo de activación.")
    @Description("Prueba el envío de un correo de activación.")
    void pruebaEnviarCorreoActivacion() throws Exception {
        User usuario = new User();
        usuario.setLangKey(Constants.DEFAULT_LANGUAGE);
        usuario.setLogin("john");
        usuario.setEmail("john.doe@example.com");
        servicioCorreo.sendActivationEmail(usuario);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getAllRecipients()[0]).hasToString(usuario.getEmail());
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent().toString()).isNotEmpty();
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @Story("Reiniciar contraseña por correo.")
    @Description("Prueba el envío de un correo para reiniciar la contraseña.")
    void pruebaReinicioContrasenaCorreo() throws Exception {
        User usuario = new User();
        usuario.setLangKey(Constants.DEFAULT_LANGUAGE);
        usuario.setLogin("john");
        usuario.setEmail("john.doe@example.com");
        servicioCorreo.sendPasswordResetMail(usuario);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getAllRecipients()[0]).hasToString(usuario.getEmail());
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent().toString()).isNotEmpty();
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @Story("Envío de correo con excepción.")
    @Description("Prueba el manejo de excepciones durante el envío de correo.")
    void pruebaEnvioCorreoConExcepcion() {
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        try {
            servicioCorreo.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción");
        }
    }

    @Test
    @Story("Envío de correo de creación.")
    @Description("Prueba el envío de un correo de creación.")
    void pruebaEnviarCorreoCreacion() throws Exception {
        User usuario = new User();
        usuario.setLangKey(Constants.DEFAULT_LANGUAGE);
        usuario.setLogin("john");
        usuario.setEmail("john.doe@example.com");
        servicioCorreo.sendCreationEmail(usuario);
        verify(javaMailSender).send(capturadorMensajes.capture());
        MimeMessage mensaje = capturadorMensajes.getValue();
        assertThat(mensaje.getAllRecipients()[0]).hasToString(usuario.getEmail());
        assertThat(mensaje.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
        assertThat(mensaje.getContent().toString()).isNotEmpty();
        assertThat(mensaje.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }
}
