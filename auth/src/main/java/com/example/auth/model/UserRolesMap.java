package com.example.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_ROLES_MAP")
public class UserRolesMap {
    @Id
    private Long uid;
    @Id
    private Long rid;
}