package com.work.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Entidad para el registro de los Pruebas
 */
@Entity
@Table(name = "variables")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Variables implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "variables")
    private String variables;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variables")
    @JsonIgnoreProperties(value = { "registros", "variables", "campo" }, allowSetters = true)
    private Set<Contadores> contadores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Variables id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariables() {
        return this.variables;
    }

    public Variables variables(String variables) {
        this.setVariables(variables);
        return this;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Set<Contadores> getContadores() {
        return this.contadores;
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
        return "Variables{" +
            "id=" + getId() +
            ", variables='" + getVariables() + "'" +
            "}";
    }
}
