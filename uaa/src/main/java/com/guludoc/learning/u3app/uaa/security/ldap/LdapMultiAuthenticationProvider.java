package com.guludoc.learning.u3app.uaa.security.ldap;

import com.guludoc.learning.u3app.uaa.repository.LdapUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class LdapMultiAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final LdapUserRepository ldapUserRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // Ignore
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return ldapUserRepository.findByUsernameAndPassword(username, authentication.getCredentials().toString())
                .orElseThrow(() -> new UsernameNotFoundException("Bad username or password"));
    }
}
