package com.example.rpa.application.port.out;

import com.example.rpa.application.dto.JobExecutionContext;

import java.util.List;

/**
 * 첫 번째 조회 세트에 대한 출력 포트다.
 */
public interface LoadPrimaryDataPort {

    /**
     * 리포트 생성에 필요한 1차 데이터를 조회한다.
     *
     * @param context 실행 문맥
     * @return 문자열 기반 샘플 데이터 목록
     */
    List<String> load(JobExecutionContext context);
}
