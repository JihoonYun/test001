package com.example.rpa.adapter.out.mattermost;

import com.example.rpa.application.dto.MattermostFileUploadCommand;
import com.example.rpa.application.port.out.UploadMattermostFilePort;
import com.example.rpa.common.exception.MattermostIntegrationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mattermost 파일 업로드 포트 구현체다.
 */
@Component
public class MattermostFileAdapter implements UploadMattermostFilePort {

    private final MattermostApiClient mattermostApiClient;

    public MattermostFileAdapter(MattermostApiClient mattermostApiClient) {
        this.mattermostApiClient = mattermostApiClient;
    }

    /**
     * 생성된 리포트 파일을 Mattermost에 업로드한다.
     *
     * @param command 파일 업로드 요청 정보
     * @return 업로드된 파일 ID 목록
     */
    @Override
    public List<String> upload(MattermostFileUploadCommand command) {
        try {
            return mattermostApiClient.uploadFile(command);
        } catch (Exception exception) {
            throw new MattermostIntegrationException("Mattermost 파일 업로드에 실패했습니다.", exception);
        }
    }
}
