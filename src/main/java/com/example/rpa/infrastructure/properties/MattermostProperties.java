package com.example.rpa.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mattermost API 연동에 필요한 서버 주소와 인증 정보를 바인딩한다.
 */
@ConfigurationProperties(prefix = "rpa.mattermost")
public record MattermostProperties(
        String baseUrl,
        String token,
        String channelId
) {
}
