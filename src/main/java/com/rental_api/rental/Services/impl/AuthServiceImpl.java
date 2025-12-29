package com.rental_api.rental.Services.impl;


import com.rental_api.rental.Dtos.Repuest.LoginRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Enitity.User;
import com.rental_api.rental.Enitity.UserRole;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Security.JwtUtils;
import com.rental_api.rental.Services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsernameOrEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(loginRequest.getUsernameOrEmail());
        }

        if (!userOpt.isPresent()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        // get roles as string list
        List<String> roles = user.getUserRoles() // assume you have Set<UserRole> userRoles in User entity
                .stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toList());

        String token = jwtUtils.generateJwtToken(user.getUsername(), roles);

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name(),
                roles,
                token
        );
    }
}
