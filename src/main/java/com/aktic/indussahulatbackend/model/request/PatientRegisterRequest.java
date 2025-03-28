package com.aktic.indussahulatbackend.model.request;

import com.aktic.indussahulatbackend.model.enums.BloodType;
import com.aktic.indussahulatbackend.model.enums.GenderType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "CNIC is required")
//    @Pattern(regexp = "\\d{5}-\\d{7}-\\d", message = "Invalid CNIC format (XXXXX-XXXXXXX-X)")
    private String CNIC;

    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "\\+92\\d{10}", message = "Phone number must be in format +92XXXXXXXXXX")
    private String phone;

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

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1 year")
    private Integer age;

}
