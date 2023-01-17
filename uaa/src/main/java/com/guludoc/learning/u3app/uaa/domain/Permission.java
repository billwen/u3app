package com.guludoc.learning.u3app.uaa.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@With
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles"})
@EqualsAndHashCode(exclude = {"roles"})
@Entity
@Table(name = "mooc_permissions")
public class Permission implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "permission_name", unique = true, nullable = false, length = 50)
    private String authority;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<RBACRole> roles;

}
