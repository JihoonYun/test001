package com.example.rpa.application.dto;

import com.example.rpa.domain.model.ReportSection;

import java.util.List;

/**
 * 여러 조회 결과를 엑셀 작성용 데이터로 정리한 애플리케이션 DTO다.
 */
public record ReportPayload(
        List<ReportSection> sections
) {
}
