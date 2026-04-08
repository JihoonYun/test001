package com.example.rpa.common.exception;

/**
 * 애플리케이션 전반에서 사용하는 기본 업무 예외 클래스다.
 */
public class BaseBusinessException extends RuntimeException {

    public BaseBusinessException(String message) {
        super(message);
    }

    public BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
