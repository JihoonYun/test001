package com.example.rpa.application.port.out;

import com.example.rpa.application.dto.MattermostFileUploadCommand;

import java.util.List;

/**
 * Mattermost 파일 업로드 책임을 정의하는 출력 포트다.
 */
public interface UploadMattermostFilePort {

    /**
     * 생성된 파일을 Mattermost에 업로드하고 파일 식별자 목록을 반환한다.
     *
     * @param command 파일 업로드 요청 정보
     * @return 업로드된 파일 ID 목록
     */
    List<String> upload(MattermostFileUploadCommand command);
}
