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
    CommandLineRunner addNewUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Ensure USER role exists12
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USER");
                        return roleRepository.save(role);
                    });

            // Add new user "reach" if not exists
            if (userRepository.findByUsernameOrEmail("reach", "reach@example.com").isEmpty()) {
                User reachUser = new User();
                reachUser.setUsername("reach");
                reachUser.setEmail("reach@example.com");
                reachUser.setFirstName("Reach");
                reachUser.setLastName("User");
                reachUser.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(reachUser);

                // Assign USER role
                UserRole ur = new UserRole();
                ur.setUser(reachUser);
                ur.setRole(userRole);
                userRoleRepository.save(ur);

                System.out.println("New user created: reach / password123 with role USER");
            }
        };
    }
}
