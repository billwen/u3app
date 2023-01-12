package com.guludoc.learning.u3app.uaa.repository;

import com.guludoc.learning.u3app.uaa.domain.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {

    Optional<LdapUser> findByUsernameAndPassword(String username, String password);
}
