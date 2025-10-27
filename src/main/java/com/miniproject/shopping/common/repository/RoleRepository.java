package com.miniproject.auth.common.repository;

import com.miniproject.auth.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
