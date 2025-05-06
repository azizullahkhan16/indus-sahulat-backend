package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMessageDTO {

    @NotNull(message = "Message can not be empty")
    private String message;
}
