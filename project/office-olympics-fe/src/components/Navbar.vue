/**
 * @파일명: Navbar.vue
 * @설명: 모든 페이지 상단에 표시되는 네비게이션 바 컴포넌트
 * @관련백엔드:
 *   - POST /api/auth/logout (로그아웃 요청)
 *   - 인증 상태에 따라 다른 메뉴 표시
 */

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
</template>

<script setup>
/**
 * @컴포넌트명: Navbar
 * @설명: 사용자 인증 상태에 따라 동적으로 메뉴를 표시하는 네비게이션 바
 * @의존성:
 *   - AuthStore: 사용자 인증 상태 관리
 *   - OlympicStore: 올림픽 생성 상태 관리
 */

import { computed } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const router = useRouter();

// 반응형 상태 계산
const isLoggedIn = computed(() => !!authStore.user);
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

/**
 * 로그아웃 처리 함수
 * 1. AuthStore의 logoutUser 액션 호출
 * 2. 성공 시 홈페이지로 리다이렉트
 * 3. 실패 시 에러 메시지 표시
 */
const onLogout = async () => {
  try {
    await authStore.logoutUser();
    alert("로그아웃되었습니다.");
    router.push('/');
  } catch (error) {
    console.error("Logout failed:", error);
    alert("로그아웃 중 오류가 발생했습니다.");
  }
};
</script>

<style scoped>
/*
  네비게이션 바 컴포넌트의 스타일 정의
  1. 컴포넌트 레이아웃
  2. 인터랙션 효과
  3. 버튼 스타일 변형
*/

/* 1. 기본 레이아웃 스타일 */
.navbar {
  background-color: var(--secondary-color);
}

.navbar-logo {
  height: 3.75rem;
  width: auto;
  object-fit: contain;
}

.nav-link {
  margin-left: 10px;
}

/* 2. 공통 버튼 스타일 */
.nav-button {
  /* 텍스트 스타일링 */
  font-size: 1rem;
  font-weight: 600;

  /* 여백 및 크기 */
  padding: 8px 16px;
  margin: 0 5px;

  /* 시각적 스타일링 */
  color: #fff;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;

  /* 인터랙션 */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

/* 3. 버튼 상태별 스타일 */
/* 호버 상태: 확대 및 색상 변경 */
.nav-button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}

/* 클릭 상태: 축소 효과 */
.nav-button:active {
  transform: scale(0.95);
}

/* 4. 버튼 변형 스타일 */
/* 경고 버튼: 올림픽 생성용 */
.nav-button.btn-warning {
  background-color: var(--warning-color);
}

/* 보조 버튼: 로그아웃용 */
.btn-tertiary {
  /* 텍스트 스타일링 */
  font-size: 1rem;
  font-weight: 600;

  /* 여백 및 크기 */
  padding: 8px 16px;
  margin: 0 5px;

  /* 시각적 스타일링 */
  color: #333;
  background-color: var(--tertiary-color);
  border: none;
  border-radius: 5px;

  /* 인터랙션 */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

/* 보조 버튼 상태별 스타일 */
.btn-tertiary:hover {
  background-color: var(--alert-color);
  transform: scale(1.05);
}

.btn-tertiary:active {
  transform: scale(0.95);
}
</style>
