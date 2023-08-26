package com.work.app.domain.enumeration;

/**
 * The TipoContador enumeration.
 */
public enum TipoContador {
    GENERADORES("Generadores"),
    CONTADORES("Contadores"),
    PRODUCCION("Produccion");

    private final String value;

    TipoContador(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
