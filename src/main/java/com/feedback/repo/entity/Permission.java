package com.feedback.repo.entity;

import lombok.Getter;

@Getter
public enum Permission {
    READ("user:read"),
    WRITE("user:write"),
    CREATE("admin:create");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
