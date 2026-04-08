package com.example.rpa.adapter.out.persistence.repository;

import com.example.rpa.adapter.out.persistence.entity.SampleSourceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=Oracle;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SampleSourceJpaRepositoryTest {

    @Autowired
    private SampleSourceJpaRepository repository;

    @Test
    void findBySourceTypeShouldReturnMatchingRows() {
        repository.saveAll(List.of(
                new SampleSourceEntity("PRIMARY", "value-1"),
                new SampleSourceEntity("SECONDARY", "value-2")
        ));

        List<SampleSourceEntity> result = repository.findBySourceType("PRIMARY");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSourceValue()).isEqualTo("value-1");
    }
}
