package com.aktic.indussahulatbackend.config;

import com.aktic.indussahulatbackend.model.entity.Role;
import com.aktic.indussahulatbackend.repository.role.RoleRepository;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner insertRoles() {
        return args -> {
            try {
                if (!roleRepository.existsByRoleName("PATIENT")) {
                    Role patientRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("PATIENT")
                            .description("This is a patient role")
                            .build();
                    roleRepository.save(patientRole);
                    log.info("PATIENT role inserted successfully.");
                }

                // create role for Ambulance Driver
                if (!roleRepository.existsByRoleName("AMBULANCE_DRIVER")) {
                    Role ambulanceDriverRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("AMBULANCE_DRIVER")
                            .description("This is an ambulance driver role")
                            .build();
                    roleRepository.save(ambulanceDriverRole);
                    log.info("AMBULANCE_DRIVER role inserted successfully.");
                }

                // create role for Ambulance Provider
                if (!roleRepository.existsByRoleName("AMBULANCE_PROVIDER")) {
                    Role ambulanceProviderRole = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("AMBULANCE_PROVIDER")
                            .description("This is an ambulance provider role")
                            .build();
                    roleRepository.save(ambulanceProviderRole);
                    log.info("AMBULANCE_PROVIDER role inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting roles: " + e.getMessage());
            }
        };
    }
}
