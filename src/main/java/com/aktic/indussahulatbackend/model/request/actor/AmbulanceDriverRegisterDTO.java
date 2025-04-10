package com.aktic.indussahulatbackend.model.request.actor;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceDriverRegisterDTO extends UserRegisterDTO {

    @NotNull(message = "Company ID is required")
    private Long companyId;

}
