package com.work.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.work.app.domain.Campos} entity.
 */
@Schema(description = "Entidad para el registro de los Pruebas")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CamposDTO implements Serializable {

    private Long id;

    private String nombreCampo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CamposDTO)) {
            return false;
        }

        CamposDTO camposDTO = (CamposDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, camposDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CamposDTO{" +
            "id=" + getId() +
            ", nombreCampo='" + getNombreCampo() + "'" +
            "}";
    }
}
