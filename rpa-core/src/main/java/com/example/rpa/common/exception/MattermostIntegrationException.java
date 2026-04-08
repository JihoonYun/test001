package com.example.rpa.common.exception;

/**
 * Mattermost API 호출 과정에서 발생한 연동 예외를 의미한다.
 */
public class MattermostIntegrationException extends BaseBusinessException {

    public MattermostIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
