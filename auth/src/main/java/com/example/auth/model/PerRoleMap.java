package com.example.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PER_ROLE_MAP")
public class PerRoleMap {
    @Id
    private Long pid;
    @Id
    private Long rid;
    private Boolean status;
}
