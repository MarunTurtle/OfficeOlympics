# Office Olympics - 올림픽 이벤트 관리

## 목차
- [Office Olympics - 올림픽 이벤트 관리](#office-olympics---올림픽-이벤트-관리)
  - [목차](#목차)
  - [1. 기본 구조 이해하기](#1-기본-구조-이해하기)
    - [1.1 컴포넌트 이해하기](#11-컴포넌트-이해하기)
    - [1.2 데이터 흐름](#12-데이터-흐름)
    - [1.3 주요 개념](#13-주요-개념)
    - [1.4 일반적인 작업 흐름](#14-일반적인-작업-흐름)
  - [2. 개요](#2-개요)
  - [3. 올림픽 생성 (OlympicCreate.vue)](#3-올림픽-생성-olympiccreatevue)
    - [3.1 기본 구조](#31-기본-구조)
    - [3.2 주요 기능](#32-주요-기능)
  - [4. 올림픽 상세 정보 (OlympicDetail.vue)](#4-올림픽-상세-정보-olympicdetailvue)
    - [4.1 화면 구성](#41-화면-구성)
    - [4.2 데이터 구조](#42-데이터-구조)
  - [5. 참가자 관리](#5-참가자-관리)
    - [5.1 참가자 정보 표시](#51-참가자-정보-표시)
    - [5.2 상태 관리](#52-상태-관리)
  - [6. 올림픽 상태 관리 (Olympic Store)](#6-올림픽-상태-관리-olympic-store)
    - [6.1 주요 상태](#61-주요-상태)
    - [6.2 주요 액션](#62-주요-액션)
  - [7. 홈 화면 통합 (Home.vue)](#7-홈-화면-통합-homevue)
  - [8. 오류 처리](#8-오류-처리)

## 1. 기본 구조 이해하기

### 1.1 컴포넌트 이해하기
- **OlympicCreate.vue**: 올림픽 생성 폼
- **OlympicDetail.vue**: 올림픽 상세 정보 표시
- **Home.vue**: 메인 대시보드

### 1.2 데이터 흐름
```
사용자 입력 → 유효성 검사 → API 요청 → Pinia Store 업데이트 → UI 업데이트
```

### 1.3 주요 개념
1. **반응형 데이터**: `ref()`와 `reactive()`를 사용한 상태 관리
2. **컴포넌트 통신**: props와 이벤트를 통한 데이터 전달
3. **라우팅**: 페이지 간 이동 및 파라미터 처리
4. **상태 관리**: Pinia를 통한 중앙 집중식 상태 관리

### 1.4 일반적인 작업 흐름
1. 올림픽 생성
2. 참가자 등록
3. 상세 정보 확인
4. 순위표 확인

## 2. 개요
Office Olympics의 핵심 기능인 올림픽 이벤트 관리 시스템을 소개합니다. 이 시스템은 사내 올림픽 생성, 참가자 관리, 상세 정보 조회 등의 기능을 제공합니다.

## 3. 올림픽 생성 (OlympicCreate.vue)

### 3.1 기본 구조
올림픽 생성 페이지에서는 다음 정보를 입력받습니다:
- 올림픽 이벤트 이름
- 참가자 수 (최대 10명)
- 각 참가자의 정보

**사용된 컴포넌트**: `MainLayout`, `useOlympicStore`

참고 코드:

```8:48:office-olympics-fe/src/pages/OlympicCreate.vue
<script setup>
/**
 * @컴포넌트명: OlympicCreate
 * @설명: 올림픽 이벤트 생성 및 참가자 등록
 * @상태:
 *   - eventName: 올림픽 이벤트 이름
 *   - playerCount: 참가자 수
 *   - players: 참가자 목록
 *   - loading: 로딩 상태
 */

import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import { useOlympicStore } from "@/stores/olympic";

// 라우터 및 스토어 초기화
const router = useRouter();
const olympicStore = useOlympicStore();

// 폼 상태 관리
const eventName = ref("");
const playerCount = ref(0);
const players = ref([]);
const loading = ref(false);

// 유효성 검사 상태
const eventNameError = ref(false);
const playerCountError = ref(false);
const playerErrors = ref([]);

// 최대 참가자 수 제한
const maxPlayerCount = 10;

/**
 * 참가자 수 유효성 검사
 * @returns {boolean} 참가자 수가 유효한지 여부
 */
const isPlayerCountValid = computed(() => {
  return playerCount.value > 0 && playerCount.value <= maxPlayerCount;
});
```

### 3.2 주요 기능
- 실시간 유효성 검사
- 참가자 수에 따른 동적 폼 생성
- 올림픽 생성 후 자동 리다이렉션

## 4. 올림픽 상세 정보 (OlympicDetail.vue)

### 4.1 화면 구성
- 올림픽 기본 정보 표시
- 참가자 목록 표시
- 네비게이션 버튼

**사용된 컴포넌트**: `MainLayout`, `RouterLink`

참고 코드:

```9:38:office-olympics-fe/src/pages/OlympicDetail.vue
<template>
  <MainLayout>
    <!-- 올림픽 상세 정보 컨테이너 -->
    <div class="olympic-detail">
      <!-- 올림픽 기본 정보 -->
      <h1 class="text-center mb-4">{{ olympic?.name }}</h1>
      <p class="text-center text-muted">올림픽 ID: {{ olympic?.id }}</p>

      <!-- 참가자 목록 섹션 -->
      <div class="player-list mt-5">
        <h2>플레이어</h2>
        <ul class="list-group">
          <!-- 각 참가자 정보 -->
          <li
            v-for="(player, index) in players"
            :key="player.id"
            class="list-group-item"
          >
            <strong>플레이어 {{ index + 1 }}:</strong> {{ player.nickname }}
          </li>
        </ul>
      </div>

      <!-- 네비게이션 버튼 -->
      <div class="mt-5 text-center">
        <RouterLink to="/main" class="btn btn-secondary">홈으로 돌아가기</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>
```

### 4.2 데이터 구조
```javascript
olympic: {
  id: string,
  name: string,
  createdAt: Date
}

players: [{
  id: string,
  nickname: string,
  score: number
}]
```

## 5. 참가자 관리

### 5.1 참가자 정보 표시
- 참가자 목록 조회
- 점수 현황 확인
- 프로필 정보 표시

**사용된 컴포넌트**: `useUserStore`, `useAuthStore`

### 5.2 상태 관리
MyPage 컴포넌트에서 참가자 정보를 관리합니다:

참고 코드:

```131:178:office-olympics-fe/src/pages/MyPage.vue

// 라우터 및 스토어 초기화
const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();

// 상태 관리
const userData = ref(null);
const players = ref([]);
const loading = ref(true);
const error = ref(null);
const editProfileModal = ref(null);

/**
 * 사용자 프로필 정보 조회
 * - 사용자 ID로 프로필 데이터 요청
 * - 프로필 이미지 처리
 * - 올림픽 참가자 정보 설정
 */
const fetchUserProfile = async () => {
  try {
    loading.value = true;
    error.value = null;

    console.log('Auth Store User:', authStore.user);
    const userId = authStore.user?.id || authStore.user?.userId;
    console.log('Extracted User ID:', userId);

    if (!userId) {
      throw new Error('No user ID found');
    }

    const response = await userStore.fetchUser(userId);
    console.log('Profile Response:', response);

    userData.value = {
      ...response.data.userData,
      imgSrc: response.data.userData.ImgSrc || response.data.userData.imgSrc || defaultProfileImage
    };
    players.value = response.data.players;

  } catch (err) {
    console.error('Error fetching profile:', err);
    error.value = err.message || 'Failed to load profile data';
  } finally {
    loading.value = false;
  }
};
```

## 6. 올림픽 상태 관리 (Olympic Store)

### 6.1 주요 상태
```javascript
state: {
  currentOlympic: null,    // 현재 올림픽 정보
  participants: [],        // 참가자 목록
  leaderboard: [],        // 순위표
  loading: false,         // 로딩 상태
  error: null            // 에러 상태
}
```

### 6.2 주요 액션
- createOlympic: 새 올림픽 생성
- fetchOlympicDetails: 올림픽 정보 조회
- updateParticipants: 참가자 정보 업데이트
- fetchLeaderboard: 순위표 조회

## 7. 홈 화면 통합 (Home.vue)
홈 화면에서는 사용자의 상태에 따라 다른 화면을 보여줍니다:

**사용된 컴포넌트**: `MainLayout`, `ChallengeCard`, `useAuthStore`, `useOlympicStore`, `useChallengeStore`

참고 코드:

```9:173:office-olympics-fe/src/pages/Home.vue
<script setup>
/**
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
import MainLayout from "@/layouts/MainLayout.vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useChallengeStore } from "@/stores/challenge";
import ChallengeCard from "@/components/ChallengeCard.vue";
import { formatNumber } from "@/utils/formatters";

/**
 * Store 초기화
 * - authStore: 인증 상태 관리
 * - olympicStore: 올림픽 정보 관리
 * - challengeStore: 챌린지 데이터 관리
 */
const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const challengeStore = useChallengeStore();

/**
 * 컴퓨티드 속성
 * - isLoggedIn: 사용자 로그인 상태
 * - hasOlympics: 올림픽 존재 여부
 */
const isLoggedIn = computed(() => authStore.isAuthenticated);
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

/**
 * 로컬 상태 관리
 * - leaderboard: 순위표 데이터
 * - errorMessage: 에러 메시지
 * - loading: 로딩 상태
 */
const leaderboard = ref([]);
const errorMessage = ref("");
const loading = ref(false);

/**
 * 슬라이드쇼 기능 구현
 * - 9개의 이미지를 순환
 * - 5초마다 이미지 변경
 * @returns {string[]} 이미지 URL 배열
 */
const images = Array.from(
  { length: 9 },
  (_, i) => new URL(`../assets/images/mainpage/mp${i + 1}.png`, import.meta.url).href
);
const currentImageIndex = ref(Math.floor(Math.random() * images.length));
const slideInterval = ref(null);

/**
 * 슬라이드쇼 시작 함수
 * 로그인하지 않았거나 올림픽이 없는 경우에만 실행
 */
const startSlideshow = () => {
  if (!isLoggedIn.value || !hasOlympics.value) {
    slideInterval.value = setInterval(() => {
      currentImageIndex.value = (currentImageIndex.value + 1) % images.length;
    }, 5000);
  }
};

/**
 * 순위별 스타일 클래스 반환
 * @param {number} rank - 순위
 * @returns {string} CSS 클래스명
 */
const getRankClass = (rank) => {
  if (rank === 1) return 'rank-1';
  if (rank === 2) return 'rank-2';
  if (rank === 3) return 'rank-3';
  return '';
};

/**
 * 점수 포맷팅 함수
 * @param {number} score - 포맷팅할 점수
 * @returns {string} 포맷팅된 점수 문자열
 */
const formatScore = (score) => {
  if (score === undefined || score === null) return '0';
  return formatNumber(score);
};

/**
 * 컴포넌트 마운트 시 초기화
 * - 슬라이드쇼 시작
 * - 메인 페이지 데이터 로드
 * - 올림픽 ID 확인 및 순위표 로드
 */
onMounted(async () => {
  startSlideshow();

  try {
    loading.value = true;
    await challengeStore.fetchMainPageData();

    const olympicId = olympicStore.userOlympicId || localStorage.getItem('olympicsId');

    if (olympicId && isLoggedIn.value) {
      olympicStore.setUserOlympicId(olympicId);
      leaderboard.value = challengeStore.leaderboard;
    }
  } catch (err) {
    console.error('Error loading main page data:', err);
    errorMessage.value = err.response?.data?.message || "데이터 로딩에 실패했습니다. 다시 시도해주세요.";
  } finally {
    loading.value = false;
  }
});

/**
 * 컴포넌트 언마운트 시 정리
 * - 슬라이드쇼 인터벌 제거
 */
onBeforeUnmount(() => {
  if (slideInterval.value) {
    clearInterval(slideInterval.value);
  }
});
</script>

<template>
  <MainLayout>
    <div class="home-page">
      <!--
        Case 1: 로그인 + 올림픽 있음
        - 순위표 표시
        - 추천 챌린지 목록 표시
      -->
      <template v-if="isLoggedIn && hasOlympics">
        <div class="leaderboard-section">
          <h2 class="text-center mb-4">현재 순위</h2>

          <!-- 에러 메시지 표시 -->
          <div v-if="errorMessage" class="alert alert-danger text-center">
            {{ errorMessage }}
          </div>

          <!-- 순위 데이터 없음 메시지 -->
          <div v-else-if="leaderboard.length === 0" class="text-center">
            아직 순위가 없습니다.
          </div>
```

## 8. 오류 처리
에러 발생 시 사용자 친화적인 메시지를 표시합니다:

**사용된 컴포넌트**: `MainLayout`, `RouterLink`

참고 코드:

```7:28:office-olympics-fe/src/pages/Error.vue
<template>
  <MainLayout>
    <!--
      에러 페이지 컨테이너
      중앙 정렬된 에러 메시지와 홈 버튼 표시
    -->
    <div class="error-page">
      <!-- 에러 제목 -->
      <h1 class="text-danger text-center">앗! 문제가 발생했습니다.</h1>

      <!-- 에러 설명 -->
      <p class="text-center">
        찾으시는 페이지가 삭제되었거나, 일시적으로 사용할 수 없습니다.
      </p>

      <!-- 홈으로 이동하는 네비게이션 버튼 -->
      <div class="text-center mt-4">
        <RouterLink to="/" class="btn btn-primary">홈으로 이동</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>
```
