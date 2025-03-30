package com.aktic.indussahulatbackend.repository.role;

import com.aktic.indussahulatbackend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String user);

    boolean existsByRoleName(String user);

    Optional<Role> findByroleName(String a);
}
