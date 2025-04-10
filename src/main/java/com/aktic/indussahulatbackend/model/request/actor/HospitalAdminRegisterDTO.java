package com.aktic.indussahulatbackend.model.request.actor;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalAdminRegisterDTO extends UserRegisterDTO {
    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;
}
