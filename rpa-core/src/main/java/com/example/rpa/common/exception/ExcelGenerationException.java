package com.example.rpa.common.exception;

/**
 * 엑셀 템플릿 로드, 시트 작성, 파일 저장 단계에서 발생하는 예외를 표현한다.
 */
public class ExcelGenerationException extends BaseBusinessException {

    public ExcelGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
