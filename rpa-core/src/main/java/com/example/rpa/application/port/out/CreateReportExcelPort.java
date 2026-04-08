package com.example.rpa.application.port.out;

import com.example.rpa.application.dto.JobExecutionContext;
import com.example.rpa.application.dto.ReportPayload;

import java.nio.file.Path;

/**
 * 엑셀 생성 책임을 외부 어댑터로 분리하기 위한 출력 포트다.
 */
public interface CreateReportExcelPort {

    /**
     * 리포트 데이터를 바탕으로 실제 엑셀 파일을 생성한다.
     *
     * @param context 실행 문맥
     * @param payload 엑셀에 반영할 데이터
     * @return 생성된 파일 경로
     */
    Path create(JobExecutionContext context, ReportPayload payload);
}
