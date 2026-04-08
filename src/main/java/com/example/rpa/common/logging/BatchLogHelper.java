package com.example.rpa.common.logging;

import com.example.rpa.common.constant.LoggingConstants;

/**
 * 구조화 로그 메시지를 일관된 문자열 형태로 만드는 보조 클래스다.
 */
public final class BatchLogHelper {

    private BatchLogHelper() {
    }

    public static String format(String step, String status, String message) {
        return "%s step=%s status=%s message=%s".formatted(LoggingConstants.LOG_PREFIX, step, status, message);
    }
}
