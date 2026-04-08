package com.example.rpa.adapter.out.persistence;

import com.example.rpa.adapter.out.persistence.repository.SampleSourceJpaRepository;
import com.example.rpa.application.dto.JobExecutionContext;
import com.example.rpa.application.port.out.LoadPrimaryDataPort;
import com.example.rpa.common.exception.DatabaseAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 1차 데이터 조회 포트 구현체다.
 */
@Component
public class PrimaryDataPersistenceAdapter implements LoadPrimaryDataPort {

    private final SampleSourceJpaRepository repository;

    public PrimaryDataPersistenceAdapter(SampleSourceJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * sourceType이 PRIMARY인 데이터를 조회한다.
     *
     * @param context 실행 문맥
     * @return 엑셀 반영용 샘플 문자열 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> load(JobExecutionContext context) {
        try {
            return repository.findBySourceType("PRIMARY")
                    .stream()
                    .map(entity -> entity.getSourceValue() + " | executionId=" + context.executionId())
                    .toList();
        } catch (Exception exception) {
            throw new DatabaseAccessException("1차 데이터 조회에 실패했습니다.", exception);
        }
    }
}
