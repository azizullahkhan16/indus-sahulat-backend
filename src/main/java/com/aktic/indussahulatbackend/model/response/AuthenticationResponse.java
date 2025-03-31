package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.response.actor.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private UserDTO userInfo;
    private String token;
}

