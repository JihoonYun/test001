package com.example.rpa.application.dto;

import java.time.LocalDateTime;

/**
 * 배치 실행 요청 시 필요한 입력값을 전달하는 커맨드다.
 */
public record ReportGenerationCommand(
        String executionId,
        LocalDateTime requestedAt,
        String triggerType
) {
}
