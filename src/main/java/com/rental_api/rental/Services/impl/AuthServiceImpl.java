package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Dtos.Response.RegisterResponse;
import com.rental_api.rental.Entity.Role;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.UserRole;
import com.rental_api.rental.Repository.RoleRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Repository.UserRoleRepository;
import com.rental_api.rental.Security.JwtUtils;
import com.rental_api.rental.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ================= LOGIN =================
    @Override
    public LoginResponse login(LoginRequest request) {

        User user;

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Username or Email is required");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getUsername());


return new LoginResponse(
        token, 
        user.getId(),         // id              
        user.getUsername(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getRoles()
                .stream()
                .map(r -> r.getRole().getName())
                .collect(Collectors.toList())
);

    }

    // ================= REGISTER =================
    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsernameOrEmail(
                request.getUsername(),
                request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        // Create User
        User user = new User();
        user.setFirstName(request.getFirst_name());
        user.setLastName(request.getLast_name());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // ===== ROLE ASSIGNMENT =====
        Role role;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        } else {
            role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        // ===== RESPONSE =====
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRoles(role.getName());
        response.setMessage(user.getUsername() + " registered successfully at " + timestamp);

        return response;
    }
}
