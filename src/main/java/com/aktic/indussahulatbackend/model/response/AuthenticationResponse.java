package com.aktic.indussahulatbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private UserInfo userInfo;
    private String token;
}

