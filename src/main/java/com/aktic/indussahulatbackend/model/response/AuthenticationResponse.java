package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.response.actor.UserInfo;
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

