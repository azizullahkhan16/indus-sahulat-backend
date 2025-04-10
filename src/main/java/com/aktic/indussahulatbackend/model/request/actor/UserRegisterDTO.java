package com.aktic.indussahulatbackend.model.request.actor;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
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

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dateOfBirth;
}
