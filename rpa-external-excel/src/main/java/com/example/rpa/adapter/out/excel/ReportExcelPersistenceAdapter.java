package com.example.rpa.adapter.out.excel;

import com.example.rpa.application.dto.JobExecutionContext;
import com.example.rpa.application.dto.ReportPayload;
import com.example.rpa.application.port.out.CreateReportExcelPort;
import com.example.rpa.common.exception.ExcelGenerationException;
import com.example.rpa.infrastructure.properties.ExcelProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 엑셀 생성 출력 포트 구현체다.
 */
@Component
public class ReportExcelPersistenceAdapter implements CreateReportExcelPort {

    private final ExcelTemplateLoader excelTemplateLoader;
    private final WorkbookFactory workbookFactory;
    private final MultiSheetReportWriter multiSheetReportWriter;
    private final ExcelProperties excelProperties;

    public ReportExcelPersistenceAdapter(
            ExcelTemplateLoader excelTemplateLoader,
            WorkbookFactory workbookFactory,
            MultiSheetReportWriter multiSheetReportWriter,
            ExcelProperties excelProperties
    ) {
        this.excelTemplateLoader = excelTemplateLoader;
        this.workbookFactory = workbookFactory;
        this.multiSheetReportWriter = multiSheetReportWriter;
        this.excelProperties = excelProperties;
    }

    /**
     * 리포트 페이로드를 기반으로 엑셀 파일을 생성한다.
     *
     * @param context 실행 문맥
     * @param payload 엑셀 작성 데이터
     * @return 생성된 파일 경로
     */
    @Override
    public Path create(JobExecutionContext context, ReportPayload payload) {
        try {
            excelTemplateLoader.loadTemplate();
            Files.createDirectories(Path.of(excelProperties.outputDirectory()));
            Path outputPath = Path.of(excelProperties.outputDirectory(), "report-%s.xlsx".formatted(context.executionId()));

            try (XSSFWorkbook workbook = workbookFactory.create();
                 OutputStream outputStream = Files.newOutputStream(outputPath)) {
                multiSheetReportWriter.write(workbook, payload);
                workbook.write(outputStream);
            }

            return outputPath;
        } catch (IOException exception) {
            throw new ExcelGenerationException("엑셀 파일 생성에 실패했습니다.", exception);
        }
    }
}
