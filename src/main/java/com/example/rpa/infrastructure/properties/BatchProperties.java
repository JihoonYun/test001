package com.example.rpa.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 배치 실행 주기와 식별자 등 배치 공통 설정을 바인딩한다.
 */
@ConfigurationProperties(prefix = "rpa.batch")
public record BatchProperties(
        String jobName,
        String cron
) {
}
