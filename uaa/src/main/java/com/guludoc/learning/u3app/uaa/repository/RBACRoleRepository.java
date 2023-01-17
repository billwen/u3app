package com.guludoc.learning.u3app.uaa.repository;

import com.guludoc.learning.u3app.uaa.domain.RBACRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RBACRoleRepository extends JpaRepository<RBACRole, Long> {
}
