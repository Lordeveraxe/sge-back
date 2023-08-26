package com.work.app.service.dto;

import com.work.app.domain.enumeration.Estado;
import com.work.app.domain.enumeration.TipoContador;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.work.app.domain.Contadores} entity.
 */
@Schema(description = "Entidad para el registro de los Contadores")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContadoresDTO implements Serializable {

    private Long id;

    private TipoContador tipoContador;

    private Instant fechaIngresoContador;

    private String tag;

    private Estado estado;

    private VariablesDTO variables;

    private CamposDTO campo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoContador getTipoContador() {
        return tipoContador;
    }

    public void setTipoContador(TipoContador tipoContador) {
        this.tipoContador = tipoContador;
    }

    public Instant getFechaIngresoContador() {
        return fechaIngresoContador;
    }

    public void setFechaIngresoContador(Instant fechaIngresoContador) {
        this.fechaIngresoContador = fechaIngresoContador;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public VariablesDTO getVariables() {
        return variables;
    }

    public void setVariables(VariablesDTO variables) {
        this.variables = variables;
    }

    public CamposDTO getCampo() {
        return campo;
    }

    public void setCampo(CamposDTO campo) {
        this.campo = campo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContadoresDTO)) {
            return false;
        }

        ContadoresDTO contadoresDTO = (ContadoresDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contadoresDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContadoresDTO{" +
            "id=" + getId() +
            ", tipoContador='" + getTipoContador() + "'" +
            ", fechaIngresoContador='" + getFechaIngresoContador() + "'" +
            ", tag='" + getTag() + "'" +
            ", estado='" + getEstado() + "'" +
            ", variables=" + getVariables() +
            ", campo=" + getCampo() +
            "}";
    }
}
