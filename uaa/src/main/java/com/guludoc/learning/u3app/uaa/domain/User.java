package com.guludoc.learning.u3app.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
@Proxy(lazy = false)
@Entity
@Table(name = "mooc_users")
public class User implements UserDetails {

    private static final long serialVersionUID = -7189824224534351030L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @NotEmpty
    @Column(name = "password_hash" ,length = 128, nullable = false)
    private String password;

    @NotEmpty
    @Column(length = 128, unique = true, nullable = false)
    private String email;

    @Column(length = 11, nullable = false)
    private String mobile;

    @Length(max = 50)
    private String name;

    // 基础类型，方法是is开头
    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(name ="account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    @Builder.Default
    @Column(name = "using_mfa", nullable = false)
    private boolean usingMfa = false;

    @Column(name = "mfa_key")
    private String mfaKey;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "mooc_users_roles",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            }
        )
    private Set<Role> authorities;

}
