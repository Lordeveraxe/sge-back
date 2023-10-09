package com.work.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * Entidad para el registro de los Actividades
 */
@Entity
@Table(name = "registros")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Registros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_registro_variable", nullable = false)
    private Instant fechaRegistroVariable;

    @Column(name = "registro_variable_contador", nullable = false, columnDefinition = "json")
    private String registroVariableContador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties(value = { "registros", "variables", "campo" }, allowSetters = true)
    private Contadores contadores;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Registros id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaRegistroVariable() {
        return this.fechaRegistroVariable;
    }

    public Registros fechaRegistroVariable(Instant fechaRegistroVariable) {
        this.setFechaRegistroVariable(fechaRegistroVariable);
        return this;
    }

    public void setFechaRegistroVariable(Instant fechaRegistroVariable) {
        this.fechaRegistroVariable = fechaRegistroVariable;
    }

    public String getRegistroVariableContador() {
        return this.registroVariableContador;
    }

    public Registros registroVariableContador(String registroVariableContador) {
        this.setRegistroVariableContador(registroVariableContador);
        return this;
    }

    public void setRegistroVariableContador(String registroVariableContador) {
        this.registroVariableContador = registroVariableContador;
    }

    public Contadores getContadores() {
        return this.contadores;
    }

    public void setContadores(Contadores contadores) {
        this.contadores = contadores;
    }

    public Registros contadores(Contadores contadores) {
        this.setContadores(contadores);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registros)) {
            return false;
        }
        return id != null && id.equals(((Registros) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Registros{" +
            "id=" + getId() +
            ", fechaRegistroVariable='" + getFechaRegistroVariable() + "'" +
            ", registroVariableContador='" + getRegistroVariableContador() + "'" +
            "}";
    }
}
