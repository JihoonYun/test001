package com.example.rpa.application.service;

import com.example.rpa.application.dto.JobExecutionContext;
import com.example.rpa.application.dto.JobExecutionResult;
import com.example.rpa.application.dto.MattermostMessageCommand;
import com.example.rpa.application.dto.ReportGenerationCommand;
import com.example.rpa.application.dto.ReportPayload;
import com.example.rpa.application.port.out.CreateReportExcelPort;
import com.example.rpa.application.port.out.SendMattermostMessagePort;
import com.example.rpa.infrastructure.properties.MattermostProperties;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateAndSendReportServiceTest {

    @Test
    void executeShouldCreateExcelAndSendMattermostNotifications() {
        RecordingMessagePort messagePort = new RecordingMessagePort();
        GenerateAndSendReportService service = new GenerateAndSendReportService(
                context -> List.of("p1", "p2"),
                context -> List.of("s1"),
                context -> List.of("t1"),
                new StubExcelPort(),
                command -> List.of("file-1"),
                messagePort,
                new MattermostProperties("http://localhost:8065", "token", "channel-1")
        );

        JobExecutionResult result = service.execute(new ReportGenerationCommand(
                "execution-1",
                LocalDateTime.now()
        ));

        assertThat(result.success()).isTrue();
        assertThat(result.generatedFile()).isEqualTo(Path.of("build", "report.xlsx"));
        assertThat(messagePort.messages).hasSize(2);
    }

    private static class StubExcelPort implements CreateReportExcelPort {
        @Override
        public Path create(JobExecutionContext context, ReportPayload payload) {
            return Path.of("build", "report.xlsx");
        }
    }

    private static class RecordingMessagePort implements SendMattermostMessagePort {
        private final List<MattermostMessageCommand> messages = new ArrayList<>();

        @Override
        public void send(MattermostMessageCommand command) {
            messages.add(command);
        }
    }
}
