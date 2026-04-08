package com.example.rpa.application.port.in;

import com.example.rpa.application.dto.JobExecutionResult;
import com.example.rpa.application.dto.ReportGenerationCommand;

/**
 * 리포트 생성과 외부 전송을 담당하는 대표 유스케이스다.
 */
public interface GenerateAndSendReportUseCase {

    /**
     * 전체 배치 흐름을 실행한다.
     *
     * @param command 실행 요청 정보
     * @return 배치 실행 결과 요약
     */
    JobExecutionResult execute(ReportGenerationCommand command);
}
