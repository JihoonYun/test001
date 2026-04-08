package com.example.rpa.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 공통 설정을 담는 클래스다.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
