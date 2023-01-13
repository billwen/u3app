package com.guludoc.learning.u3app.uaa.config;

import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "imooc.email-provider", name = "name", havingValue = "api")
public class SendGridEmailConfig {

    private final AppProperties appProperties;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(appProperties.getEmailProvider().getApiKey());
    }
}
