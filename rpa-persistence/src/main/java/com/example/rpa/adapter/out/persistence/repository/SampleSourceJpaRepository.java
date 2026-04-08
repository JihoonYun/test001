package com.example.rpa.adapter.out.persistence.repository;

import com.example.rpa.adapter.out.persistence.entity.SampleSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 샘플 엔티티에 대한 Spring Data JPA Repository다.
 */
public interface SampleSourceJpaRepository extends JpaRepository<SampleSourceEntity, Long> {

    /**
     * sourceType 값으로 데이터를 조회한다.
     *
     * @param sourceType 조회 구분값
     * @return 조건에 맞는 엔티티 목록
     */
    List<SampleSourceEntity> findBySourceType(String sourceType);
}
