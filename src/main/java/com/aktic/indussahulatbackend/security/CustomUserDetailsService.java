package com.aktic.indussahulatbackend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsernameAndRole(String phone, String userRole);
}
