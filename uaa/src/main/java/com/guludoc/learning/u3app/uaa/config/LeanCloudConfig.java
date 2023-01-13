package com.guludoc.learning.u3app.uaa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import cn.leancloud.core.AVOSCloud;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "imooc.message-provider", name="name", havingValue = "leancloud")
public class LeanCloudConfig {

    private final AppProperties appProperties;

    @PostConstruct
    public void initialize() {
        AVOSCloud.initialize(appProperties.getLeanCloud().getAppId(), appProperties.getLeanCloud().getAppKey());
    }
}
