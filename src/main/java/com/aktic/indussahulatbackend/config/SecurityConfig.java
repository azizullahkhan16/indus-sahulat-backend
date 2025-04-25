package com.aktic.indussahulatbackend.config;

import com.aktic.indussahulatbackend.constant.Constants;
import com.aktic.indussahulatbackend.filter.JwtAuthenticationFilter;
import com.aktic.indussahulatbackend.security.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(Constants.WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/chat/**").hasAnyRole("PATIENT", "AMBULANCE_DRIVER", "AMBULANCE_PROVIDER", "HOSPITAL_ADMIN")
                                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                                .requestMatchers("/api/ambulance-driver/**").hasRole("AMBULANCE_DRIVER")
                                .requestMatchers("/api/ambulance-provider/**").hasRole("AMBULANCE_PROVIDER")
                                .requestMatchers("/api/hospital-admin/**").hasRole("HOSPITAL_ADMIN")
                                .requestMatchers("/api/notification/**").hasAnyRole("PATIENT", "AMBULANCE_DRIVER", "AMBULANCE_PROVIDER", "HOSPITAL_ADMIN")
                                .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .logout(logout -> logout.addLogoutHandler(logoutHandler))
        return http.build();
    }
}
