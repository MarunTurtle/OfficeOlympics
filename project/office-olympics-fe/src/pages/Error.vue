/**
 * @파일명: Error.vue
 * @설명: 404 및 기타 에러 상황을 처리하는 에러 페이지 컴포넌트
 * @관련백엔드: 없음 (순수 프론트엔드 컴포넌트)
 */

<template>
  <MainLayout>
    <div class="auth-form">
      <h1 class="text-center mb-4 text-danger">앗! 문제가 발생했습니다.</h1>
      <div class="error-content">
        <img
          src="@/assets/images/error.png"
          alt="Office Olympics Error"
          class="error-image mb-4"
        />
        <p class="text-center text-muted mb-4">
          찾으시는 페이지가 삭제되었거나, 일시적으로 사용할 수 없습니다.
        </p>
        <div class="text-center">
          <RouterLink to="/" class="btn btn-primary w-100">
            홈으로 이동
          </RouterLink>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
</script>

<style scoped>
.auth-form {
  max-width: 40rem;
  margin: 20px auto;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
}

.error-image {
  width: 100%;
  max-width: 40rem;
  height: auto;
  display: block;
  margin: 0 auto;
  border-radius: 8px;
}

.error-content {
  border-radius: 8px;
  padding: 1.5rem;
  text-align: center;
}

h1 {
  color: var(--alert-color);
  font-weight: 600;
}

p {
  font-size: 1.1rem;
}

.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}
</style>
