package com.guludoc.learning.u3app.uaa.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

// 配置后可以使用方法级安全注解  @PreAuthorize, @PreFilter, @PostAuthorize, @PostFilter
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
}
