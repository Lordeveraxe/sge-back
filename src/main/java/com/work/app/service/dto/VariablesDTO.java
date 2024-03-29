package com.work.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.work.app.domain.Variables} entity.
 */
@Schema(description = "Entidad para el registro de los Pruebas")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VariablesDTO implements Serializable {

    private Long id;
    private String nombre_variables;
    private String variables;

    public String getNombreVariables() {
        return nombre_variables;
    }

    public void setNombrevariables(String nombre_variables) {
        this.nombre_variables = nombre_variables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VariablesDTO variablesDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, variablesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "VariablesDTO{" + "id=" + id + ", tipo='" + nombre_variables + '\'' + ", variables='" + variables + '\'' + '}';
    }
}
