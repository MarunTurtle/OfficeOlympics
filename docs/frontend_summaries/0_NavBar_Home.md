# 오피스 올림픽 - 네비게이션 바와 홈페이지 가이드

## 목차
- [오피스 올림픽 - 네비게이션 바와 홈페이지 가이드](#오피스-올림픽---네비게이션-바와-홈페이지-가이드)
  - [목차](#목차)
  - [1. 기본 구조 이해하기](#1-기본-구조-이해하기)
    - [1.1 컴포넌트 구조 이해하기](#11-컴포넌트-구조-이해하기)
    - [1.2 주요 Vue.js 개념](#12-주요-vuejs-개념)
    - [1.3 유용한 팁](#13-유용한-팁)
  - [2. 네비게이션 바 (NavBar.vue)](#2-네비게이션-바-navbarvue)
    - [2.1 개요](#21-개요)
    - [2.2 세 가지 케이스](#22-세-가지-케이스)
    - [2.3 스타일링](#23-스타일링)
  - [3. 홈페이지 (Home.vue)](#3-홈페이지-homevue)
    - [3.1 개요](#31-개요)
    - [3.2 세 가지 케이스](#32-세-가지-케이스)
    - [3.3 반응형 디자인](#33-반응형-디자인)
  - [4. 주요 기능 구현](#4-주요-기능-구현)
    - [4.1 상태 관리](#41-상태-관리)
    - [4.2 데이터 흐름](#42-데이터-흐름)
  - [5. 오류 처리](#5-오류-처리)
  - [6. 성능 최적화](#6-성능-최적화)

## 1. 기본 구조 이해하기

### 1.1 컴포넌트 구조 이해하기
1. **템플릿 섹션** (`<template>`)
   - HTML 구조 정의
   - 조건부 렌더링 (v-if/v-else)
   - 이벤트 바인딩 (@click)

2. **스크립트 섹션** (`<script setup>`)
   - 컴포넌트 로직
   - 상태 관리
   - 계산된 속성

3. **스타일 섹션** (`<style>`)
   - 컴포넌트별 스타일
   - 반응형 디자인
   - 전역 스타일 상속

### 1.2 주요 Vue.js 개념
1. **반응형 데이터**
   - `ref()`: 단일 값 반응형
   - `computed()`: 계산된 속성

2. **조건부 렌더링**
   - `v-if/v-else-if/v-else`
   - `template` 태그 활용

3. **이벤트 처리**
   - `@click` 이벤트
   - 메서드 정의와 호출

### 1.3 유용한 팁
- 컴포넌트 이름은 명확하게 지정
- 주석으로 코드 설명 추가
- 일관된 코딩 스타일 유지
- 재사용 가능한 컴포넌트 설계

## 2. 네비게이션 바 (NavBar.vue)

### 2.1 개요
네비게이션 바는 사용자의 로그인 상태와 올림픽 참여 여부에 따라 세 가지 다른 모습을 보여줍니다. 이 컴포넌트는 `Navbar.vue`에서 구현되어 있습니다.

### 2.2 세 가지 케이스

1. **비로그인 상태**
   - 로고 (홈 링크)
   - 로그인 버튼
   - 회원가입 버튼
   - **사용된 컴포넌트**: `RouterLink`

2. **로그인 + 올림픽 있음**
   - 로고
   - 마이페이지 버튼
   - 로그아웃 버튼
   - **사용된 컴포넌트**: `RouterLink`, `authStore`

3. **로그인 + 올림픽 없음**
   - 로고
   - 올림픽 만들기 버튼 (노란색)
   - 마이페이지 버튼
   - 로그아웃 버튼
   - **사용된 컴포넌트**: `RouterLink`, `authStore`

참고 코드:

```9:77:office-olympics-fe/src/components/Navbar.vue
<template>
  <!--
    Bootstrap Navbar 컴포넌트
    반응형 디자인을 위해 expand-lg 클래스 사용
  -->
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <!-- 로고 및 홈 링크 -->
      <RouterLink class="navbar-brand" to="/">
        <img src="@/assets/images/logo.png" alt="오피스 올림픽 로고" class="navbar-logo">
      </RouterLink>

      <!--
        모바일 뷰에서 표시되는 토글 버튼
        data-bs-toggle="collapse"로 Bootstrap 동작 연결
      -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- 네비게이션 메뉴 아이템들 -->
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <!--
            Case 1: 로그아웃 상태
            로그인/회원가입 버튼 표시
          -->
          <template v-if="!isLoggedIn">
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/auth/login">로그인</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/auth/register">회원가입</RouterLink>
            </li>
          </template>

          <!--
            Case 2: 로그인 상태 & 올림픽 생성됨
            마이페이지/로그아웃 버튼 표시
          -->
          <template v-else-if="isLoggedIn && hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn" :to="`/accounts/${authStore.user.id}`">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <button class="btn btn-tertiary" @click="onLogout">로그아웃</button>
            </li>
          </template>

          <!--
            Case 3: 로그인 상태 & 올림픽 미생성
            올림픽 생성/마이페이지/로그아웃 버튼 표시
          -->
          <template v-else-if="isLoggedIn && !hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn btn-warning" to="/olympic/create">올림픽 만들기</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/accounts/{{ authStore.user.id }}">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <button class="btn btn-tertiary" @click="onLogout">로그아웃</button>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
```

### 2.3 스타일링
네비게이션 바는 일관된 디자인 시스템을 따릅니다. 스타일은 `main.css` 파일에서 관리됩니다.

```13:20:office-olympics-fe/src/assets/styles/main.css
}

/* Colors */
:root {
  --primary-color: #2B88D9;
  --secondary-color: #F2F2F0;
  --tertiary-color: #E8EdF2;
  --warning-color: #F2AE30;
```

## 3. 홈페이지 (Home.vue)

### 3.1 개요
홈페이지는 사용자의 상태에 따라 세 가지 다른 뷰를 제공합니다. 이 컴포넌트는 `Home.vue`에서 구현되어 있습니다.

### 3.2 세 가지 케이스

1. **비로그인 상태**
   - 환영 메시지
   - 서비스 소개
   - 로그인/회원가입 유도
   - 슬라이드쇼 배경
   - **사용된 컴포넌트**: `RouterLink`

2. **로그인 + 올림픽 없음**
   - 올림픽 생성 안내
   - 올림픽 생성 버튼
   - 사용 가이드
   - 추천 챌린지 미리보기
   - **사용된 컴포넌트**: `RouterLink`, `olympicStore`

3. **로그인 + 올림픽 있음**
   - 현재 순위표
   - 진행 중인 챌린지
   - 추천 챌린지
   - 참여자 현황
   - **사용된 컴포넌트**: `RouterLink`, `olympicStore`, `challengeStore`

참고 코드:

```11:32:office-olympics-fe/src/pages/Home.vue
 * @컴포넌트명: Home
 * @설명: 사용자 상태에 따라 다른 화면을 보여주는 홈페이지
 * @상태:
 *   - 로그인 + 올림픽 있음: 순위표와 추천 챌린지
 *   - 로그인 + 올림픽 없음: 올림픽 생성 유도
 *   - 비로그인: 로그인 유도
 * @데이터구조: {
 *   leaderboard: Array<{
 *     playerName: string,
 *     score: number
 *   }>,
 *   challenges: Array<{
 *     challengeId: string,
 *     challengeName: string,
 *     challengeDesc: string,
 *     challengeUrl: string
 *   }>
 * }
 */

// 필요한 Vue 컴포지션 API와 컴포넌트 임포트
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
```

### 3.3 반응형 디자인
화면 크기에 따라 자동으로 레이아웃이 조정됩니다. 반응형 디자인은 `Home.vue`의 스타일 섹션에서 정의됩니다.

```390:409:office-olympics-fe/src/pages/Home.vue
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

/**
 * 반응형 미디어 쿼리
 * - 1200px 미만: 2열 그리드
 * - 768px 미만: 1열 그리드
 */
@media (max-width: 1200px) {
  .challenges-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .challenges-grid {
    grid-template-columns: 1fr;
  }
}
```

## 4. 주요 기능 구현

### 4.1 상태 관리
- Pinia store를 사용한 전역 상태 관리
- 인증 상태 (`authStore`)
- 올림픽 정보 (`olympicStore`)
- 챌린지 데이터 (`challengeStore`)

### 4.2 데이터 흐름
```
사용자 액션 → Store 업데이트 → 컴포넌트 리렌더링 → UI 업데이트
```

## 5. 오류 처리
- 로딩 상태 표시
- 에러 메시지 표시
- 폴백 UI 제공
- 사용자 피드백

## 6. 성능 최적화
- 이미지 최적화
- 지연 로딩
- 캐시 활용
- 불필요한 리렌더링 방지
