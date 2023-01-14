package com.guludoc.learning.u3app.uaa.config;

import lombok.RequiredArgsConstructor;
import org.passay.MessageResolver;
import org.passay.spring.SpringMessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot 国际化踩坑指南
 * https://zhuanlan.zhihu.com/p/110980107
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;

    private final Environment environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);

        registry.setOrder(1);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * Provide international support for Passay
     * @return
     */
    @Bean
    public MessageResolver messageResolver() {
        return new SpringMessageResolver(messageSource);
    }

    /**
     * Provide international support for Validation annotation
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);

        return bean;
    }

    /**
     * 使用Spring MVC配置 cors
     * @param registry Cors的注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            registry.addMapping("/**")
                    .allowedHeaders("*")
                    .exposedHeaders("X-Authenticate")
                    .allowedOrigins("http://localhost:3000");
        } else {
            registry.addMapping("/api/**")
                    .allowedHeaders("*")
                    .exposedHeaders("X-Authenticate")
                    .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                    .allowedOrigins("https://uaa.imooc.com");
        }

    }
}
