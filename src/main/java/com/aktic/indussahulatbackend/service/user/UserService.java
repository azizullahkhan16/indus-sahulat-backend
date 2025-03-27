package com.aktic.indussahulatbackend.service.user;

import com.aktic.indussahulatbackend.model.response.UserInfo;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public ResponseEntity<ApiResponse<UserInfo>> getUserInfo() {
    }
}
