package com.example.rpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 애플리케이션의 시작점이다.
 * 이 클래스는 스프링부트 자동 설정을 활성화하고,
 * 환경 설정 프로퍼티 바인딩 클래스들을 스캔하도록 구성한다.
 *
 * 현재 프로젝트는 배치 스켈레톤이므로 실행 자체보다
 * 프로젝트 구조와 확장 가능한 진입점 제공에 목적이 있다.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class RpaApplication {

    /**
     * 스프링부트 애플리케이션을 시작한다.
     *
     * @param args 실행 인자
     */
    public static void main(String[] args) {
        SpringApplication.run(RpaApplication.class, args);
    }
}
