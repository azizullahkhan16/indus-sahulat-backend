package com.aktic.indussahulatbackend.model.common;

import com.aktic.indussahulatbackend.model.entity.Role;

import java.time.Instant;
import java.time.LocalDate;

public interface UserBase {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPassword();
    String getCNIC();
    String getPhone();
    Boolean getIsVerified();
    Role getRole();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    String getImage();
    LocalDate getDateOfBirth();
}
