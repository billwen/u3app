package com.guludoc.learning.u3app.uaa.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "mooc")
public class AppProperties {

    private Jwt jwt = new Jwt();

    private Ali ali = new Ali();

    private LeanCloud leanCloud = new LeanCloud();

    private MessageProvider messageProvider = new MessageProvider();

    @Data
    @NoArgsConstructor
    public static class Jwt {

        private String header = "Authorization";

        private String prefix = "Bearer";

        private Long accessTokenExpireTime = 60_000L;

        private Long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }

    @Data
    @NoArgsConstructor
    public static class Ali {
        private String apiKey = "";

        private String apiSecret = "";
    }

    @Data
    @NoArgsConstructor
    public static class LeanCloud {
        private String appId = "";

        private String appKey = "";
    }

    @Data
    @NoArgsConstructor
    public static class MessageProvider {
        private String name = "";

        private String apiUrl = "";
    }
}
