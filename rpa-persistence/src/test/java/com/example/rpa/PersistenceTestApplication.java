package com.example.rpa;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * persistence 모듈의 JPA 테스트를 위한 최소 부트스트랩 클래스다.
 * 멀티 모듈 구조에서는 실행 모듈과 영속성 모듈이 분리되므로,
 * JPA 슬라이스 테스트가 독립적으로 설정 클래스를 찾을 수 있도록 별도 테스트 진입점을 둔다.
 */
@SpringBootApplication(scanBasePackages = "com.example.rpa")
public class PersistenceTestApplication {
}
