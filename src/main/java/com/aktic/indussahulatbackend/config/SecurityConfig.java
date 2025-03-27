package com.aktic.indussahulatbackend.config;

import com.aktic.indussahulatbackend.constant.SecurityConstants;
import com.aktic.indussahulatbackend.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(SecurityConstants.WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/patient/**").hasAnyRole("PAITENT", "ADMIN")
                                .requestMatchers("/ambulance-driver/**").hasAnyRole("AMBULANCE_DRVIER", "ADMIN")
                                .requestMatchers("/ambulance-provider/**").hasAnyRole("AMBULANCE_PROVIDER", "ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
//                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .logout(logout -> logout.addLogoutHandler(logoutHandler))
        return http.build();
    }

}
