# Office Olympics 프론트엔드 - 프로젝트 구조 및 설정

## 목차
- [컴포넌트별 상세 문서](#컴포넌트별-상세-문서)
- [프로젝트 소감](#프로젝트-소감)
- [프로젝트 구조](#1-프로젝트-구조)
- [프로젝트 개요](#2-프로젝트-개요)
- [기술 스택](#3-기술-스택)
- [주요 설정 파일](#4-주요-설정-파일)
- [주요 디렉터리 설명](#5-주요-디렉터리-설명)
- [주요 기능](#6-주요-기능)
- [개발 환경 설정](#7-개발-환경-설정)

## 컴포넌트별 상세 문서
프로젝트의 주요 컴포넌트들에 대한 자세한 설명은 다음 문서들을 참조하세요:

1. [네비게이션 바와 홈페이지](./frontend_summaries/0_NavBar_Home.md)
2. [인증 및 사용자 관리](./frontend_summaries/1_Auth_Users.md)
3. [올림픽 이벤트 관리](./frontend_summaries/2_Olympics.md)
4. [챌린지 시스템](./frontend_summaries/3_Challenge.md)
5. [댓글 시스템](./frontend_summaries/4_Comments.md)

## 프로젝트 소감

### 배운 점
- 컴포넌트화의 중요성과 재사용 가능한 코드 설계하는 것의 중요성
- API 명세서, 변수명, ERD를 프로젝트 초기에 확정하는 것이 프로젝트 진행에 큰 도움이 됨
- 백엔드 개발자와의 원활한 소통을 위한 문서화의 중요성 학습: "기록이 기억을 이긴다."
- 체계적인 프로젝트 구조 설계의 필요성을 이해함

### 어려웠던 점
- 에러 발생 시 문제 지점을 특정하기 위한 디버깅 과정이 어려웠음
- 효과적인 디버깅을 위한 console.log 배치의 중요성 인식
- 에러 메시지 분석 및 문제 해결을 위한 백엔드와의 소통하는 방법을 익힘.
- 복잡한 상태 관리와 컴포넌트 간 데이터 흐름 파악의 어려움을 극복함.

### 개선하고 싶은 점
- 미래에는 Mock Server를 활용한 프론트엔드 개발 환경 구축으로 개발 효율성을 높이고 싶음
- 개발 과정에서 단순 구현에 더해서 효율적인 에러 핸들링 및 디버깅 시스템 구축

## 1. 프로젝트 구조
```
office-olympics-fe/
├── public/
├── src/
│ ├── App.vue
│ ├── assets/
│ │ ├── images/
│ │ │ └── mainpage/ 
│ │ └── styles/
│ │ └── main.css
│ ├── components/
│ │ ├── ChallengeCard.vue
│ │ ├── Comments.vue
│ │ ├── EditProfileModal.vue
│ │ ├── Footer.vue
│ │ ├── Navbar.vue 
│ │ └── RecommendedChallengeCard.vue
│ ├── layouts/
│ │ ├── AuthLayout.vue
│ │ └── MainLayout.vue
│ ├── pages/
│ │ ├── ChallengeDetail.vue
│ │ ├── ChallengeRank.vue 
│ │ ├── ChallengeScore.vue 
│ │ ├── Error.vue
│ │ ├── FinalRank.vue
│ │ ├── Home.vue
│ │ ├── Login.vue
│ │ ├── MyPage.vue
│ │ ├── OlympicCreate.vue
│ │ ├── OlympicDetail.vue
│ │ └── Register.vue 
│ ├── router/
│ │ └── index.js
│ ├── services/
│ │ ├── api.js
│ │ ├── auth.js
│ │ ├── challenge.js
│ │ ├── comment.js
│ │ ├── olympic.js
│ │ └── user.js 
│ ├── stores/
│ │ ├── auth.js
│ │ ├── challenge.js
│ │ ├── comment.js
| | ├── olympic.js
│ │ └── user.js
│ ├── utils/
│ │ ├── formatters.js
│ │ ├── validation.js
│ │ └── youtube.js
│ └── main.js
├── .gitignore
├── index.html
├── README.md
```

## 2. 프로젝트 개요
Office Olympics는 Vue 3와 Vite를 기반으로 한 웹 애플리케이션입니다. 사무실에서 진행하는 올림픽 게임을 관리하고 점수를 기록하는 플랫폼입니다.

### 핵심 특징
- **보안 인증**: Session 기반의 안전한 사용자 인증 시스템
- **챌린지 관리**: RESTful API를 통한 게임과 도전 과제 CRUD
- **실시간 순위 시스템**: 실시간 점수 집계 및 순위 표시
- **소셜 기능**: 댓글 시스템을 통한 참가자 간 소통
- **사용자 친화적 UI**: Bootstrap 5 기반의 반응형 디자인

### 기술적 특징
- **Vue 3 Composition API**: 재사용 가능한 로직 구성
- **Pinia 상태관리**: 체계적인 전역 상태 관리
- **컴포넌트 기반 설계**: 재사용 가능한 UI 컴포넌트
- **동적 라우팅**: 코드 스플리팅을 통한 성능 최적화

## 3. 기술 스택
- **프레임워크**: Vue 3
- **빌드 도구**: Vite
- **상태 관리**: Pinia
- **라우팅**: Vue Router
- **스타일링**: Bootstrap 5
- **HTTP 클라이언트**: Axios

## 4. 주요 설정 파일
### 4.1 진입점 (index.html)
- 기본 메타 정보 설정
- Bootstrap CSS/JS 및 아이콘 CDN 연결
- Vue 앱 마운트 포인트 정의

### 4.2 메인 애플리케이션 (main.js)
- Vue 앱 인스턴스 생성
- Pinia 스토어 설정
- 라우터 설정
- 전역 스타일 적용
- 사용자 인증 상태 초기화

### 4.3 라우팅 설정 (router/index.js)
- 공개 접근 가능한 라우트
- 인증이 필요한 보호된 라우트
- 동적 임포트를 통한 코드 스플리팅
- 네비게이션 가드 설정

## 5. 주요 디렉터리 설명
### 5.1 컴포넌트 (components/)
- **ChallengeCard.vue**: 챌린지 정보를 표시하는 재사용 가능한 카드 컴포넌트
- **Comments.vue**: 챌린지별 댓글 시스템 컴포넌트 (대댓글 지원)
- **EditProfileModal.vue**: 사용자 프로필 수정을 위한 모달 컴포넌트
- **Footer.vue**: 애플리케이션 전역 푸터 컴포넌트 (저작권 정보 표시)
- **Navbar.vue**: 애플리케이션 상단 네비게이션 바 컴포넌트
- **RecommendedChallengeCard.vue**: 추천 챌린지 카드 컴포넌트

### 5.2 페이지 (pages/)
- **Home.vue**: 메인 페이지, 사용자 상태에 따른 조건부 렌더링 구현
  - 비로그인: 로그인/회원가입 버튼 + 서비스 소개
  - 로그인+올림픽 생성 후: 순위표 + 챌린지 목록
  - 로그인+올림픽 생성 전: 올림픽 생성 유도

- **인증 관련**
  - **Login.vue**: 로그인 페이지, 세션 기반 인증
  - **Register.vue**: 회원가입 페이지
  - **MyPage.vue**: 사용자 프로필 및 참여 중인 올림픽 정보 표시

- **올림픽 관련**
  - **OlympicCreate.vue**: 새로운 올림픽 이벤트 생성 및 참가자 등록
  - **OlympicDetail.vue**: 올림픽 상세 정보 및 참가자 목록 표시

- **챌린지 관련**
  - **ChallengeDetail.vue**: 챌린지 상세 정보, 비디오 플레이어, 댓글 시스템
  - **ChallengeScore.vue**: 참가자별 점수 입력 및 제출
  - **ChallengeRank.vue**: 챌린지별 순위 표시
  - **FinalRank.vue**: 올림픽 최종 순위 및 시상대 형태의 결과 표시

- **기타**
  - **Error.vue**: 404 및 기타 에러 상황 처리

### 5.3 스토어 (stores/)
- **auth.js**: 사용자 인증 상태 및 세션 관리
- **challenge.js**: 챌린지 데이터 및 상태 관리
- **comment.js**: 댓글 데이터 및 상태 관리
- **olympic.js**: 올림픽 이벤트 관련 상태 관리
- **user.js**: 사용자 프로필 데이터 및 상태 관리

### 5.4 서비스 (services/)
- **api.js**: Axios 인스턴스 및 기본 설정
- **auth.js**: 인증 관련 API 호출 함수 (로그인, 회원가입, 로그아웃)
- **challenge.js**: 챌린지 관련 API 호출 함수
- **comment.js**: 댓글 관련 API 호출 함수
- **olympic.js**: 올림픽 관련 API 호출 함수
- **user.js**: 사용자 프로필 관련 API 호출 함수

### 5.5 유틸리티 (utils/)
- **formatters.js**: 날짜, 시간, 점수 등의 포맷팅 함수
- **validation.js**: 입력값 유효성 검사 유틸리티
- **youtube.js**: YouTube 동영상 관련 헬퍼 함수

### 5.6 에셋 (assets/)
- **images/**: 이미지 리소스 저장
  - **mainpage/**: 메인 페이지 관련 이미지
- **styles/**:
  - **main.css**: 전역 스타일 정의

### 5.7 레이아웃 (layouts/)
- **AuthLayout.vue**: 인증 관련 페이지용 레이아웃
- **MainLayout.vue**: 메인 콘텐츠 페이지용 레이아웃

## 6. 주요 기능

### 6.1 실시간 순위 시스템
- 참가자들의 점수를 실시간으로 집계하고 순위표 표시 (ChallengeRank.vue)
- 챌린지별/전체 순위 필터링 기능
- 시상대 형식의 최종 순위 표시 (FinalRank.vue)
- 순위별 차별화된 스타일링 적용

### 6.2 챌린지 관리 시스템
- 새로운 챌린지 생성 및 수정 기능
- YouTube 동영상 통합 및 플레이어 지원 (ChallengeDetail.vue)
- 참가자별 점수 입력 및 검증 시스템 (ChallengeScore.vue
- 추천 챌린지 시스템 (RecommendedChallengeCard.vue)

### 6.3 사용자 인증 및 프로필
- Session 기반의 안전한 로그인/회원가입 시스템
- 사용자 프로필 관리 및 수정 (EditProfileModal.vue)
- 권한 기반의 기능 접근 제어
- 참여 중인 올림픽 목록 및 진행 상황 표시 (MyPage.vue)

### 6.4 올림픽 이벤트 관리
- 새로운 올림픽 이벤트 생성 및 설정 (OlympicCreate.vue)
- 참가자 등록 및 관리 시스템
- 올림픽 상세 정보 및 진행 현황 대시보드 (OlympicDetail.vue)
- 올림픽별 챌린지 구성 및 일정 관리
- 결과 집계 및 통계 분석

### 6.5 상태별 반응형 UI 변화
#### 네비게이션 바 (Navbar.vue)
1. **로그아웃 상태**
   - 데스크톱: 우측 정렬된 로그인/회원가입 버튼
   - 모바일: 햄버거 메뉴 내 세로 정렬된 버튼

2. **로그인 + 올림픽 생성 상태**
   - 데스크톱: 우측 정렬된 마이페이지/로그아웃 버튼
   - 모바일: 햄버거 메뉴 내 세로 정렬

3. **로그인 + 올림픽 미생성 상태**
   - 데스크톱: 우측 정렬된 올림픽 만들기/마이페이지/로그아웃 버튼
   - 모바일: 강조된 '올림픽 만들기' 버튼을 최상단에 배치
   - 반응형 호버 효과 (모바일: scale 1.02, 데스크톱: scale 1.05)

#### 홈페이지 (Home.vue)
1. **로그인 + 올림픽 생성 상태**
   - 데스크톱: 순위표와 3열 그리드의 챌린지 카드
   - 태블릿: 순위표 스크롤 지원, 2열 챌린지 그리드
   - 모바일: 수직 스크롤 순위표, 1열 챌린지 그리드

2. **로그인 + 올림픽 미생성 상태**
   - 데스크톱: 전체 너비 슬라이드쇼와 중앙 정렬된 CTA 버튼
   - 모바일: 화면 높이에 맞춘 슬라이드쇼, 터치 친화적 버튼
   - 반응형 폰트 크기 (welcome-message 클래스)

3. **로그아웃 상태**
   - 데스크톱: 전체 너비 슬라이드쇼와 로그인 유도 메시지
   - 모바일: 최적화된 이미지 크기와 터치 영역

### 6.6 댓글/대댓글 시스템
- 챌린지별 댓글 시스템 (Comments.vue)
- 대댓글 지원으로 심층적인 토론 가능
- 댓글 수정 및 삭제 기능

## 7. 개발 환경 설정
### 7.1 프로젝트 설치
```bash
npm install
```

### 7.2 개발 서버 실행
```bash
npm run dev
```

### 7.3 프로덕션 빌드
```bash
npm run build
```
