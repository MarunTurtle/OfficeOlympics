<template>
  <!-- 전체 애플리케이션 래퍼 -->
  <div class="app-wrapper">
    <!-- 메인 콘텐츠 영역 (Navbar + RouterView) -->
    <div class="content-wrapper">
      <!-- 상단 네비게이션 바 -->
      <Navbar />

      <!-- 라우터에 따른 동적 콘텐츠 영역 -->
      <RouterView />
    </div>

    <!-- 하단 푸터 -->
    <Footer />
  </div>
</template>

<script setup>
/**
 * @컴포넌트: App.vue
 * @설명: 애플리케이션의 최상위 레이아웃 컴포넌트
 * @구조:
 *   - Navbar: 상단 네비게이션 바
 *   - RouterView: 동적 콘텐츠 영역
 *   - Footer: 하단 푸터
 */

import { onMounted } from 'vue';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';
import { useAuthStore } from '@/stores/auth';

// 인증 스토어 초기화
const authStore = useAuthStore();

/**
 * 컴포넌트 마운트 시 사용자 정보 로드
 * - 로컬 스토리지에서 저장된 사용자 정보가 있다면 복구
 */
onMounted(() => {
  if (!authStore.user) {
    authStore.loadUser();
  }
});
</script>

<style>
/* 전체 애플리케이션 래퍼 스타일 */
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 메인 콘텐츠 영역 스타일 */
.content-wrapper {
  flex: 1 0 auto; /* 남은 공간을 모두 차지하도록 설정 */
}
</style>
