# 구현 규칙 및 스캐폴딩 기준

## 1. 목표

이 문서는 이후 실제 코드를 생성할 때 결과물이 흔들리지 않도록 프로젝트 생성 규칙을 고정한다.
핵심은 "같은 요청이면 같은 구조가 나오게 하는 것"이다.

## 2. 프로젝트 식별자 기준

- GroupId: `com.example`
- ArtifactId: `rpa-batch`
- Name: `rpa-batch`
- Base Package: `com.example.rpa`
- Java Version: `17`
- Spring Boot Version: `3.x`

실제 회사 패키지명이 정해지면 위 값만 교체하고 나머지 구조는 유지한다.

## 3. Maven 의존성 기준

반드시 포함:

- `spring-boot-starter`
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`
- `lombok`
- `poi-ooxml`
- `oracle database jdbc driver`

선택 포함:

- `spring-boot-starter-webflux` 또는 Spring Framework `RestClient` 사용 의존성
- `logstash-logback-encoder`

## 4. 반드시 생성할 패키지와 클래스

### 4.1 진입점

- `RpaApplication`

### 4.2 설정

- `SchedulingConfig`
- `JpaConfig`
- `MattermostProperties`
- `ExcelProperties`
- `BatchProperties`
- `LoggingConstants`

### 4.3 스케줄러

- `ReportBatchScheduler`

### 4.4 애플리케이션 포트

- `GenerateAndSendReportUseCase`
- `LoadPrimaryDataPort`
- `LoadSecondaryDataPort`
- `LoadTertiaryDataPort`
- `CreateReportExcelPort`
- `UploadMattermostFilePort`
- `SendMattermostMessagePort`

### 4.5 유스케이스 서비스

- `GenerateAndSendReportService`

### 4.6 DTO

- `ReportGenerationCommand`
- `ReportPayload`
- `MattermostMessageCommand`
- `MattermostFileUploadCommand`
- `JobExecutionResult`
- `JobExecutionContext`

### 4.7 영속성

- `SampleSourceEntity`
- `SampleSourceJpaRepository`
- `PrimaryDataPersistenceAdapter`
- `SecondaryDataPersistenceAdapter`
- `TertiaryDataPersistenceAdapter`

### 4.8 엑셀

- `ReportExcelPersistenceAdapter`
- `ExcelTemplateLoader`
- `WorkbookFactory`
- `MultiSheetReportWriter`
- `SheetCoordinateConstants`

### 4.9 Mattermost

- `MattermostApiClient`
- `MattermostMessageAdapter`
- `MattermostFileAdapter`

### 4.10 예외

- `BaseBusinessException`
- `DatabaseAccessException`
- `ExcelGenerationException`
- `MattermostIntegrationException`
- `BatchExecutionException`

## 5. 클래스 작성 규칙

- 모든 public 클래스 상단에 "역할", "책임", "향후 확장 포인트"를 설명하는 주석을 작성한다.
- public 메서드에는 입력값, 처리 흐름, 반환값, 예외 가능성을 설명하는 주석을 작성한다.
- 구현이 비어 있는 지점은 단순 `TODO` 한 줄이 아니라 왜 비어 있는지와 무엇이 들어갈지 설명한다.
- 유틸 클래스 남용을 피하고, 의미 있는 도메인/서비스 객체로 분리한다.
- 엔티티와 외부 API 응답 모델을 애플리케이션 계층 DTO로 직접 노출하지 않는다.

## 6. 주석 스타일 기준

주석은 상세하게 작성하되, 다음 원칙을 따른다.

- 업무 담당자가 처음 봐도 흐름을 이해할 수 있어야 한다.
- 프레임워크 동작 이유와 구조적 의도를 설명해야 한다.
- 코드가 하는 일을 그대로 반복하는 주석은 피한다.

권장 예시:

```java
/**
 * 배치 실행의 전체 오케스트레이션을 담당한다.
 * 이 클래스는 스케줄러 진입점으로부터 호출되며,
 * 여러 데이터 조회 결과를 취합한 뒤 엑셀 생성과 외부 알림 전송을 순차적으로 수행한다.
 *
 * 현재 버전은 스켈레톤 구현이므로 실제 데이터 매핑 규칙은 포함하지 않고,
 * 향후 비즈니스 로직이 삽입될 수 있는 명확한 확장 지점만 제공한다.
 */
```

## 7. 설정 파일 기준

`src/main/resources` 아래에 다음 파일을 둔다.

- `application.yml`
- `application-local.yml`
- `application-run.yml`
- `application-test.yml`
- `application-real.yml`
- `logback-spring.xml`

`application.yml` 에는 공통 키 구조만 두고 민감한 값은 profile 파일 또는 환경변수 참조로 분리한다.

예시 키 구조:

```yaml
spring:
  application:
    name: rpa-batch

rpa:
  batch:
    job-name: reportBatchJob
    cron: "0 0/30 * * * *"
  excel:
    template-path: classpath:templates/excel/report-template.xlsx
    output-directory: ./output
  mattermost:
    base-url: ${MATTERMOST_BASE_URL}
    token: ${MATTERMOST_TOKEN}
    channel-id: ${MATTERMOST_CHANNEL_ID}
```

## 8. 디렉터리 기준

```text
src
├─ main
│  ├─ java/com/example/rpa
│  └─ resources
│     ├─ application.yml
│     ├─ application-local.yml
│     ├─ application-run.yml
│     ├─ application-test.yml
│     ├─ application-real.yml
│     ├─ logback-spring.xml
│     └─ templates/excel/report-template.xlsx
└─ test
   └─ java/com/example/rpa
```

## 9. 로깅 메시지 기준

로그 메시지 형식은 가능한 한 일정하게 유지한다.

예시:

- `[REPORT_BATCH] Batch started. executionId={}, profile={}`
- `[REPORT_BATCH] Data loaded. executionId={}, dataset={}, size={}`
- `[REPORT_BATCH] Excel generated. executionId={}, path={}`
- `[REPORT_BATCH] Mattermost file uploaded. executionId={}, fileId={}`
- `[REPORT_BATCH] Batch finished. executionId={}, success={}, elapsedMs={}`

## 10. 네이밍 규칙

- 유스케이스는 `...UseCase`
- 서비스 구현은 `...Service`
- 출력 포트 구현체는 `...Adapter`
- 외부 HTTP 호출 전용 클래스는 `...Client`
- 설정 바인딩 클래스는 `...Properties`
- 예외는 `...Exception`
- 상수 클래스는 `...Constants`

## 11. 재현성 확보 규칙

동일한 코드 재생성을 위해 다음 항목을 바꾸지 않는다.

- 기본 패키지 구조
- 클래스명
- 포트/어댑터 분리 방식
- 프로파일 파일명
- 엑셀/메신저/DB를 각각 개별 어댑터로 분리하는 원칙
- 주석 스타일

## 12. 이후 실제 생성 순서

1. Maven 기반 Spring Boot 프로젝트 생성
2. 패키지 및 빈 클래스 전체 생성
3. 설정 파일과 프로파일 파일 생성
4. 포트와 어댑터 인터페이스/구현체 생성
5. 스케줄러와 유스케이스 연결
6. 테스트용 기본 클래스 생성
7. 템플릿 리소스 위치와 로그 설정 반영

## 13. 첫 구현 완료 기준

다음 조건을 만족하면 1차 스켈레톤 완료로 본다.

- 전체 프로젝트가 컴파일 가능하다.
- 배치 진입점이 존재한다.
- DB 조회 포트가 3개 이상 분리되어 있다.
- 엑셀 다중 시트 생성용 골격이 존재한다.
- Mattermost 파일 업로드/메시지 전송 골격이 존재한다.
- 환경별 설정 파일이 분리되어 있다.
- 핵심 클래스에 상세 주석이 작성되어 있다.
