package com.rental_api.rental.config;

import com.rental_api.rental.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // 🔓 Public authentication endpoints
                .requestMatchers("/api/auth/**", "/register").permitAll()

                // 🔓 Public property READ (NO TOKEN)
                .requestMatchers(HttpMethod.GET, "/api/properties/**").permitAll()

                // 🔐 Property WRITE (AGENT / ADMIN only)
                .requestMatchers(HttpMethod.POST, "/api/properties/**")
                    .hasAnyRole("AGENT", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/properties/**")
                    .hasAnyRole("AGENT", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/properties/**")
                    .hasAnyRole("AGENT", "ADMIN")

                // 🔐 Everything else requires authentication
                .anyRequest().authenticated()
            )

            // JWT filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
