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

    // ================= REGISTER =================
    @Override
    public RegisterResponse register(RegisterRequest request) {
        Validation.validateRegister(request);

        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new ConflictException("Username or email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new ArrayList<>());
        userRepository.save(user);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER role not found"));
        assignRole(user, userRole);

        if (request.isAgent()) {
            Role agentRole = roleRepository.findByName("AGENT")
                    .orElseThrow(() -> new RoleNotFoundException("AGENT role not found"));
            assignRole(user, agentRole);
        }

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

    // ================= LOGIN =================
    @Override
    public LoginResponse login(LoginRequest request) {
        Validation.validateLogin(request);
        User user = findUser(request);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username/email or password");
        }

        // Get roles & IDs
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getName)
                .collect(Collectors.toList());

        List<Long> roleIds = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getId)
                .collect(Collectors.toList());

        // Generate token including roles & roleIds
        String token = jwtUtils.generateToken(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles,
                roleIds
        );

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

    // ================= HELPER METHODS =================
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
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }
}