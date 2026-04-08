package com.example.rpa.adapter.in.scheduler;

import com.example.rpa.application.dto.ReportGenerationCommand;
import com.example.rpa.application.port.in.GenerateAndSendReportUseCase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 크론 기반 배치 실행의 진입점이다.
 * 이 클래스는 스케줄 조건만 담당하며, 실제 처리 흐름은 유스케이스로 위임한다.
 */
@Component
@ConditionalOnProperty(name = "rpa.batch.scheduler-enabled", havingValue = "true", matchIfMissing = true)
public class ReportBatchScheduler {

    private final GenerateAndSendReportUseCase generateAndSendReportUseCase;

    public ReportBatchScheduler(GenerateAndSendReportUseCase generateAndSendReportUseCase) {
        this.generateAndSendReportUseCase = generateAndSendReportUseCase;
    }

    /**
     * 외부 설정에 정의된 크론 표현식에 따라 배치를 시작한다.
     */
    @Scheduled(cron = "${rpa.batch.cron}")
    public void schedule() {
        generateAndSendReportUseCase.execute(new ReportGenerationCommand(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                "SCHEDULED"
        ));
    }
}
