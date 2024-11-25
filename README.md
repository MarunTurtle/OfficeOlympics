# Office Olympics 오피스 올림픽 설계서

## 목차
- [Office Olympics 오피스 올림픽 설계서](#office-olympics-오피스-올림픽-설계서)
  - [목차](#목차)
    - [1. 프로젝트 개요](#1-프로젝트-개요)
    - [2. 타겟 페르소나](#2-타겟-페르소나)
    - [3. 기대 효과](#3-기대-효과)
    - [4. 핵심 기능](#4-핵심-기능)
    - [5. 유저 저니 맵](#5-유저-저니-맵)
    - [6. 기술 스택](#6-기술-스택)
    - [7. 시스템 아키텍처](#7-시스템-아키텍처)
    - [8. API 명세서](#8-api-명세서)
    - [9. Use Case 다이어그램](#9-use-case-다이어그램)
    - [10. ERD 다이어그램](#10-erd-다이어그램)
    - [11. Class 다이어그램](#11-class-다이어그램)
    - [12. 변수명 리스트](#12-변수명-리스트)
    - [13. 화면설계서](#13-화면설계서)
      - [1) 공통 컴포넌트](#1-공통-컴포넌트)
      - [2) 페이지 구성](#2-페이지-구성)
      - [3) 로고](#3-로고)
      - [4) 색상 팔레트](#4-색상-팔레트)
      - [5) 타이포그래피](#5-타이포그래피)
    - [14. 간트차트](#14-간트차트)

### 1. 프로젝트 개요

- **프로젝트 명**: 오피스 올림픽
- **목적**: 사무실에서 간단한 운동을 통해 직장인들이 건강한 생활 습관을 형성하고, 업무 중에 즐거움을 찾을 수 있도록 하는 웹 서비스
- **문제점 해결**: 장시간 앉아 있는 생활로 인한 건강 문제를 해결하고, 직장 내 활력을 불어넣어 스트레스를 해소하는 것을 목표

### 2. 타겟 페르소나

- **직장인:** 사무실에서 근무하며 간단한 운동을 통해 건강 관리를 하고 싶은 사람들
- **팀장**: 팀 **아이스 브레이킹**을 위해 팀원들과 함께 올림픽을 개최하고 싶은 사용자.

### 3. 기대 효과

- **건강 증진**: 직장인들이 업무 중에도 간단한 운동을 통해 건강을 관리할 수 있음
- **팀워크 향상**: 올림픽 게임을 통해 동료들 간의 소통과 협력 강화.
- **스트레스 해소**: 게임 요소를 도입해 업무 스트레스를 줄이고 즐거움을 제공

### 4. 핵심 기능

1. **홈페이지**: 전체 리더보드와 챌린지 목록 제공, 사용자들이 참여할 수 있는 챌린지 소개
2. **회원가입 및 로그인**: 이메일, 비밀번호를 통해 회원가입 및 로그인
3. **올림픽 생성**: 올림픽을 개최하기 위해 필요한 데이터 입력: 올림픽명, 올림픽 참가 인원 수, 플레이어 수, 플레이어의 닉네임
4. **챌린지 참여 및 기록 제출**: 각 챌린지에 대한 상세 정보 제공, 챌린지 참가 후 기록을 제출하여 리더보드에 반영
5. **마이 페이지**: 프로필 수정, 올림픽 정보 조회, 회원 탈퇴
6. **커뮤니티 기능**: 챌린지에 대한 댓글, 답글 작성, 수정, 삭제를 통한 의견 공유

### 5. 유저 저니 맵

| **여정 단계**     | **발견**                          | **등록**                      | **게임 생성 및 플레이**                       | **결과 확인 및 공유**                  |
|-------------------|-----------------------------------|-------------------------------|----------------------------------------------|----------------------------------------|
| **고객 행동**     | 건강 활동 & 아이스 브레이킹을 위한 ‘오피스 올림픽’ 서비스 접속 | 호스트가 올림픽을 생성하고 정보 입력   | 플레이어 설정, 올림픽 생성, 챌린지별 결과 기록, 점수와 순위 확인 | 챌린지 완료 후 최종 결과 확인, 동료와 순위 비교 및 공유 |
| **고객의 필요**   | 간단한 운동으로 팀 분위기 개선 필요, 오프라인 팀 활동 결과 공유 | 간편한 등록 절차, 안전한 개인정보 관리 | 손쉬운 플레이어 등록, 서비스에서 결과 관리, 간편한 사용법 | 팀 성취 자랑 및 공유, 새로운 올림픽 생성 초대 |
| **상호작용**      | 추천이나 웹 검색으로 서비스 접속     | 로그인/회원가입                | 올림픽 생성 및 설정, 챌린지 결과 입력, 리더보드 및 누적 결과 확인 | 최종 리더보드 및 챌린지별 순위 확인, 결과 공유 기능  |
| **고객의 감정**   | 🤔 (호기심), 🥳 (즐거움 기대)        | 😌 (안도감), 👍 (간편함)        | 🤩 (기대감), 😊 (편리함), 💪 (경쟁 의욕)    | 🏆 (성취감), 🎉 (동료와의 즐거움)       |
| **백스테이지**     | 참여 유도 홈페이지 UI              | 보안 강화된 가입/로그인 절차      | 간단한 올림픽 생성폼 제공, 챌린지 결과 관리 기능 개발 | 점수 변환 알고리즘 구현, 결과 공유 및 시각화 제공 |
| **전략**         | 챌린지의 재미를 강조하는 홈페이지    | 간편한 등록 절차 제공            | 명확한 UX, 정확한 점수 관리 기능           | 올림픽 순위 페이지, 새로운 올림픽 생성  |


### 6. 기술 스택

- **프론트엔드**: HTML, CSS, JavaScript, Vue.js, Bootstrap
- **백엔드**: Java Spring Boot, MyBatis
- **데이터베이스**: MySQL
- **API 문서화**: Swagger

### 7. 시스템 아키텍처

- **프론트엔드와 백엔드 분리**: Vue.js를 사용한 프론트엔드와 Spring Boot 기반의 백엔드가 REST API를 통해 통신합니다.
- **데이터 관리**: MySQL을 이용해 사용자, 올림픽, 챌린지, 점수 등을 관리합니다.
- **인증 괸리**: Session을 통해 인증 관리를 합니다.

### 8. API 명세서

| **카테고리** | **기능** | **메소드** | **엔드포인트** |
|--------------|----------|------------|----------------|
| **회원 가입** | 회원가입 폼 반환 | GET | /auth/register |
| | 사용자 회원가입 요청 처리 | POST | /auth/register |
| **로그인** | 로그인 폼 반환 | GET | /auth/login |
| | 사용자 로그인 요청 처리 | POST | /auth/login |
| **로그아웃** | 로그아웃 요청 처리 | POST | /auth/logout |
| **마이페이지** | 마이 페이지 조회 | GET | /accounts/{userId} |
| | 사용자 정보 수정 처리 | PUT | /accounts/{userId} |
| | 회원 탈퇴 처리 | DELETE | /accounts/{userId} |
| **올림픽** | 올림픽 설정 폼 반환 | GET | /olympics |
| | 올림픽 설정 로직 구현 | POST | /olympics |
| | 올림픽 삭제 | DELETE | /olympics/{olympics_id} |
| **챌린지** | 챌린지 세부 정보 조회 | GET | /challenges/{challengesId} |
| | 챌린지 기록 폼 반환 | GET | /challenges/{challengesId}/score |
| | 챌린지 기록 제출 | POST | /challenges/{challengesId}/score |
| | 챌린지 리더보드 조회 | GET | /challenges/{challengesId}/rank |
| | 챌린지 최종 리더보드 조회 | GET | /challenges/{challengesId}/final-rank |
| **메인** | 전체 리더보드 및 챌린지 조회 | GET | / |
| **댓글** | 댓글 조회 (대댓글 포함) | GET | /challenges/{challengesId}/comments |
| | 댓글 등록 | POST | /challenges/{challengesId}/comments |
| | 댓글 수정 | PUT | /challenges/{challengesId}/comments/{commentId} |
| | 댓글 삭제 | DELETE | /challenges/{challengesId}/comments/{commentId} |
| **대댓글** | 대댓글 등록 | POST | /challenges/{challengesId}/comments/{commentId}/replies |
| | 대댓글 수정 | PUT | /challenges/{challengesId}/comments/{commentId}/replies/{replyId} |

### 9. Use Case 다이어그램

![Use Case Diagram](/media/UseCaseDiagram.png) 

### 10. ERD 다이어그램

![ER Diagram](./media/ERD.png)
- **users**: 사용자 정보를 저장하는 테이블로, 이메일, 비밀번호, 닉네임 등의 정보를 포함합니다.
- **olympics**: 올림픽 팀 정보와 호스트 사용자 정보를 저장합니다.
- **players**: 올림픽 참가 플레이어 정보를 저장합니다.
- **challenges**: 챌린지 정보와 설명을 저장합니다.
- **challenge_scores**: 챌린지 참여 기록 및 점수를 저장합니다.
- **comments & replies**: 챌린지에 대한 댓글과 대댓글을 저장합니다.

### 11. Class 다이어그램

![Class Diagram](/media/backend/ClassDiagram.png)

### 12. 변수명 리스트

| 변수명 | 도메인 | 설명 |
|--------|---------|------|
| loginUserId | auth, accounts | 로그인 성공 후 세션에 저장한 사용자 id |
| loginUserName | auth | 로그인 성공 후 세션에 저장한 사용자 이름 |
| email | auth, accounts | 회원가입/정보 수정 시 확인할 email |
| name | auth, accounts | 사용자의 이름 (회원가입 및 정보 조회에 사용) |
| password | auth | 회원가입 및 로그인 시 사용되는 비밀번호 |
| userId | accounts, olympics, challenges | 사용자의 고유 식별자 (계정 조회 및 수정, 팀 생성시에 사용) |
| profileImg | auth, accounts | 사용자 프로필 이미지 (회원가입 및 정보 조회에 사용) |
| imgSrc | auth, accounts | 프로필 이미지의 실제 저장 경로 |
| olympicsName | olympics | 팀 이름 |
| playerNames | olympics, challenges | 플레이어들 이름 [ ] |
| olympicId | olympics | 올림픽 팀의 고유 식별자 (팀 ID) |
| challengeId | challenges | 챌린지 고유 ID |
| scores | challenges | 플레이어별 획득 점수 [ ] |
| rank | challenges | 순위 |
| playerName | challenges | 플레이어 이름 (리더보드용) |
| score | challenges | 플레이어가 획득한 점수 (리더보드용) |
| commentText | challenges | 댓글 내용 |
| commentId | challenges | 댓글 고유 ID |
| UserDTO | DTO | User |
| OlympicsDTO | DTO | OlympicsSetup \| Player |
| ChallengesDTO | DTO | Challenge \| Score \| Rank |
| CommentsDTO | DTO | Comments |

### 13. 화면설계서

[Figma 목업 링크](https://www.figma.com/file/QGQUGc3WrTEybfLmkKcl8f?node-id=0:1&locale=en&type=design)

#### 1) 공통 컴포넌트
- **Navbar**
  - Logo + Office Olympics (좌측)
  - 상태별 버튼 그룹 (우측)
  - Bootstrap 기반 반응형 네비게이션
- **MainLayout**
  - 페이지 공통 레이아웃 제공
  - Navbar, Footer 포함
- **ChallengeCardList**
  - Bootstrap Grid 시스템 활용 3열 레이아웃
  - Card: 썸네일, 제목, 설명, 상세보기 버튼

#### 2) 페이지 구성
1. **메인 페이지 (Home.vue)**
   - 비로그인: 로그인/회원가입 버튼 + Hero Section + ChallengeCardList
   - 로그인+올림픽 생성 후: 마이페이지/로그아웃 버튼 + PlayerLeaderboard + ChallengeCardList
   - 로그인+올림픽 생성 전: 올림픽생성/마이페이지/로그아웃 버튼 + Hero Section + ChallengeCardList

2. **올림픽 관련 페이지**
   - **올림픽 생성 (OlympicCreate.vue)**:
     - 올림픽 이름 입력
     - 참가자 수 설정 (최대 10명)
     - 참가자 닉네임 입력
   - **챌린지 상세 (ChallengeDetail.vue)**:
     - 16:9 비율 비디오 플레이어
     - 챌린지 설명
     - 댓글 시스템
   - **스코어 입력 (ChallengeScore.vue)**:
     - 플레이어별 점수 입력
     - 현재 순위 표시
   - **최종 순위 (FinalRank.vue)**:
     - 시상대 형태의 순위 표시
     - 새 올림픽 시작/홈으로 이동 버튼

3. **마이페이지 (MyPage.vue)**
   - 프로필 정보 표시
   - 올림픽 참가자 목록 및 점수
   - 프로필 수정 모달
   - 계정 삭제 기능

4. **인증 페이지**
   - 로그인: 이메일/비밀번호 입력
   - 회원가입: 개인정보 입력 폼, 프로필 이미지 업로드

#### 3) 로고
![Office Olympics Logo](/media/frontend/logo_office_olympics_large.png)
- 사무용품을 활용한 올림픽 오륜기 모티브
- USB, 자, 시계, 스테이플러, 도넛으로 구성

#### 4) 색상 팔레트
![Color Palette](/media/frontend/color_palette.png)
| 색상 | HEX | 용도 |
|------|-----|------|
| `#2B88D9` | Primary | 주요 브랜드 색상, 로고, 강조 텍스트 |
| `#F2F2F0` | Secondary | 배경, 네비게이션 바 |
| `#F2AE30` | Warning | 주의, 환기, 경고 |
| `#0DA64F` | Success | 성공, 반응, 긍정적 액션 |
| `#F2668B` | Alert | 오류, 로그아웃, 삭제 기능 |

- 모든 색상은 WCAG 2.0 접근성 기준을 준수
- CSS 변수로 관리 (`--primary-color`, `--secondary-color` 등)

#### 5) 타이포그래피
- **기본 서체**: Pretendard
  - 한글 최적화 및 가독성이 우수한 오픈소스 글꼴
  - [Pretendard 공식 GitHub](https://github.com/orioncactus/pretendard)

### 14. 간트차트

![Gantt Chart](/media/GanttChart.png)