package com.rental_api.rental.config;

import com.rental_api.rental.Entity.Role;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.UserRole;
import com.rental_api.rental.Repository.RoleRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner addDefaultUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Ensure USER role exists
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USER");
                        return roleRepository.save(role);
                    });

            // Check if default user exists by username or email
            if (userRepository.findByUsernameOrEmail("reach", "reach@example.com").isEmpty()) {
                // Create default user
                User reachUser = new User();
                reachUser.setUsername("reach");
                reachUser.setEmail("reach@example.com");
                reachUser.setFirstName("Reach");
                reachUser.setLastName("User");
                reachUser.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(reachUser);

                // Assign USER role to the user
                UserRole ur = new UserRole();
                ur.setUser(reachUser);
                ur.setRole(userRole);
                userRoleRepository.save(ur);

                System.out.println("Default user created: reach / password123 with role USER");
            }
        };
    }
}
