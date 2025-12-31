package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Dtos.Response.RegisterResponse;
import com.rental_api.rental.Entity.Role;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.UserRole;
import com.rental_api.rental.Exception.RoleNotFoundException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.RoleRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Repository.UserRoleRepository;
import com.rental_api.rental.Security.JwtUtils;
import com.rental_api.rental.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // ================= LOGIN =================
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = findUserByUsernameOrEmail(request);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Get role names and IDs
        List<String> roleNames = user.getRoles() == null ? new ArrayList<>() :
                user.getRoles().stream()
                        .map(UserRole::getRole)
                        .map(Role::getName)
                        .collect(Collectors.toList());

        List<Long> roleIds = user.getRoles() == null ? new ArrayList<>() :
                user.getRoles().stream()
                        .map(UserRole::getRole)
                        .map(Role::getId)
                        .collect(Collectors.toList());

        // Generate JWT
        String token = jwtUtils.generateToken(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames,
                roleIds
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleNames
        );
    }

    // ================= REGISTER =================
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new ArrayList<>());
        userRepository.save(user);

        // Always assign USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER role not found"));
        assignRole(user, userRole);

        // Assign extra roles if provided
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            for (Long roleId : request.getRoleIds()) {
                Role extraRole = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found"));
                if (!extraRole.getName().equalsIgnoreCase("USER")) {
                    assignRole(user, extraRole);
                }
            }
        }

        List<String> roleNames = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getName)
                .collect(Collectors.toList());

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRoles(String.join(", ", roleNames));
        response.setMessage("Registered successfully at " + timestamp);

        return response;
    }

    // ================= HELPER METHODS =================
    private User findUserByUsernameOrEmail(LoginRequest request) {
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            return userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            return userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        throw new RuntimeException("Username or Email is required");
    }

    private void assignRole(User user, Role role) {
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }
}
