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
    CommandLineRunner addDefaultRolesAndUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // --- Ensure roles exist ---
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "USER")));

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));

            Role agentRole = roleRepository.findByName("AGENT")
                    .orElseGet(() -> roleRepository.save(new Role(null, "AGENT")));

            // --- Default USER ---
            if (userRepository.findByUsernameOrEmail("reach", "reach@example.com").isEmpty()) {
                User reachUser = new User();
                reachUser.setUsername("reach");
                reachUser.setEmail("reach@example.com");
                reachUser.setFirstName("Reach");
                reachUser.setLastName("User");
                reachUser.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(reachUser);

                UserRole ur = new UserRole();
                ur.setUser(reachUser);
                ur.setRole(userRole);
                userRoleRepository.save(ur);
                System.out.println("Default USER created: reach / password123");
            }

            // --- Default ADMIN ---
            if (userRepository.findByUsernameOrEmail("admin", "admin@example.com").isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(adminUser);

                UserRole ur = new UserRole();
                ur.setUser(adminUser);
                ur.setRole(adminRole);
                userRoleRepository.save(ur);
                System.out.println("Default ADMIN created: admin / admin123");
            }

            // --- Default AGENT ---
            if (userRepository.findByUsernameOrEmail("agent", "agent@example.com").isEmpty()) {
                User agentUser = new User();
                agentUser.setUsername("agent");
                agentUser.setEmail("agent@example.com");
                agentUser.setFirstName("Agent");
                agentUser.setLastName("User");
                agentUser.setPassword(passwordEncoder.encode("agent123"));
                userRepository.save(agentUser);

                UserRole ur = new UserRole();
                ur.setUser(agentUser);
                ur.setRole(agentRole);
                userRoleRepository.save(ur);
                System.out.println("Default AGENT created: agent / agent123");
            }
        };
    }
}
