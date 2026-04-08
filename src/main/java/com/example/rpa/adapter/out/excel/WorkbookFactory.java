package com.example.rpa.adapter.out.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * Apache POI Workbook 생성 책임을 분리한 팩토리다.
 */
@Component
public class WorkbookFactory {

    /**
     * 비어 있는 XLSX 워크북을 생성한다.
     *
     * @return 새 워크북
     */
    public XSSFWorkbook create() {
        return new XSSFWorkbook();
    }
}
