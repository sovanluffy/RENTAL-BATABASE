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
    public LoginResponse login(LoginRequest request) throws Exception {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        // Try to find user by username first, then email
        User user = null;

        if (loginRequest.getUsername() != null && !loginRequest.getUsername().isEmpty()) {
            user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        }

        if (user == null && loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()) {
            user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        }

        if (user == null) {
            throw new Exception("User not found");
        }

        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getUsername());

        // Return LoginResponse
        return new LoginResponse(
    token,  // <-- comma added
    user.getId(),
    user.getUsername(),
    user.getEmail(),
    user.getFirstName(),
    user.getLastName(),
    user.getRoles().stream()
            .map(r -> r.getRole().getName())
            .collect(Collectors.toList())  // <-- removed trailing comma
);

    }

    // ================= REGISTER =================
    @Override
public RegisterResponse register(RegisterRequest request) {

    // Check if user already exists
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

    // ===== ROLES ASSIGNMENT =====
    Role role;
    if (request.getRoleId() != null) {
        role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
    } else {
        role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
    }

    // Save UserRole
    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);

    // ===== Build Response =====
    String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    RegisterResponse response = new RegisterResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setEmail(user.getEmail());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setPhone(user.getPhone()); // keep actual phone
    response.setRoles(role.getName()); // set role properly
    response.setMessage(user.getUsername() + " registered successfully at " + timestamp);

    return response;
}
}