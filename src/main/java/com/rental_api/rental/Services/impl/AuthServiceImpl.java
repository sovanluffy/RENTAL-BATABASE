package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.AuthService;
import com.rental_api.rental.Security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        ).orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getUsername());

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream().map(r -> r.getRole().getName()).collect(Collectors.toList()),
                token
        );
    }
}
