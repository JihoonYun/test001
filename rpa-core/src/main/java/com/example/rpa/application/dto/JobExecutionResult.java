package com.example.rpa.application.dto;

import java.nio.file.Path;

/**
 * 배치 실행의 최종 결과를 표현한다.
 */
public record JobExecutionResult(
        boolean success,
        String executionId,
        String summary,
        Path generatedFile
) {
}
