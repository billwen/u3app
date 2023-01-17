package com.guludoc.learning.u3app.uaa.config;

import com.guludoc.learning.u3app.uaa.domain.RBACRole;
import com.guludoc.learning.u3app.uaa.repository.RBACRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleHierarchyService {

    private final RBACRoleRepository roleRepository;

    public String getRoleHierarchyExpr() {
        List<RBACRole> roles = roleRepository.findAll();

        return roles.stream()
                .flatMap(role -> role.getPermissions()
                                    .stream()
                                    .map(permission -> role.getRoleName() + " > " + permission.getAuthority() + " "))
                .collect(Collectors.joining(" ", "ROLE_ADMIN" + " > " + "ROLE_STAFF" + " ", ""));
    }
}
