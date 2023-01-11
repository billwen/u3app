package com.guludoc.learning.u3app.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name = "mooc_users")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @NotEmpty
    @Column(name = "password_hash" ,length = 255, nullable = false)
    private String password;

    @NotEmpty
    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 11, nullable = false)
    private String mobile;

    @Length(max = 50)
    private String name;

    // 基础类型，方法是is开头
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name ="account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @ManyToMany
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
