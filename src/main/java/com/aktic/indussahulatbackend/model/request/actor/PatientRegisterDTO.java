package com.aktic.indussahulatbackend.model.request.actor;

import com.aktic.indussahulatbackend.model.enums.BloodType;
import com.aktic.indussahulatbackend.model.enums.GenderType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterDTO extends UserRegisterDTO {

    @NotNull(message = "Weight is required")
    @Min(value = 30, message = "Weight must be at least 30 kg")
    private Float weight;

    @NotNull(message = "Height is required")
    @Min(value = 50, message = "Height must be at least 50 cm")
    private Float height;

    @NotNull(message = "Blood type is required")
    private BloodType bloodType;

    @NotNull(message = "Gender is required")
    private GenderType gender;

}
