package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Dtos.Response.RegisterResponse;
import com.rental_api.rental.Entity.Role;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.UserRole;
import com.rental_api.rental.Exception.*;
import com.rental_api.rental.Repository.RoleRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Repository.UserRoleRepository;
import com.rental_api.rental.Security.JwtUtils;
import com.rental_api.rental.Services.AuthService;
import com.rental_api.rental.Utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Validate input
        Validation.validateRegister(request);

        // Check username/email
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new ConflictException("Username or email already exists");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new ArrayList<>());
        userRepository.save(user);

        // Assign USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER role not found"));
        assignRole(user, userRole);

        // Assign AGENT role if checked
        if (request.isAgent()) {
            Role agentRole = roleRepository.findByName("AGENT")
                    .orElseThrow(() -> new RoleNotFoundException("AGENT role not found"));
            assignRole(user, agentRole);
        }

        // Collect roles as list of strings
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getName)
                .collect(Collectors.toList());

        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                roles
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Validate input
        Validation.validateLogin(request);

        // Find user
        User user = findUser(request);

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username/email or password");
        }

        // Generate JWT
        String token = jwtUtils.generateToken(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                null
        );

        // Collect roles
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getName)
                .collect(Collectors.toList());

        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }

    // ---------------- HELPER METHODS ----------------
    private User findUser(LoginRequest request) {
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            return userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            return userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        throw new BadRequestException("Username or email must be provided");
    }

    private void assignRole(User user, Role role) {
        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(role);
        user.getRoles().add(ur);
        userRoleRepository.save(ur);
    }
}
