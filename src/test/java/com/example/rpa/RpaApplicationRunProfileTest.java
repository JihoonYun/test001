package com.example.rpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * IntelliJ 실행 전용 프로파일이 최소한의 애플리케이션 기동 조건을 만족하는지 검증한다.
 * 이 테스트는 실제 배치 수행이 아니라 컨텍스트 로딩 가능 여부만 확인한다.
 */
@SpringBootTest
@ActiveProfiles("run")
class RpaApplicationRunProfileTest {

    @Test
    void contextLoadsWithRunProfile() {
    }
}
