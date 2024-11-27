# Office Olympics - 최종 관통 프로젝트 정리

## 목차
- [1. 프로젝트 구조](#1-프로젝트-구조)
- [2. 프로젝트 개요](#2-프로젝트-개요)
- [3. 기술 스택](#3-기술-스택)
- [4. 주요 디렉터리 및 파일](#4-주요-디렉터리-및-파일)
  - [4.1 애플리케이션 진입점](#41-애플리케이션-진입점)
  - [4.2 설정 파일 (Config)](#42-설정-파일-config)
  - [4.3 컨트롤러 (Controller)](#43-컨트롤러-controller)
  - [4.4 데이터 액세스 계층 (DAO)](#44-데이터-액세스-계층-dao)
  - [4.5 서비스 계층 (Service)](#45-서비스-계층-service)
  - [4.6 DTO](#46-dto)
  - [4.7 유틸리티 (Util)](#47-유틸리티-util)
  - [4.8 리소스 및 설정 파일](#48-리소스-및-설정-파일)
- [5. 시스템 보안](#5-시스템-보안)
- [6. 주요 기능](#6-주요-기능)
- [7. 개발 환경 설정](#7-개발-환경-설정)
- [8. 기능별 개발 내용 기록](#8-기능별-개발-내용-기록)
  - [Auth/Accounts](./docs/backend_summaries/Auth_Accounts.md)
  - [Olympics](./docs/backend_summaries/Olympics.md)
  - [Challenges](./docs/backend_summaries/Challenges.md)
  - [Comments](./docs/backend_summaries/Comment.md)
  - [TroubleShooting](./docs/trouble_shooting/TroubleShootingLog.md)

---

## 1. 프로젝트 구조

```
OfficeOlympics/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/olympics/mvc/
│   │   │   ├── OfficeOlympicsApplication.java
│   │   │   ├── config/
│   │   │   │   ├── DBConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AccountController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ChallengeController.java
│   │   │   │   ├── CommentsController.java
│   │   │   │   ├── MainController.java
│   │   │   │   └── OlympicsController.java
│   │   │   ├── interceptor/
│   │   │   │   └── LoginInterceptor.java
│   │   │   ├── model/
│   │   │   │   ├── dao/
│   │   │   │   │   ├── ChallengeScoreDao.java
│   │   │   │   │   ├── CommentsDao.java
│   │   │   │   │   ├── PlayerDao.java
│   │   │   │   │   └── UserDao.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── Challenge.java
│   │   │   │   │   ├── Comments.java
│   │   │   │   │   ├── OlympicsSetup.java
│   │   │   │   │   ├── Player.java
│   │   │   │   │   ├── Rank.java
│   │   │   │   │   ├── Score.java
│   │   │   │   │   └── User.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── ChallengeScoreService.java
│   │   │   │   │   ├── ChallengeScoreServiceImpl.java
│   │   │   │   │   ├── CommentsService.java
│   │   │   │   │   ├── CommentsServiceImpl.java
│   │   │   │   │   ├── PlayerService.java
│   │   │   │   │   ├── PlayerServiceImpl.java
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   └── UserServiceImpl.java
│   │   │   ├── util/
│   │   │   │   ├── FileConfirm.java
│   │   │   │   ├── HashUtil.java
│   │   │   │   └── Validate.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── mappers/
│   │       │   ├── ChallengeScoreMapper.xml
│   │       │   ├── CommentsMapper.xml
│   │       │   ├── PlayerMapper.xml
│   │       │   └── UserMapper.xml
│   │       └── sql/
│   │           ├── schema.sql
│   │           └── data.sql
└── target/
```

---

## 2. 프로젝트 개요
- **프로젝트 이름**: Office Olympics
- **설명**: 사내 팀의 게임과 도전 과제를 관리하는 시스템.
- **주요 기능**:
  1. **사용자 관리**
   - 사용자 생성 및 관리.
   - SHA-256 해시 알고리즘과 salt를 활용한 안전한 비밀번호 암호화 구현.

  2. **팀 관리**
   - 팀 생성 및 수정.
   - 팀과 관련된 데이터는 `olympics` 테이블에서 관리.

  3. **팀원 관리**
   - 특정 팀에 소속된 팀원 추가 및 관리.
   - 팀원 정보는 `players` 테이블에서 관리.

  4. **챌린지 관리**
   - 챌린지를 정의하고 팀원 간 경쟁을 촉진.


---

## 3. 기술 스택
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.3.5
- **ORM**: MyBatis 기반의 관계형 데이터 관리
- **데이터베이스**: MySQL
- **보안**: 
  - SHA-256 비밀번호 해싱 및 salt 처리
  - 애플리케이션 운영용 계정 **ooc**를 통한 권한 분리
- **버전 관리**: Git 브랜칭을 활용한 기능 개발과 병합

---

## 4. 주요 디렉터리 및 파일

### 4.1 애플리케이션 진입점
- **OfficeOlympicsApplication**

### 4.2 설정 파일 (Config)
 - **DBConfig**: 데이터베이스 설정과 MyBatis 통합을 처리.
 - **SwaggerConfig**: Swagger를 사용한 API 문서화 설정 파일.
 - **WebConfig**: CORS 정책 및 전역 설정을 포함하는 클래스.

### 4.3 컨트롤러 (Controller)
 - **AccountController**: 사용자 계정 관리를 위한 API 제공.
 - **AuthController**: 로그인 및 인증 처리.
 - **ChallengeController**: 도전 과제 생성 및 점수 관리.
 - **CommentsController**: 댓글 관리 API.
 - **MainController**: 메인 페이지의 데이터를 반환.
 - **OlympicsController**: 팀과 플레이어 관리 API.

### 4.4 데이터 액세스 계층 (DAO)
 - **ChallengeScoreDao**: 도전 점수 관련 데이터베이스 작업.
 - **CommentsDao**: 댓글 데이터를 CRUD 처리.
 - **PlayerDao**: 플레이어 데이터를 CRUD 처리.
 - **UserDao**: 사용자 정보의 CRUD 처리.

### 4.5 서비스 계층 (Service)
 - **ChallengeScoreService / Impl**: 도전 점수 계산 및 로직 처리.
 - **CommentsService / Impl**: 댓글 추가 및 삭제 처리.
 - **PlayerService / Impl**: 플레이어 관리와 관련된 비즈니스 로직을 처리.
 - **UserService / Impl**: 사용자 관리 로직을 처리.

### 4.6 DTO
 - **Challenge.java**: 도전 과제 데이터를 담는 클래스.
 - **Comments.java**: 댓글 데이터를 저장하는 클래스.
 - **OlympicsSetup.java**: 팀 구성 데이터를 포함하는 클래스.
 - **Player.java**: 플레이어 데이터를 관리하는 클래스.
 - **Rank.java**: 순위 데이터를 포함하는 클래스.
 - **Score.java**: 점수 데이터를 관리하는 클래스.
 - **User.java**: 사용자 정보를 포함하는 클래스.

### 4.7 유틸리티 (Util)
 - **FileConfirm.java**: 파일 업로드 및 확인 관련 메서드.
 - **HashUtil.java**: 비밀번호 암호화 및 검증 로직.
 - **Validate.java**: 입력 값 유효성 검사.

### 4.8 리소스 및 설정 파일
 - **application.properties**: 애플리케이션 전역 설정.
 - **application-db.properties**: DB설정.
 - **mappers/**: MyBatis 매퍼 XML.
 - **static/uploads/**: 사용자 프로필 이미지 및 기타 업로드된 파일.

---

## 5. 시스템 보안

### 5.1 비밀번호 암호화
- **SHA-256 해싱 및 솔트 방식**:
  - 비밀번호 해싱에 SHA-256 알고리즘을 활용하고, 솔트를 추가하여 보안성을 강화.
  - **장점**: 데이터 유출 시에도 비밀번호 복원 불가.

### 5.2 세션 및 인증
- **LoginInterceptor**를 활용한 세션 기반 인증:
  - 로그인 상태를 확인하고 보호된 리소스에 접근 제어.
  - **트랜잭션 기반**으로 동작하여 안전한 데이터 처리.

### 5.3 운영 계정 **ooc**
- **운영용 계정 분리**:
  - 데이터베이스 관리 및 민감한 데이터 접근에 사용할 **ooc** 계정을 추가.
  - **권한 제한**:
    - ooc 계정은 운영 환경에서만 사용되도록 '%'가 아닌 IP를 기입
  - **보안 강화**:
    - 운영 계정의 비밀번호는 추가로 암호화되어 보관. (application-db.properties / 배포시 .gitignor에 추가)

---

## 6. 주요 기능

### 6.1 Auth 시스템
- **SHA-256 해싱 및 솔트 방식**:
  - 비밀번호 해싱과 솔트 적용으로 보안성 강화.
- **세션 기반 인증**:
  - 세션 상태를 활용해 로그인 사용자 인증.
- **보안 로그 기록**:
  - 실패한 로그인 시 모든 시도를 기록하여 감사 및 모니터링에 활용.

---

### 6.2 Player 및 Olympics 관리 시스템
- **효율적인 데이터 관리**:
  - 선수 및 팀 데이터를 MyBatis와 DTO를 통해 관리.
- **트랜잭션 처리**:
  - 팀 삭제 시 관련 데이터를 일괄 삭제.
- **확장 가능성**:
  - 새로운 올림픽 또는 챌린지 추가 시 구조 변경 없이 바로 반영 가능.

---

### 6.3 챌린지 시스템
- **순위 계산 및 점수 관리**:
  - MyBatis 매퍼를 활용하여 대량 데이터를 효율적으로 처리.
- **실시간 피드백**:
  - 사용자 점수 제출 시 즉각적인 피드백 제공.
- **트랜잭션 보장**:
  - 점수 제출 및 수정 시 데이터 일관성 유지.

---

### 6.4 댓글 시스템
- **계층적 데이터 관리**:
  - `commentGroup` 및 `commentDepth`를 활용해 댓글과 대댓글 관리.
- **삭제 처리**:
  - 댓글 삭제 시 대댓글이 있으면 "삭제된 메시지"로 대체.
  - 대댓글이 모두 삭제되면 원댓글 삭제.


## 7. 개발 환경 설정

### 7.1 실행
```bash
mvn spring-boot:run
```

### 7.2 데이터베이스 초기화
`src/main/resources/sql/schema.sql`과 `data.sql` 파일을 통해 데이터베이스 초기화 가능.

<details>
<summary><h3>7.3 운영계정 ooc 생성</h3></summary>
<div markdown="1">


  1. MySQL 접속

        ```sql
        mysql -u root -p;
        ```
        
  2. 운영용 계정 생성
        
        ```sql
        CREATE USER 'ooc'@'localhost' IDENTIFIED BY 'Office123!@#';
        ```
        
  3. 필요한 권한 부여
        
        ```sql
        GRANT SELECT, INSERT, UPDATE, DELETE ON office_db.* TO 'ooc'@'localhost';
        ```
        
  4. 권한 적용
        
        ```sql
        FLUSH PRIVILEGES;
        ```
        
  5. 계정 생성 확인 (root 권한 계정으로 확인 필요)
        
        ```sql
        SELECT user, host FROM mysql.user WHERE user = 'ooc';
        ```
        
  6. 계정 접속 테스트
        ```sql
        mysql -u ooc -p;
        ```
</div>
</details>

---
## 8. 기능별 개발 내용 기록
 - [Auth_Accounts](./docs/backend_summaries/Auth_Accounts.md)
 - [Olympics](./docs/backend_summaries/Olympics.md)
 - [Challenges](./docs/backend_summaries/Challenges.md)
 - [Comments](./docs/backend_summaries/Comment.md)
 - [TroubleShooting](./docs/trouble_shooting/TroubleShootingLog.md)
