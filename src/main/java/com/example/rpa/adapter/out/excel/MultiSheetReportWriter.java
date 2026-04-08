package com.example.rpa.adapter.out.excel;

import com.example.rpa.application.dto.ReportPayload;
import com.example.rpa.domain.model.ReportSection;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * 다중 시트 엑셀에 데이터를 채워 넣는 책임을 가진 클래스다.
 */
@Component
public class MultiSheetReportWriter {

    /**
     * 전달받은 페이로드를 기반으로 워크북에 시트를 만들고 데이터를 채운다.
     *
     * @param workbook 대상 워크북
     * @param payload 작성할 데이터
     */
    public void write(XSSFWorkbook workbook, ReportPayload payload) {
        createSummarySheet(workbook, payload);
        for (ReportSection section : payload.sections()) {
            Sheet sheet = workbook.createSheet(section.title());
            int rowIndex = SheetCoordinateConstants.DEFAULT_START_ROW;
            for (String rowValue : section.rows()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rowValue);
            }
        }
    }

    private void createSummarySheet(XSSFWorkbook workbook, ReportPayload payload) {
        Sheet sheet = workbook.createSheet(SheetCoordinateConstants.SUMMARY_SHEET);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Generated Sections");
        Row content = sheet.createRow(1);
        content.createCell(0).setCellValue(payload.sections().size());
    }
}
