package com.example.rpa.application.port.out;

import com.example.rpa.application.dto.JobExecutionContext;

import java.util.List;

/**
 * 두 번째 조회 세트에 대한 출력 포트다.
 */
public interface LoadSecondaryDataPort {

    /**
     * 리포트의 추가 시트 또는 보조 데이터 영역에 사용할 조회 결과를 반환한다.
     *
     * @param context 실행 문맥
     * @return 문자열 기반 샘플 데이터 목록
     */
    List<String> load(JobExecutionContext context);
}
