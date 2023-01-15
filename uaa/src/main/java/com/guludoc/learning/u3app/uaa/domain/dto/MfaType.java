package com.guludoc.learning.u3app.uaa.domain.dto;

import java.util.Arrays;

public enum MfaType {
    EMAIL("email"),
    SMS("sms");

    private String value;

    private MfaType(String value) {
        this.value = value;
    }

    public static MfaType fromValue(String value) {
        for (MfaType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }
}
