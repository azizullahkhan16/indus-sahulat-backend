package com.aktic.indussahulatbackend.repository.role;

import com.aktic.indussahulatbackend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String user);

    boolean existsByRoleName(String user);

    Optional<Role> findByroleName(String a);
}
