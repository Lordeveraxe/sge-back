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
@Table(name = "campos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Campos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_campo")
    private String nombreCampo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campo")
    @JsonIgnoreProperties(value = { "registros", "variables", "campo" }, allowSetters = true)
    private Set<Contadores> contadores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCampo() {
        return this.nombreCampo;
    }

    public Campos nombreCampo(String nombreCampo) {
        this.setNombreCampo(nombreCampo);
        return this;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public Set<Contadores> getContadores() {
        return this.contadores;
    }

    public void setContadores(Set<Contadores> contadores) {
        if (this.contadores != null) {
            this.contadores.forEach(i -> i.setCampo(null));
        }
        if (contadores != null) {
            contadores.forEach(i -> i.setCampo(this));
        }
        this.contadores = contadores;
    }

    public Campos contadores(Set<Contadores> contadores) {
        this.setContadores(contadores);
        return this;
    }

    public Campos addContadores(Contadores contadores) {
        this.contadores.add(contadores);
        contadores.setCampo(this);
        return this;
    }

    public Campos removeContadores(Contadores contadores) {
        this.contadores.remove(contadores);
        contadores.setCampo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campos)) {
            return false;
        }
        return id != null && id.equals(((Campos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campos{" +
            "id=" + getId() +
            ", nombreCampo='" + getNombreCampo() + "'" +
            "}";
    }
}
