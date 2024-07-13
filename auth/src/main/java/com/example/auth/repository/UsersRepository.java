package com.example.auth.repository;

import com.example.auth.model.Users;
import com.example.auth.security.PermissionsEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT p.name FROM Permissions p JOIN PerRoleMap rpm ON p.id = rpm.pid JOIN UserRoles ur ON rpm.rid = ur.id JOIN UserRolesMap urm ON ur.id = urm.rid JOIN Users u ON urm.uid = u.id WHERE u.name = :username and rpm.status = true")
    List<PermissionsEnum> getUserPermissions(@Param("username") String username);

    @Query("SELECT u FROM Users u WHERE u.name = :username")
    Users findByUsername(String username);
}
