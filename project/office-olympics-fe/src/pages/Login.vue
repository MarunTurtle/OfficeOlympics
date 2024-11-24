/**
 * @파일명: Login.vue
 * @설명: 사용자 로그인 페이지 컴포넌트
 * @관련백엔드:
 *   - POST /api/auth/login (로그인 요청)
 */

<template>
  <AuthLayout>
    <!-- 로그인 폼 컨테이너 -->
    <div class="auth-form">
      <h1 class="text-center mb-4">로그인</h1>
      <!-- 로그인 폼 - 이메일과 비밀번호 입력 -->
      <form @submit.prevent="onLogin">
        <!-- 이메일 입력 필드 -->
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input
            type="email"
            id="email"
            class="form-control input-field"
            v-model="email"
            :class="{ 'is-invalid': emailError }"
            placeholder="이메일을 입력하세요"
            required
          />
          <!-- 이메일 유효성 검사 에러 메시지 -->
          <div v-if="emailError" class="invalid-feedback">
            유효한 이메일 주소를 입력해주세요.
          </div>
        </div>

        <!-- 비밀번호 입력 필드 -->
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input
            type="password"
            id="password"
            class="form-control input-field"
            v-model="password"
            :class="{ 'is-invalid': passwordError }"
            placeholder="비밀번호를 입력하세요"
            required
          />
          <!-- 비밀번호 유효성 검사 에러 메시지 -->
          <div v-if="passwordError" class="invalid-feedback">
            비밀번호를 입력해주세요.
          </div>
        </div>

        <!-- 로그인 버튼 -->
        <button type="submit" class="btn btn-primary w-100">로그인</button>
      </form>

      <!-- 회원가입 링크 -->
      <p class="text-center mt-3">
        계정이 없으신가요?
        <RouterLink to="/auth/register" class="link-primary">회원가입</RouterLink>
      </p>
    </div>
  </AuthLayout>
</template>

<script setup>
/**
 * @컴포넌트명: Login
 * @설명: 사용자 로그인 처리 및 폼 유효성 검사
 * @상태:
 *   - email: 사용자 이메일
 *   - password: 사용자 비밀번호
 *   - emailError: 이메일 유효성 검사 에러
 *   - passwordError: 비밀번호 유효성 검사 에러
 */

import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useOlympicStore } from '@/stores/olympic';
import { isValidEmail, isNotEmpty } from '@/utils/validation';
import { useRouter } from 'vue-router';
import AuthLayout from '@/layouts/AuthLayout.vue';

// 라우터 및 상태 관리 초기화
const router = useRouter();
const authStore = useAuthStore();
const olympicStore = useOlympicStore();

// 폼 입력 상태 관리
const email = ref('');
const password = ref('');
const emailError = ref(false);
const passwordError = ref(false);

/**
 * 입력값 유효성 검사
 * @returns {boolean} 유효성 검사 통과 여부
 */
const validateInputs = () => {
  emailError.value = !isValidEmail(email.value);
  passwordError.value = !isNotEmpty(password.value);
  return !emailError.value && !passwordError.value;
};

/**
 * 로그인 처리 함수
 * - 입력값 검증
 * - 로그인 요청
 * - 올림픽 ID에 따른 페이지 이동
 */
const onLogin = async () => {
  if (!validateInputs()) {
    alert('입력 내용을 확인하고 다시 시도해주세요.');
    return;
  }

  try {
    await authStore.loginUser({ email: email.value, password: password.value });
    const olympicId = olympicStore.userOlympicId;

    // 올림픽 ID 존재 여부에 따른 리다이렉션
    if (olympicId) {
      router.push('/');
    } else {
      router.push('/olympic/create');
    }

    const nickname = authStore.user?.nickname || '사용자';
    alert(`환영합니다, ${nickname}님!`);
  } catch (error) {
    alert(error.message || '로그인에 실패했습니다. 다시 시도해주세요.');
  }
};
</script>

<style scoped>
/**
 * 로그인 폼 스타일링
 * - 중앙 정렬
 * - 최대 너비 제한
 * - 둥근 모서리와 여백
 */
.auth-form {
  max-width: 30rem;
  margin: 20px auto;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
}

/**
 * 입력 필드 스타일링
 * - 배경색 설정
 * - 부드러운 전환 효과
 * - 포커스 시 시각적 피드백
 */
.input-field {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

/* 입력 필드 포커스 효과 */
.input-field:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

/**
 * 버튼 스타일링
 * - 브랜드 컬러 사용
 * - 호버 효과
 * - 부드러운 전환
 */
.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

/* 버튼 호버 효과 */
.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

/**
 * 링크 스타일링
 * - 브랜드 컬러 사용
 * - 호버 효과
 */
.link-primary {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 600;
}

/* 링크 호버 효과 */
.link-primary:hover {
  color: var(--interaction-hover-color);
}

/* 에러 메시지 스타일링 */
.invalid-feedback {
  color: var(--alert-color);
  font-size: 0.875rem;
}

/* 제목 스타일링 */
h1 {
  color: var(--primary-color);
  font-weight: 600;
}
</style>
