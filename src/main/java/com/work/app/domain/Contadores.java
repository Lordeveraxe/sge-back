package com.work.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.work.app.domain.enumeration.Estado;
import com.work.app.domain.enumeration.TipoContador;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad para el registro de los Contadores
 */
@Entity
@Table(name = "contadores")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contadores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contador")
    private TipoContador tipoContador;

    @Column(name = "fecha_ingreso_contador")
    private Instant fechaIngresoContador;

    @Column(name = "tag")
    private String tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contadores")
    @JsonIgnoreProperties(value = { "contadores" }, allowSetters = true)
    private Set<Registros> registros = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contadores" }, allowSetters = true)
    private Variables variables;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contadores" }, allowSetters = true)
    private Campos campo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contadores id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoContador getTipoContador() {
        return this.tipoContador;
    }

    public Contadores tipoContador(TipoContador tipoContador) {
        this.setTipoContador(tipoContador);
        return this;
    }

    public void setTipoContador(TipoContador tipoContador) {
        this.tipoContador = tipoContador;
    }

    public Instant getFechaIngresoContador() {
        return this.fechaIngresoContador;
    }

    public Contadores fechaIngresoContador(Instant fechaIngresoContador) {
        this.setFechaIngresoContador(fechaIngresoContador);
        return this;
    }

    public void setFechaIngresoContador(Instant fechaIngresoContador) {
        this.fechaIngresoContador = fechaIngresoContador;
    }

    public String getTag() {
        return this.tag;
    }

    public Contadores tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public Contadores estado(Estado estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Set<Registros> getRegistros() {
        return this.registros;
    }

    public void setRegistros(Set<Registros> registros) {
        if (this.registros != null) {
            this.registros.forEach(i -> i.setContadores(null));
        }
        if (registros != null) {
            registros.forEach(i -> i.setContadores(this));
        }
        this.registros = registros;
    }

    public Contadores registros(Set<Registros> registros) {
        this.setRegistros(registros);
        return this;
    }

    public Contadores addRegistros(Registros registros) {
        this.registros.add(registros);
        registros.setContadores(this);
        return this;
    }

    public Contadores removeRegistros(Registros registros) {
        this.registros.remove(registros);
        registros.setContadores(null);
        return this;
    }

    public Variables getVariables() {
        return this.variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public Contadores variables(Variables variables) {
        this.setVariables(variables);
        return this;
    }

    public Campos getCampo() {
        return this.campo;
    }

    public void setCampo(Campos campos) {
        this.campo = campos;
    }

    public Contadores campo(Campos campos) {
        this.setCampo(campos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contadores)) {
            return false;
        }
        return id != null && id.equals(((Contadores) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contadores{" +
            "id=" + getId() +
            ", tipoContador='" + getTipoContador() + "'" +
            ", fechaIngresoContador='" + getFechaIngresoContador() + "'" +
            ", tag='" + getTag() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
