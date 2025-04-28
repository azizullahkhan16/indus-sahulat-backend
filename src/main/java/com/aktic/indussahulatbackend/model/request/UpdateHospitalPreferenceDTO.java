package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateHospitalPreferenceDTO {

    @NotEmpty(message = "Hospital IDs are required")
    private List<Long> hospitalIds;
}
