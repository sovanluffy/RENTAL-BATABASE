package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {}
