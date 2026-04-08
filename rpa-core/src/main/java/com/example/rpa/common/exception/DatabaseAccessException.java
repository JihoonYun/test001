package com.example.rpa.common.exception;

/**
 * 데이터베이스 조회 또는 매핑 과정에서 발생한 예외를 감싸기 위한 타입이다.
 */
public class DatabaseAccessException extends BaseBusinessException {

    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
