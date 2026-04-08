package com.example.rpa.application.dto;

import java.nio.file.Path;

/**
 * Mattermost 파일 업로드 요청 정보를 담는 DTO다.
 */
public record MattermostFileUploadCommand(
        String channelId,
        Path filePath
) {
}
