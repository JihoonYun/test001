# 배치형 엑셀 생성 및 Mattermost 전송 시스템 설계서

## 1. 목적

이 문서는 스프링부트 + 메이븐 기반의 주기 실행 프로그램을 구현하기 전에 아키텍처와 설계 기준을 고정하기 위한 문서다.
목표는 다음과 같다.

- 비즈니스 로직이 채워지기 전에도 실행 흐름이 이해되는 스켈레톤 코드 구조를 만든다.
- 이후 다시 구현 요청을 하더라도 동일한 구조와 패키지 체계가 재현되도록 기준을 명확히 남긴다.
- 테스트 서버와 운영 서버에 쉽게 배포할 수 있도록 환경 분리 기준을 설계 단계에서 확정한다.

## 2. 시스템 개요

프로그램은 크론 기반으로 주기 실행되는 배치 애플리케이션이다.
실행 시 다음 순서로 동작한다.

1. 스케줄러가 배치 작업을 시작한다.
2. Oracle DB에서 JPA 기반으로 여러 종류의 데이터를 조회한다.
3. 조회한 데이터를 배치 내부 DTO 또는 도메인 모델로 정리한다.
4. 정해진 다중 시트 엑셀 양식에 데이터를 매핑한다.
5. 생성된 엑셀 파일을 Mattermost 서버로 API 전송한다.
6. 진행 상황, 성공/실패 여부, 결과 요약을 Mattermost 채널에 텍스트 메시지로 전송한다.
7. 전 과정의 로그를 표준화된 형식으로 남긴다.

## 3. 아키텍처 스타일

클린 아키텍처를 기준으로 다음 원칙을 적용한다.

- 바깥 계층은 안쪽 계층에 의존할 수 있지만, 안쪽 계층은 바깥 계층을 몰라야 한다.
- 비즈니스 흐름은 유스케이스 중심으로 읽혀야 한다.
- 외부 시스템 연동은 모두 포트와 어댑터로 분리한다.
- JPA, Excel, Mattermost, Scheduler는 구현 세부사항으로 취급한다.
- 중복 로직은 공통 모듈 또는 공통 유틸로 격리하되, 무분별한 범용화는 피한다.

## 4. 권장 패키지 구조

단일 모듈 Maven 프로젝트를 기본안으로 사용한다.
초기 단계에서는 멀티 모듈보다 단일 모듈이 스켈레톤 관리와 이해 측면에서 유리하다.

```text
com.example.rpa
├─ RpaApplication
├─ common
│  ├─ annotation
│  ├─ config
│  ├─ constant
│  ├─ exception
│  ├─ logging
│  ├─ util
│  └─ support
├─ domain
│  ├─ model
│  ├─ value
│  └─ service
├─ application
│  ├─ port
│  │  ├─ in
│  │  └─ out
│  ├─ usecase
│  ├─ dto
│  └─ mapper
├─ adapter
│  ├─ in
│  │  └─ scheduler
│  └─ out
│     ├─ persistence
│     │  ├─ entity
│     │  ├─ repository
│     │  └─ mapper
│     ├─ excel
│     └─ mattermost
└─ infrastructure
   ├─ batch
   ├─ client
   ├─ properties
   └─ logging
```

## 5. 계층별 책임

### 5.1 domain

- 업무 개념을 표현하는 모델을 둔다.
- 프레임워크 의존 코드를 두지 않는다.
- 아직 비즈니스 규칙이 비어 있어도 의미 있는 타입 이름과 주석을 미리 정의한다.

### 5.2 application

- 배치 실행 유스케이스를 정의한다.
- 입력 포트와 출력 포트를 선언한다.
- 여러 조회 결과를 조합하는 오케스트레이션을 담당한다.
- 트랜잭션 경계와 처리 순서를 가장 잘 읽히는 위치에 둔다.

### 5.3 adapter.in

- 내부 스케줄러는 두지 않는다.
- 외부 배치 스케줄러(JobPass)가 프로그램 실행을 담당한다고 가정한다.
- 애플리케이션 내부에서는 실행 요청을 받아 유스케이스를 호출하는 진입 구조만 제공한다.

### 5.4 adapter.out.persistence

- JPA 엔티티와 Spring Data JPA Repository를 둔다.
- 애플리케이션 계층의 출력 포트를 구현한다.
- 엔티티를 바로 상위 계층으로 노출하지 않고 매퍼로 변환한다.

### 5.5 adapter.out.excel

- 정해진 템플릿 엑셀 파일을 읽고 시트별로 데이터를 채운다.
- Apache POI 기반 구현을 권장한다.
- 시트명, 시작 행, 셀 좌표 등 양식 정보는 상수 또는 별도 설정 클래스로 관리한다.

### 5.6 adapter.out.mattermost

- 파일 업로드 API와 메시지 전송 API를 분리 구현한다.
- 인증 토큰, 서버 URL, 채널 ID는 환경설정으로 분리한다.
- 외부 호출 실패 시 재시도 여부는 정책 클래스로 분리할 수 있게 설계한다.

## 6. 핵심 유스케이스 설계

대표 유스케이스 이름은 다음처럼 고정한다.

- `GenerateAndSendReportUseCase`
- `GenerateAndSendReportService`

세부 단계는 아래 흐름을 따른다.

1. 실행 컨텍스트 생성
2. 실행 시작 로그 기록
3. DB 조회 1..N 수행
4. 조회 결과 집계 및 엑셀 작성용 데이터 모델 생성
5. 엑셀 파일 생성
6. Mattermost 파일 업로드
7. Mattermost 텍스트 메시지 전송
8. 성공/실패 결과 요약 로그 기록

실패 처리 원칙은 다음과 같다.

- DB 조회 실패, 엑셀 생성 실패, 파일 업로드 실패, 메시지 전송 실패를 구분된 예외 타입으로 관리한다.
- 최상위 유스케이스에서 예외를 잡아 실행 결과를 표준 응답 객체로 정리한다.
- 실패해도 가능한 범위에서 Mattermost 오류 메시지를 남길 수 있도록 별도 안전 전송 경로를 둔다.

## 7. 데이터 접근 설계

- DB별 업무 조회는 각각 독립된 출력 포트로 추상화한다.
- 예시:
  - `LoadOrderDataPort`
  - `LoadSettlementDataPort`
  - `LoadCustomerDataPort`
- 여러 데이터를 한 리포트에서 사용할 경우, 유스케이스에서 조합한다.
- 조회성 배치이므로 기본적으로 read-only 트랜잭션을 사용한다.
- 대량 데이터 가능성이 있으면 페이징 또는 스트리밍 전략을 추후 확장 가능하도록 인터페이스를 설계한다.

## 8. 엑셀 설계

- 템플릿 기반 생성 방식을 기본으로 한다.
- 템플릿 파일은 `src/main/resources/templates/excel` 아래에 둔다.
- 각 시트의 역할과 매핑 클래스는 분리한다.

권장 구성:

- `ExcelTemplateLoader`
- `ExcelWorkbookFactory`
- `ExcelSheetWriter`
- `ReportExcelWriter`
- `SheetPopulationService`

시트별 책임 예시:

- 요약 시트
- 상세 목록 시트
- 통계 시트

엑셀 구조 변경 가능성에 대비해 셀 좌표를 하드코딩하지 않고 별도 상수 클래스로 분리한다.

## 9. Mattermost 연동 설계

출력 포트는 최소 두 개로 분리한다.

- `SendMattermostMessagePort`
- `UploadMattermostFilePort`

추가로 조합 포트를 둘 수 있다.

- `NotifyMattermostPort`

메시지 종류는 다음을 기준으로 나눈다.

- 실행 시작 알림
- 진행 상황 알림
- 결과 요약 알림
- 오류 알림

파일 업로드 후 반환되는 파일 ID를 메시지 전송에 연결할 수 있도록 응답 DTO를 둔다.

## 10. 실행 설계

- 애플리케이션 내부에서는 스케줄을 관리하지 않는다.
- JobPass가 실행 시점과 주기를 관리한다.
- 애플리케이션은 한 번 실행되면 한 번의 배치 업무만 수행하도록 단순하게 유지한다.
- 중복 실행 방지 필요 여부는 JobPass 정책 또는 외부 운영 제어 기준으로 판단한다.

## 11. 로깅 표준

로그는 JSON 또는 키-값 기반 구조화 로그를 권장한다.
최소 포함 필드:

- `timestamp`
- `level`
- `service`
- `profile`
- `jobName`
- `executionId`
- `step`
- `status`
- `message`
- `elapsedMs`
- `errorCode`

로그 정책:

- 시작 로그, 단계 로그, 종료 로그를 반드시 남긴다.
- 예외는 비즈니스 예외와 시스템 예외를 구분한다.
- 개인 정보 또는 민감 정보는 마스킹한다.

## 12. 환경 분리 설계

Spring Profile 기준:

- `local`
- `run`
- `test`
- `real`

설정 파일 예시:

- `application.yml`
- `application-local.yml`
- `application-run.yml`
- `application-test.yml`
- `application-real.yml`

분리 대상:

- Oracle 접속 정보
- 크론 표현식
- Mattermost URL, 토큰, 채널 ID
- 로그 레벨
- 파일 출력 경로
- 템플릿 경로 오버라이드 여부

민감 정보는 yml 하드코딩 대신 환경변수 또는 외부 시크릿 주입을 우선한다.

## 13. 예외 및 결과 모델

표준 실행 결과 모델을 둔다.

- `JobExecutionResult`
- `JobStepResult`
- `NotificationResult`
- `FileCreationResult`

예외 계층 예시:

- `BusinessException`
- `ExternalSystemException`
- `DatabaseAccessException`
- `ExcelGenerationException`
- `MattermostIntegrationException`
- `BatchExecutionException`

## 14. 테스트 전략

초기 스켈레톤 기준 테스트는 다음 수준까지 구현한다.

- 유스케이스 단위 테스트
- 포트 목킹 기반 서비스 테스트
- JPA Repository 슬라이스 테스트
- Excel Writer 최소 생성 테스트
- Mattermost Client 직렬화 테스트

운영 전 단계에서 추가 가능한 테스트:

- Oracle 연동 통합 테스트
- Mattermost API 통합 테스트
- 배치 전체 흐름 통합 테스트

## 15. 향후 구현 시 제외할 것

스켈레톤 1차 버전에서는 다음을 넣지 않는다.

- 실제 업무 규칙 계산 로직
- 복잡한 데이터 변환 규칙
- 실운영 재시도/보상 트랜잭션
- 관리자 화면
- 다중 배치 종류 동시 지원

## 16. 추천 기술 스택

- Java 17
- Spring Boot 3.x
- Maven
- Spring Data JPA
- Oracle JDBC Driver
- Apache POI
- Spring WebClient 또는 RestClient
- Lombok
- Logback

## 17. 산출물 기준

향후 실제 코드 생성 시 다음 상태가 되도록 한다.

- 프로젝트가 바로 IntelliJ에서 열리고 Maven import 가능
- `mvn test` 수준에서 최소 컴파일과 기본 테스트 통과
- 스케줄러, 유스케이스, 포트, 어댑터, 설정 클래스가 모두 비어 있지 않고 상세 주석 포함
- TODO 주석으로 향후 비즈니스 로직 삽입 지점이 명확히 보임
