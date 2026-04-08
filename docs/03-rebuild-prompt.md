# 재구현용 고정 프롬프트

아래 프롬프트는 이후 실제 개발 요청 시 가능한 한 동일한 구조의 결과물을 만들기 위한 기준 입력문이다.
필요 시 회사 패키지명, 실제 테이블명, 실제 API 스펙만 교체하고 나머지 지시는 유지한다.

## 사용 프롬프트

```text
Spring Boot 3.x, Java 17, Maven 기반의 배치형 프로그램 스켈레톤 코드를 생성해줘.
IDE는 IntelliJ 기준으로 바로 열 수 있게 표준 Maven 프로젝트 구조로 만들어줘.

다음 조건을 반드시 지켜줘.

1. 클린 아키텍처 기반으로 작성하고, 패키지 구조는 아래 원칙을 그대로 따라줘.
   - com.example.rpa
   - common, domain, application, adapter, infrastructure 계층 분리
   - adapter.in.scheduler
   - adapter.out.persistence
   - adapter.out.excel
   - adapter.out.mattermost

2. 이 프로그램은 크론 기반으로 주기 실행되는 배치다.
   - Scheduler가 시작점을 담당
   - Oracle DB를 JPA 기반으로 여러 번 조회
   - 조회 결과를 정리해서 다중 시트 엑셀 양식에 채움
   - 생성된 파일을 Mattermost 서버 API로 업로드
   - 진행 상황과 결과 요약을 Mattermost 채널에 텍스트 메시지로 전송
   - 표준화된 로그를 구조적으로 남김

3. 비즈니스 로직은 아직 비워두고, 나중에 채워넣기 쉬운 스켈레톤 코드로 작성해줘.
   - 실제 계산 로직 대신 확장 포인트와 TODO를 명확히 작성
   - public 클래스와 public 메서드에는 상세한 한국어 주석 작성
   - 단순한 TODO가 아니라 향후 어떤 로직이 들어갈지 설명하는 주석 작성

4. 반드시 생성할 주요 클래스는 아래 이름을 그대로 사용해줘.
   - RpaApplication
   - SchedulingConfig
   - JpaConfig
   - MattermostProperties
   - ExcelProperties
   - BatchProperties
   - ReportBatchScheduler
   - GenerateAndSendReportUseCase
   - GenerateAndSendReportService
   - LoadPrimaryDataPort
   - LoadSecondaryDataPort
   - LoadTertiaryDataPort
   - CreateReportExcelPort
   - UploadMattermostFilePort
   - SendMattermostMessagePort
   - ReportGenerationCommand
   - ReportPayload
   - MattermostMessageCommand
   - MattermostFileUploadCommand
   - JobExecutionResult
   - JobExecutionContext
   - SampleSourceEntity
   - SampleSourceJpaRepository
   - PrimaryDataPersistenceAdapter
   - SecondaryDataPersistenceAdapter
   - TertiaryDataPersistenceAdapter
   - ReportExcelPersistenceAdapter
   - ExcelTemplateLoader
   - WorkbookFactory
   - MultiSheetReportWriter
   - SheetCoordinateConstants
   - MattermostApiClient
   - MattermostMessageAdapter
   - MattermostFileAdapter
   - BaseBusinessException
   - DatabaseAccessException
   - ExcelGenerationException
   - MattermostIntegrationException
   - BatchExecutionException

5. 설정은 profile 별로 분리해줘.
   - application.yml
   - application-local.yml
   - application-run.yml
   - application-test.yml
   - application-real.yml
   - logback-spring.xml

6. 다음 리소스 경로를 포함해줘.
   - src/main/resources/templates/excel/report-template.xlsx

7. 구현 규칙:
   - 엔티티를 직접 상위 계층에 노출하지 말 것
   - 포트와 어댑터를 명확히 분리할 것
   - 스케줄러는 유스케이스 호출만 담당할 것
   - Mattermost 파일 업로드와 메시지 전송을 분리할 것
   - 엑셀 생성은 템플릿 기반, 다중 시트 구조로 설계할 것
   - 로그는 executionId, step, status를 포함하도록 설계할 것

8. 테스트 코드도 기본 골격을 생성해줘.
   - 유스케이스 단위 테스트
   - JPA Repository 테스트
   - Excel Writer 최소 테스트

9. 최종 결과는 컴파일 가능한 스켈레톤이어야 하고, 각 파일에는 상세 주석이 포함되어야 한다.
```

## 사용 시 추가 입력해야 할 값

다음은 실제 구현 시 함께 넘기면 좋다.

- 회사 실제 base package
- Oracle 접속 방식
- 실제 조회 대상 테이블/뷰 목록
- 엑셀 양식 파일
- Mattermost API 인증 방식
- 테스트/운영 서버별 환경 변수명

## 재현성 체크포인트

재구현 결과가 설계와 일치하는지 아래 기준으로 확인한다.

- 패키지 구조가 문서와 동일한가
- 클래스명이 고정 목록과 동일한가
- 프로파일 파일이 분리되었는가
- 스케줄러, 포트, 어댑터, 유스케이스 흐름이 분리되었는가
- 비즈니스 로직 없이도 구조와 주석만으로 흐름이 이해되는가
