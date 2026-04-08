# 멀티 모듈 구조 및 프로파일 정리

## 1. 현재 프로젝트 구조

이 프로젝트는 단일 모듈 구조가 아니라 멀티 모듈 Maven 프로젝트로 정리되었다.
외부 연계 기능은 애플리케이션 코어와 분리된 별도 모듈로 구성한다.

```text
rpa-batch-parent
├─ rpa-core
├─ rpa-persistence
├─ rpa-external-excel
├─ rpa-external-mattermost
└─ rpa-bootstrap
```

## 2. 모듈별 책임

### 2.1 rpa-core

- 유스케이스
- 입력/출력 포트
- 공통 DTO
- 공통 예외
- 공통 로그 유틸
- 설정 프로퍼티 클래스

이 모듈은 업무 흐름의 중심이며, 외부 기술 구현 세부사항을 직접 포함하지 않는다.

### 2.2 rpa-persistence

- Oracle/JPA 기반 조회 어댑터
- 엔티티
- Spring Data JPA Repository

DB 연계 책임만 담당한다.

### 2.3 rpa-external-excel

- 엑셀 템플릿 로드
- Workbook 생성
- 다중 시트 작성
- 엑셀 파일 생성 어댑터

엑셀 변환 기능을 별도 모듈로 격리한다.

### 2.4 rpa-external-mattermost

- Mattermost API 클라이언트
- 파일 업로드 어댑터
- 메시지 전송 어댑터

메신저 연계를 별도 모듈로 격리한다.

### 2.5 rpa-bootstrap

- Spring Boot 실행 클래스
- JPA 공통 설정
- `application.yml` 및 profile 설정 파일
- 템플릿 리소스

실행과 환경설정, 조립 역할만 담당한다.

## 3. 내부 스케줄러 정책

- 애플리케이션 내부에 스케줄러를 두지 않는다.
- `@Scheduled`, 크론 표현식, 스케줄 활성/비활성 설정은 제거했다.
- JobPass가 외부에서 프로그램 실행 시점과 주기를 관리한다고 가정한다.

즉, 이 프로그램은 "스스로 시간을 계산해 실행하는 프로그램"이 아니라
"JobPass가 실행해 주는 배치 프로그램"이다.

## 4. 프로파일 기준

프로파일 파일은 `rpa-bootstrap/src/main/resources` 아래에 둔다.

- `application.yml`
- `application-local.yml`
- `application-run.yml`
- `application-test.yml`
- `application-real.yml`

## 5. 프로파일별 용도

### 5.1 local

- 개발자 로컬 Oracle 확인용
- 로컬 DB 정보와 로컬 Mattermost 테스트값을 둘 수 있다.

### 5.2 run

- IntelliJ에서 안전하게 기동 확인하는 용도
- H2 메모리 DB 사용
- 실제 외부 서버 연결 없이 애플리케이션 컨텍스트가 뜨는지 확인하는 목적

### 5.3 test

- 테스트 서버 배포/실행용
- 테스트 DB, 테스트 Mattermost 설정을 사용

### 5.4 real

- 운영 서버 배포/실행용
- 운영 DB, 운영 Mattermost 설정을 사용
- 민감 정보는 반드시 환경변수 또는 외부 시크릿으로 주입한다.

## 6. 설정 관리 원칙

- 공통 키 구조는 `application.yml` 에만 둔다.
- DB 접속 정보, 토큰, 채널 ID 등 환경별 값은 각 profile 파일에 둔다.
- 운영 민감 정보는 하드코딩하지 않는다.

예시 기준:

```yaml
spring:
  datasource:
    url: ${REAL_DB_URL}
    username: ${REAL_DB_USERNAME}
    password: ${REAL_DB_PASSWORD}
    driver-class-name: oracle.jdbc.OracleDriver

rpa:
  mattermost:
    base-url: ${REAL_MATTERMOST_BASE_URL}
    token: ${REAL_MATTERMOST_TOKEN}
    channel-id: ${REAL_MATTERMOST_CHANNEL_ID}
```

## 7. IntelliJ 실행 기준

실행 모듈은 `rpa-bootstrap` 이다.

- 메인 클래스: `com.example.rpa.RpaApplication`
- 안전 기동 확인: `run` 프로파일
- 테스트 서버 실행: `test` 프로파일
- 운영 실행: `real` 프로파일

## 8. 빌드 검증 상태

현재 구조는 멀티 모듈 기준으로 `mvn test` 통과 상태다.
즉, 모듈 분리 후에도 전체 프로젝트는 정상적으로 컴파일 및 테스트된다.
