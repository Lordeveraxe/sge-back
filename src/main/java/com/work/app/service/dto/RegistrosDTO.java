package com.work.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.work.app.domain.Registros} entity.
 */
@Schema(description = "Entidad para el registro de los Actividades")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistrosDTO implements Serializable {

    private Long id;

    private Instant fechaRegistroVariable;

    private String registroVariableContador;

    private ContadoresDTO contadores;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaRegistroVariable() {
        return fechaRegistroVariable;
    }

    public void setFechaRegistroVariable(Instant fechaRegistroVariable) {
        this.fechaRegistroVariable = fechaRegistroVariable;
    }

    public String getRegistroVariableContador() {
        return registroVariableContador;
    }

    public void setRegistroVariableContador(String registroVariableContador) {
        this.registroVariableContador = registroVariableContador;
    }

    public ContadoresDTO getContadores() {
        return contadores;
    }

    public void setContadores(ContadoresDTO contadores) {
        this.contadores = contadores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistrosDTO)) {
            return false;
        }

        RegistrosDTO registrosDTO = (RegistrosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, registrosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistrosDTO{" +
            "id=" + getId() +
            ", fechaRegistroVariable='" + getFechaRegistroVariable() + "'" +
            ", registroVariableContador='" + getRegistroVariableContador() + "'" +
            ", contadores=" + getContadores() +
            "}";
    }
}
