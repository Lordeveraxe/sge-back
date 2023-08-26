package com.work.app.domain.enumeration;

/**
 * The Estado enumeration.
 */
public enum Estado {
    ACTIVO("A"),
    INACTIVO("I");

    private final String value;

    Estado(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
