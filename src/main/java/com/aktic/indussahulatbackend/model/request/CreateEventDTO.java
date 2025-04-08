package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEventDTO {
    @NotNull(message = "Location can not be empty")
    private LocationDTO location;

    @NotNull(message = "Address can not be empty")
    private String address;
}
