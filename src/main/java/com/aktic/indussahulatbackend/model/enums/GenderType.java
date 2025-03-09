package com.aktic.indussahulatbackend.model.enums;

public enum GenderType {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String value;


    GenderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
