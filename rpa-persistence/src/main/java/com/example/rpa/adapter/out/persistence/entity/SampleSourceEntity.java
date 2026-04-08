package com.example.rpa.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Oracle 조회 대상의 예시 엔티티다.
 * 실제 테이블이 확정되기 전까지는 JPA 매핑 구조와 Repository 테스트 가능 상태를 유지하기 위한 최소 모델만 둔다.
 */
@Entity
@Table(name = "SAMPLE_SOURCE")
public class SampleSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SOURCE_TYPE", nullable = false)
    private String sourceType;

    @Column(name = "SOURCE_VALUE", nullable = false)
    private String sourceValue;

    protected SampleSourceEntity() {
    }

    public SampleSourceEntity(String sourceType, String sourceValue) {
        this.sourceType = sourceType;
        this.sourceValue = sourceValue;
    }

    public Long getId() {
        return id;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSourceValue() {
        return sourceValue;
    }
}
