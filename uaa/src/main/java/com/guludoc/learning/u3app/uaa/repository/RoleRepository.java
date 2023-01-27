package com.guludoc.learning.u3app.uaa.repository;

import com.guludoc.learning.u3app.uaa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);

    Optional<Role> findOptionalByRoleName(String roleName);
}
