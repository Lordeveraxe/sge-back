package com.work.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Clase abstracta base para entidades que contendrán definiciones para atributos de creado, última modificación,
 * creado por y última modificación por.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Método abstracto para obtener el identificador de la entidad.
     *
     * @return El identificador de la entidad.
     */
    public abstract T getId();

    /**
     * Obtiene el nombre del usuario que creó esta entidad.
     *
     * @return El nombre del usuario creador.
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();

    /**
     * Obtiene el nombre del usuario que creó esta entidad.
     *
     * @return El nombre del usuario creador.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Establece el nombre del usuario que creó esta entidad.
     *
     * @param createdBy El nombre del usuario creador.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Obtiene la fecha y hora en que se creó esta entidad.
     *
     * @return La fecha y hora de creación en formato Instant.
     */
    public Instant getCreatedDate() {
        return createdDate;
    }

    /**
     * Establece la fecha y hora en que se creó esta entidad.
     *
     * @param createdDate La fecha y hora de creación en formato Instant.
     */
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Obtiene el nombre del último usuario que modificó esta entidad.
     *
     * @return El nombre del último usuario que modificó la entidad.
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Establece el nombre del último usuario que modificó esta entidad.
     *
     * @param lastModifiedBy El nombre del último usuario que modificó la entidad.
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Obtiene la fecha y hora de la última modificación de esta entidad.
     *
     * @return La fecha y hora de la última modificación en formato Instant.
     */
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Establece la fecha y hora de la última modificación de esta entidad.
     *
     * @param lastModifiedDate La fecha y hora de la última modificación en formato Instant.
     */
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
