package com.work.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidad para el registro de los Pruebas
 */
@Schema(description = "Entidad para el registro de los Pruebas")
@Entity
@Table(name = "variables")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Variables implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_variables", nullable = false)
    private String nombre;

    @Column(name = "campo", nullable = false)
    private String campo;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "variables", nullable = false, columnDefinition = "json")
    private String variables;

    @OneToMany(mappedBy = "variables")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sistemaContadores", "registros", "variables", "campos" }, allowSetters = true)
    private Set<Contadores> contadores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Variables id(Long id) {
        this.setId(id);
        return this;
    }

    public String getVariables() {
        return this.variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Variables variables(String variables) {
        this.setVariables(variables);
        return this;
    }

    public Set<Contadores> getContadores() {
        return this.contadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setContadores(Set<Contadores> contadores) {
        if (this.contadores != null) {
            this.contadores.forEach(i -> i.setVariables(null));
        }
        if (contadores != null) {
            contadores.forEach(i -> i.setVariables(this));
        }
        this.contadores = contadores;
    }

    public Variables contadores(Set<Contadores> contadores) {
        this.setContadores(contadores);
        return this;
    }

    public Variables addContadores(Contadores contadores) {
        this.contadores.add(contadores);
        contadores.setVariables(this);
        return this;
    }

    public Variables removeContadores(Contadores contadores) {
        this.contadores.remove(contadores);
        contadores.setVariables(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variables)) {
            return false;
        }
        return id != null && id.equals(((Variables) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Variables{" + "id=" + getId() + " ,nombre='" + getNombre() + "'" + " ,campo='" + getCampo() + "'" + " ,tipo='" + getTipo() + "'" +", variables='" + getVariables() + "'" + "}";
    }
}
