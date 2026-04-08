package com.example.rpa.domain.model;

import java.util.List;

/**
 * 엑셀 시트 또는 리포트 구간에 들어갈 데이터를 표현하는 도메인 모델이다.
 */
public record ReportSection(
        String title,
        List<String> rows
) {
}
