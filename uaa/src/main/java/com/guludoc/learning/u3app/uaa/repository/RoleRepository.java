package com.guludoc.learning.u3app.uaa.repository;

import com.guludoc.learning.u3app.uaa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
