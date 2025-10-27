package com.miniproject.auth.common.repository;

import com.miniproject.auth.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<Object> findByUsername(String username);

    Optional<Object> findByEmail(String email);
}
