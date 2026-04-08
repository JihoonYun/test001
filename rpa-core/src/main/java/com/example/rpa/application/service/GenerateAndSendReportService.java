package com.example.rpa.application.service;

import com.example.rpa.application.dto.JobExecutionContext;
import com.example.rpa.application.dto.JobExecutionResult;
import com.example.rpa.application.dto.MattermostFileUploadCommand;
import com.example.rpa.application.dto.MattermostMessageCommand;
import com.example.rpa.application.dto.ReportGenerationCommand;
import com.example.rpa.application.dto.ReportPayload;
import com.example.rpa.application.port.in.GenerateAndSendReportUseCase;
import com.example.rpa.application.port.out.CreateReportExcelPort;
import com.example.rpa.application.port.out.LoadPrimaryDataPort;
import com.example.rpa.application.port.out.LoadSecondaryDataPort;
import com.example.rpa.application.port.out.LoadTertiaryDataPort;
import com.example.rpa.application.port.out.SendMattermostMessagePort;
import com.example.rpa.application.port.out.UploadMattermostFilePort;
import com.example.rpa.common.constant.LoggingConstants;
import com.example.rpa.common.exception.BatchExecutionException;
import com.example.rpa.common.logging.BatchLogHelper;
import com.example.rpa.domain.model.ReportSection;
import com.example.rpa.infrastructure.properties.MattermostProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 배치 실행의 전체 오케스트레이션을 담당한다.
 * 여러 데이터 조회 결과를 취합한 뒤 엑셀 생성과 외부 알림 전송을 순차적으로 수행한다.
 *
 * 현재 버전은 스켈레톤 구현이므로 실제 데이터 매핑 규칙은 포함하지 않고,
 * 향후 비즈니스 로직이 삽입될 수 있는 명확한 확장 지점만 제공한다.
 */
@Service
public class GenerateAndSendReportService implements GenerateAndSendReportUseCase {

    private static final Logger log = LoggerFactory.getLogger(GenerateAndSendReportService.class);

    private final LoadPrimaryDataPort loadPrimaryDataPort;
    private final LoadSecondaryDataPort loadSecondaryDataPort;
    private final LoadTertiaryDataPort loadTertiaryDataPort;
    private final CreateReportExcelPort createReportExcelPort;
    private final UploadMattermostFilePort uploadMattermostFilePort;
    private final SendMattermostMessagePort sendMattermostMessagePort;
    private final MattermostProperties mattermostProperties;

    public GenerateAndSendReportService(
            LoadPrimaryDataPort loadPrimaryDataPort,
            LoadSecondaryDataPort loadSecondaryDataPort,
            LoadTertiaryDataPort loadTertiaryDataPort,
            CreateReportExcelPort createReportExcelPort,
            UploadMattermostFilePort uploadMattermostFilePort,
            SendMattermostMessagePort sendMattermostMessagePort,
            MattermostProperties mattermostProperties
    ) {
        this.loadPrimaryDataPort = loadPrimaryDataPort;
        this.loadSecondaryDataPort = loadSecondaryDataPort;
        this.loadTertiaryDataPort = loadTertiaryDataPort;
        this.createReportExcelPort = createReportExcelPort;
        this.uploadMattermostFilePort = uploadMattermostFilePort;
        this.sendMattermostMessagePort = sendMattermostMessagePort;
        this.mattermostProperties = mattermostProperties;
    }

    /**
     * 전체 배치 실행 흐름을 수행한다.
     * 실제 운영 시점에는 각 단계에 비즈니스 규칙과 상세 검증, 예외 정책이 추가될 예정이다.
     *
     * @param command 실행 요청 정보
     * @return 실행 결과 요약
     */
    @Override
    public JobExecutionResult execute(ReportGenerationCommand command) {
        JobExecutionContext context = new JobExecutionContext(
                command.executionId(),
                LocalDateTime.now(),
                LoggingConstants.JOB_NAME
        );

        try {
            log.info(BatchLogHelper.format("START", "RUNNING", "Batch execution started"));
            notifyStart(context);

            List<String> primaryData = loadPrimaryDataPort.load(context);
            List<String> secondaryData = loadSecondaryDataPort.load(context);
            List<String> tertiaryData = loadTertiaryDataPort.load(context);

            ReportPayload payload = new ReportPayload(List.of(
                    new ReportSection("Primary Data", primaryData),
                    new ReportSection("Secondary Data", secondaryData),
                    new ReportSection("Tertiary Data", tertiaryData)
            ));

            Path generatedFile = createReportExcelPort.create(context, payload);
            List<String> fileIds = uploadMattermostFilePort.upload(
                    new MattermostFileUploadCommand(mattermostProperties.channelId(), generatedFile)
            );

            sendMattermostMessagePort.send(new MattermostMessageCommand(
                    mattermostProperties.channelId(),
                    "배치 실행이 완료되었습니다. executionId=%s".formatted(context.executionId()),
                    fileIds
            ));

            log.info(BatchLogHelper.format("FINISH", "SUCCESS", "Batch execution finished successfully"));
            return new JobExecutionResult(true, context.executionId(), "배치 실행이 성공적으로 완료되었습니다.", generatedFile);
        } catch (Exception exception) {
            log.error(BatchLogHelper.format("FINISH", "FAILED", "Batch execution failed"), exception);
            safelyNotifyFailure(context, exception);
            throw new BatchExecutionException("배치 실행 중 오류가 발생했습니다.", exception);
        }
    }

    private void notifyStart(JobExecutionContext context) {
        sendMattermostMessagePort.send(new MattermostMessageCommand(
                mattermostProperties.channelId(),
                "배치 실행을 시작합니다. executionId=%s".formatted(context.executionId()),
                List.of()
        ));
    }

    private void safelyNotifyFailure(JobExecutionContext context, Exception exception) {
        try {
            sendMattermostMessagePort.send(new MattermostMessageCommand(
                    mattermostProperties.channelId(),
                    "배치 실행이 실패했습니다. executionId=%s, reason=%s".formatted(context.executionId(), exception.getMessage()),
                    List.of()
            ));
        } catch (Exception notifyException) {
            log.warn(BatchLogHelper.format("NOTIFY", "FAILED", "Failure notification also failed"), notifyException);
        }
    }
}
