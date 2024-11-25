/**
 * @파일명: Navbar.vue
 * @설명: 사용자의 인증 상태에 따라 다른 메뉴를 표시, 반응형 디자인을 지원하는 상단 네비게이션 바
 * @관련백엔드:
 *   - POST /api/auth/logout (로그아웃 요청)
 *   - 인증 상태에 따라 다른 메뉴 표시
 */

<template>
  <!-- 반응형 네비게이션 바 컨테이너 -->
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
 * 네비게이션 바 로직
 *
 * @의존성
 * - AuthStore: 사용자 인증 상태 관리
 * - OlympicStore: 올림픽 데이터 상태 관리
 * - Router: 페이지 네비게이션
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
  네비게이션 바 스타일링

  1. 기본 레이아웃
    - 로고 크기와 여백 설정
    - 기본 배경색 및 정렬

  2. 버튼 스타일
    - 기본/경고/보조 버튼 변형
    - 호버 및 클릭 인터랙션

  3. 반응형 디자인
    - 모바일 뷰에서의 메뉴 표시
    - 터치 인터랙션 최적화
*/

/* 기본 레이아웃 */
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

/* 버튼 기본 스타일 */
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

/* 반응형 스타일 */
@media (max-width: 991.98px) { /* Bootstrap's lg breakpoint */
  .navbar-nav {
    width: 100%;
    display: flex;
    align-items: center; /* Center items vertically */
  }

  .nav-item {
    width: 100%;
    margin: 5px auto; /* Changed from '5px 0' to '5px auto' for horizontal centering */
    display: flex;
    justify-content: center; /* Center the button container */
  }

  .nav-button,
  .btn-tertiary {
    width: 95%;
    display: block;
    text-align: center;
    border-radius: 5px !important;
    transition: background-color 0.3s, transform 0.2s !important;
  }

  /* Reduce hover scale effect for mobile */
  .nav-button:hover,
  .btn-tertiary:hover {
    transform: scale(1.02); /* Reduced from 1.05 to 1.02 */
  }

  .nav-button:active,
  .btn-tertiary:active {
    transform: scale(0.98); /* Adjusted from 0.95 to 0.98 */
  }

  .navbar-collapse {
    transition: height 0.3s !important;
  }
}
</style>
