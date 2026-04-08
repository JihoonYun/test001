package com.example.rpa.adapter.out.excel;

import com.example.rpa.application.dto.ReportPayload;
import com.example.rpa.domain.model.ReportSection;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MultiSheetReportWriterTest {

    @Test
    void writeShouldCreateMultipleSheets() throws Exception {
        MultiSheetReportWriter writer = new MultiSheetReportWriter();
        ReportPayload payload = new ReportPayload(List.of(
                new ReportSection("Primary Data", List.of("a", "b")),
                new ReportSection("Secondary Data", List.of("c"))
        ));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            writer.write(workbook, payload);

            assertThat(workbook.getNumberOfSheets()).isEqualTo(3);
            assertThat(workbook.getSheet("Summary")).isNotNull();
            assertThat(workbook.getSheet("Primary Data")).isNotNull();
            assertThat(workbook.getSheet("Secondary Data")).isNotNull();
        }
    }
}
