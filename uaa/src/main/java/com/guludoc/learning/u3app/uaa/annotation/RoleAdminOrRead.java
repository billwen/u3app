package com.guludoc.learning.u3app.uaa.annotation;

import com.guludoc.learning.u3app.uaa.config.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('" + Constants.ROLE_ADMIN + "', '" + Constants.AUTHORITY_USER_READ + "')")
public @interface RoleAdminOrRead {
}
