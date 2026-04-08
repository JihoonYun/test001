package com.example.rpa.adapter.out.excel;

import com.example.rpa.infrastructure.properties.ExcelProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * 엑셀 템플릿 리소스 조회를 담당한다.
 */
@Component
public class ExcelTemplateLoader {

    private final ResourceLoader resourceLoader;
    private final ExcelProperties excelProperties;

    public ExcelTemplateLoader(ResourceLoader resourceLoader, ExcelProperties excelProperties) {
        this.resourceLoader = resourceLoader;
        this.excelProperties = excelProperties;
    }

    /**
     * 설정된 템플릿 리소스를 반환한다.
     *
     * @return 템플릿 리소스
     */
    public Resource loadTemplate() {
        return resourceLoader.getResource(excelProperties.templatePath());
    }
}
