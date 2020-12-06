package com.feedback.repo.entity;

public enum Role {

    ADMINISTRATOR("ADMINISTATOR"),
    TEACHER("TEACHER"),
    USER("USER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Role{" +
                "value='" + value + '\'' +
                '}';
    }
}
