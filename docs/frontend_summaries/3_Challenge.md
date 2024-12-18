# Office Olympics - 챌린지 시스템

## 목차
- [Office Olympics - 챌린지 시스템](#office-olympics---챌린지-시스템)
  - [목차](#목차)
  - [1. 기본 구조 이해하기](#1-기본-구조-이해하기)
    - [1.1 주요 컴포넌트 이해하기](#11-주요-컴포넌트-이해하기)
    - [1.2 데이터 흐름](#12-데이터-흐름)
    - [1.3 주요 작업 순서](#13-주요-작업-순서)
    - [1.4 유용한 팁](#14-유용한-팁)
  - [2. 개요](#2-개요)
  - [3. 챌린지 상태 관리 (Challenge Store)](#3-챌린지-상태-관리-challenge-store)
    - [3.1 주요 상태](#31-주요-상태)
    - [3.2 핵심 기능](#32-핵심-기능)
  - [4. 챌린지 목록 및 상세 정보](#4-챌린지-목록-및-상세-정보)
    - [4.1 챌린지 상세 페이지 (ChallengeDetail.vue)](#41-챌린지-상세-페이지-challengedetailvue)
    - [4.2 홈 페이지 챌린지 목록](#42-홈-페이지-챌린지-목록)
  - [5. 챌린지 점수 입력](#5-챌린지-점수-입력)
    - [5.1 점수 입력 페이지 (ChallengeScore.vue)](#51-점수-입력-페이지-challengescorevue)
    - [5.2 점수 제출 처리](#52-점수-제출-처리)
  - [6. 챌린지 순위 표시](#6-챌린지-순위-표시)
    - [6.1 순위 페이지 (ChallengeRank.vue)](#61-순위-페이지-challengerankvue)
  - [7. 오류 처리](#7-오류-처리)
  - [8. 성능 최적화](#8-성능-최적화)

## 1. 기본 구조 이해하기

### 1.1 주요 컴포넌트 이해하기
1. **ChallengeDetail**: 챌린지 상세 정보를 보여주는 페이지
2. **ChallengeScore**: 점수를 입력하는 페이지
3. **ChallengeRank**: 순위를 확인하는 페이지

### 1.2 데이터 흐름
```
사용자 액션 → Challenge Store → API 요청 → 데이터 업데이트 → UI 반영
```

### 1.3 주요 작업 순서
1. 챌린지 목록 확인
2. 챌린지 선택 및 상세 정보 확인
3. 챌린지 수행
4. 점수 입력
5. 순위 확인

### 1.4 유용한 팁
- 모든 폼은 유효성 검사를 포함
- 로딩 상태와 에러 처리가 구현되어 있음
- 반응형 디자인으로 모바일에서도 사용 가능
- 컴포넌트 간 데이터는 Pinia store를 통해 관리

## 2. 개요
챌린지 시스템은 Office Olympics의 핵심 기능으로, 사용자들이 다양한 챌린지에 참여하고 점수를 기록하며 순위를 확인할 수 있는 시스템입니다.

## 3. 챌린지 상태 관리 (Challenge Store)

### 3.1 주요 상태
Pinia store에서 관리하는 주요 상태들:

```35:43:office-olympics-fe/src/stores/challenge.js
  state: () => ({
    leaderboard: [],
    loading: false,
    challenges: [],
    comments: [],
    challenge: null,
    currentChallenge: null,
    rankings: []
  }),
```

### 3.2 핵심 기능
- **챌린지 목록 조회**: `ChallengeList.vue` 컴포넌트를 통해 구현
- **상세 정보 조회**: `ChallengeDetail.vue` 컴포넌트를 통해 구현
- **리더보드 관리**: `Leaderboard.vue` 컴포넌트를 통해 구현
- **점수 제출**: `ChallengeScore.vue` 컴포넌트를 통해 구현

## 4. 챌린지 목록 및 상세 정보

### 4.1 챌린지 상세 페이지 (ChallengeDetail.vue)
- 챌린지 정보 표시
- 관련 챌린지 추천
- 반응형 디자인

주요 구조:

```8:26:office-olympics-fe/src/pages/ChallengeDetail.vue
<template>
  <MainLayout>
    <div class="challenge-detail container">
      <!-- 로딩 상태 표시 -->
      <div v-if="loading" class="text-center my-5 py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">로딩중...</span>
        </div>
      </div>

      <!-- 챌린지 상세 정보 표시 -->
      <div v-else-if="challenge" class="row g-4">
        <!-- 메인 콘텐츠 영역 (9칸) -->
        <div class="col-lg-9">
          <!-- 비디오 섹션: YouTube 임베드 -->
          <div class="content-section">
            <div class="video-container mb-4">
              <!-- 16:9 비율 유지를 위한 래퍼 -->
              <div class="video-wrapper">
```

### 4.2 홈 페이지 챌린지 목록
홈 페이지에서는 사용자 상태에 따라 다른 챌린지 정보를 표시:

```11:28:office-olympics-fe/src/pages/Home.vue
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
```

## 5. 챌린지 점수 입력

### 5.1 점수 입력 페이지 (ChallengeScore.vue)
- 사용자 친화적인 점수 입력 폼
- 실시간 유효성 검사
- 제출 상태 피드백

기본 구조:

```9:26:office-olympics-fe/src/pages/ChallengeScore.vue
<template>
  <MainLayout>
    <div class="score-submission container py-4">
      <h1 class="text-center mb-5">챌린지 점수 제출</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!-- 로딩 상태 표시 -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!-- 에러 메시지 표시 -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>
```

### 5.2 점수 제출 처리
점수 제출 로직:

```107:117:office-olympics-fe/src/stores/challenge.js
    async submitScore(challengeId, scoreData) {
      try {
        console.log('Store: submitting score data:', scoreData);
        const response = await submitChallengeScore(challengeId, scoreData);
        console.log('Store: score submission response:', response.data);
        return response.data;
      } catch (error) {
        console.error('Store: failed to submit score:', error.response?.data || error);
        throw error;
      }
    },
```

## 6. 챌린지 순위 표시

### 6.1 순위 페이지 (ChallengeRank.vue)
- 실시간 순위 표시
- 애니메이션 효과
- 사용자 친화적 UI

기본 구조:

```8:31:office-olympics-fe/src/pages/ChallengeRank.vue
<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">챌린지 결과</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!--
            로딩 상태 표시
            데이터 로딩 중일 때 스피너 표시
          -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!--
            에러 상태 표시
            데이터 로딩 실패 시 에러 메시지 표시
          -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>
```

## 7. 오류 처리
모든 주요 작업에는 적절한 에러 처리가 구현되어 있습니다:
- 네트워크 오류
- 유효성 검사 실패
- 서버 오류
- 사용자 피드백

## 8. 성능 최적화
- 동적 임포트를 통한 코드 스플리팅
- 캐싱 전략
- 효율적인 상태 관리
- 최적화된 API 호출
