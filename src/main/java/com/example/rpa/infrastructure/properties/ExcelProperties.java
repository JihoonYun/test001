package com.example.rpa.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 엑셀 템플릿 경로와 출력 디렉터리 정보를 바인딩한다.
 */
@ConfigurationProperties(prefix = "rpa.excel")
public record ExcelProperties(
        String templatePath,
        String outputDirectory
) {
}
