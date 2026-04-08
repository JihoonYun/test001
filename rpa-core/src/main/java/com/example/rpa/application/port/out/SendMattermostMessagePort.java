package com.example.rpa.application.port.out;

import com.example.rpa.application.dto.MattermostMessageCommand;

/**
 * Mattermost 텍스트 메시지 전송 책임을 정의하는 출력 포트다.
 */
public interface SendMattermostMessagePort {

    /**
     * 실행 시작, 진행 상황, 결과 요약, 오류 알림 등을 Mattermost 채널로 전송한다.
     *
     * @param command 메시지 전송 요청 정보
     */
    void send(MattermostMessageCommand command);
}
