package com.example.rpa.application.dto;

import java.util.List;

/**
 * Mattermost 메시지 전송에 필요한 입력값을 표현한다.
 */
public record MattermostMessageCommand(
        String channelId,
        String message,
        List<String> fileIds
) {
}
