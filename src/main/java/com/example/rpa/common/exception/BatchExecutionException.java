package com.example.rpa.common.exception;

/**
 * 배치 오케스트레이션의 최상위 실패를 나타낸다.
 */
public class BatchExecutionException extends BaseBusinessException {

    public BatchExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
