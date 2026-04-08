package com.example.rpa.application.dto;

import java.time.LocalDateTime;

/**
 * 배치 한 번의 실행 문맥을 표현한다.
 */
public record JobExecutionContext(
        String executionId,
        LocalDateTime startedAt,
        String jobName
) {
}
