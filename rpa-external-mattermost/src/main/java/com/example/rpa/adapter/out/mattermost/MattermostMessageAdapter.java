package com.example.rpa.adapter.out.mattermost;

import com.example.rpa.application.dto.MattermostMessageCommand;
import com.example.rpa.application.port.out.SendMattermostMessagePort;
import com.example.rpa.common.exception.MattermostIntegrationException;
import org.springframework.stereotype.Component;

/**
 * Mattermost 메시지 전송 포트 구현체다.
 */
@Component
public class MattermostMessageAdapter implements SendMattermostMessagePort {

    private final MattermostApiClient mattermostApiClient;

    public MattermostMessageAdapter(MattermostApiClient mattermostApiClient) {
        this.mattermostApiClient = mattermostApiClient;
    }

    /**
     * 텍스트 기반 알림 메시지를 Mattermost 채널로 전송한다.
     *
     * @param command 메시지 전송 요청 정보
     */
    @Override
    public void send(MattermostMessageCommand command) {
        try {
            mattermostApiClient.postMessage(command);
        } catch (Exception exception) {
            throw new MattermostIntegrationException("Mattermost 메시지 전송에 실패했습니다.", exception);
        }
    }
}
