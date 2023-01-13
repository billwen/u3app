package com.guludoc.learning.u3app.uaa.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "imooc.message-provider", name = "name", havingValue = "ali")
public class AliConfig {

    private final AppProperties appProperties;

    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", appProperties.getAli().getApiKey(), appProperties.getAli().getApiSecret());

        return new DefaultAcsClient(profile);
    }
}
