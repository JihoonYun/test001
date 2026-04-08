package com.example.rpa.adapter.out.mattermost;

import com.example.rpa.application.dto.MattermostFileUploadCommand;
import com.example.rpa.application.dto.MattermostMessageCommand;
import com.example.rpa.infrastructure.properties.MattermostProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Mattermost HTTP API 호출을 담당하는 저수준 클라이언트다.
 * 현재 버전은 실제 네트워크 호출 대신 메서드 시그니처와 확장 지점만 고정한다.
 */
@Component
public class MattermostApiClient {

    @SuppressWarnings("unused")
    private final RestClient restClient;

    public MattermostApiClient(MattermostProperties mattermostProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(mattermostProperties.baseUrl())
                .defaultHeader("Authorization", "Bearer " + mattermostProperties.token())
                .build();
    }

    /**
     * 파일 업로드 API를 호출한다.
     *
     * @param command 파일 업로드 요청 정보
     * @return 업로드된 파일 ID 목록
     */
    public List<String> uploadFile(MattermostFileUploadCommand command) {
        return List.of("stub-file-id");
    }

    /**
     * 메시지 전송 API를 호출한다.
     *
     * @param command 메시지 전송 요청 정보
     */
    public void postMessage(MattermostMessageCommand command) {
        // 향후 실제 Mattermost 메시지 API 호출 로직을 추가한다.
    }
}
