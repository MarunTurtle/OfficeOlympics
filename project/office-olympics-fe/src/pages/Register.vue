/**
 * @파일명: Register.vue
 * @설명: 사용자 회원가입 페이지 컴포넌트
 * @관련백엔드:
 *   - POST /api/auth/register (회원가입 요청)
 */

<template>
  <AuthLayout>
    <!-- 회원가입 폼 컨테이너 -->
    <div class="register-form">
      <h1 class="text-center mb-4">회원가입</h1>
      <form @submit.prevent="onRegister">
        <!-- 이름 입력 필드 -->
        <div class="mb-3">
          <label for="name" class="form-label">이름</label>
          <input type="text" id="name" class="form-control" v-model="name"
            placeholder="예시: 이케빈" @blur="nameTouched = true" />
          <!-- 이름 유효성 검사 에러 메시지 -->
          <small v-if="nameTouched && !isNotEmpty(name)" class="text-danger">
            이름을 입력해주세요.
          </small>
        </div>

        <!-- 이메일 입력 필드 -->
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input type="email" id="email" class="form-control" v-model="email" placeholder="예시: ssafy@example.com"
            @blur="emailTouched = true" />
          <small v-if="emailTouched && !isValidEmail(email)" class="text-danger">
            유효한 이메일을 입력해주세요.
          </small>
        </div>

        <!-- 닉네임 입력 필드 -->
        <div class="mb-3">
          <label for="nickname" class="form-label">닉네임</label>
          <input type="text" id="nickname" class="form-control" v-model="nickname" placeholder="예시: 신입케빈"
            @blur="nicknameTouched = true" />
          <small v-if="nicknameTouched && !isNotEmpty(nickname)" class="text-danger">
            닉네임을 입력해주세요.
          </small>
        </div>

        <!-- 비밀번호 입력 필드 -->
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" id="password" class="form-control" v-model="password" placeholder="비밀번호를 입력하세요"
            @blur="passwordTouched = true" />
          <small v-if="passwordTouched && !isValidPassword(password)" class="text-danger">
            비밀번호는 최소 8자 이상이어야 합니다.
          </small>
        </div>

        <!-- 비밀번호 확인 필드 -->
        <div class="mb-3">
          <label for="confirm-password" class="form-label">비밀번호 확인</label>
          <input type="password" id="confirm-password" class="form-control" v-model="confirmPassword"
            placeholder="비밀번호를 다시 입력하세요" @blur="confirmPasswordTouched = true" />
          <small v-if="confirmPasswordTouched && password !== confirmPassword" class="text-danger">
            비밀번호가 일치하지 않습니다.
          </small>
        </div>

        <!-- 프로필 이미지 업로드 -->
        <div class="mb-3">
          <label for="profile-img" class="form-label">프로필 이미지 (선택사항)</label>
          <input type="file" id="profile-img" class="form-control" @change="onFileChange" />
        </div>

        <!-- 제출 버튼 -->
        <button type="submit" class="btn btn-primary w-100" :disabled="!isFormValid">
          가입하기
        </button>
      </form>

      <!-- 로그인 페이지 링크 -->
      <p class="text-center mt-3">
        이미 계정이 있으신가요?
        <RouterLink to="/auth/login">로그인하기</RouterLink>
      </p>
    </div>
  </AuthLayout>
</template>

<script setup>
/**
 * @컴포넌트명: Register
 * @설명: 사용자 회원가입 처리 및 폼 유효성 검사
 * @상태:
 *   - name: 사용자 이름
 *   - email: 이메일
 *   - nickname: 닉네임
 *   - password: 비밀번호
 *   - confirmPassword: 비밀번호 확인
 *   - profileImg: 프로필 이미지 파일
 */

import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { isValidEmail, isValidPassword, isNotEmpty } from '@/utils/validation';
import AuthLayout from '@/layouts/AuthLayout.vue';
import router from '@/router';

// 스토어 초기화
const authStore = useAuthStore();

// 폼 입력 상태 관리
const name = ref('');
const email = ref('');
const nickname = ref('');
const password = ref('');
const confirmPassword = ref('');
const profileImg = ref(null);

// 입력 필드 터치 상태 관리
const nameTouched = ref(false);
const emailTouched = ref(false);
const nicknameTouched = ref(false);
const passwordTouched = ref(false);
const confirmPasswordTouched = ref(false);

/**
 * 파일 입력 변경 핸들러
 * @param {Event} e - 파일 입력 이벤트
 */
const onFileChange = (e) => {
  profileImg.value = e.target.files[0];
};

/**
 * 폼 유효성 검사
 * @returns {boolean} 폼이 유효한지 여부
 */
const isFormValid = computed(() => {
  return (
    isNotEmpty(name.value) &&
    isValidEmail(email.value) &&
    isNotEmpty(nickname.value) &&
    isValidPassword(password.value) &&
    password.value === confirmPassword.value
  );
});

/**
 * 회원가입 제출 처리
 * - FormData 구성
 * - API 호출
 * - 성공 시 로그인 페이지로 리다이렉션
 */
const onRegister = async () => {
  try {
    // Construct FormData for multipart request
    const formData = new FormData();
    formData.append('email', email.value);
    formData.append('password', password.value);
    formData.append('name', name.value);
    formData.append('nickname', nickname.value);
    if (profileImg.value) {
      formData.append('profileImg', profileImg.value);
    }

    // Call the API
    await authStore.registerUser(formData);
    router.push('/auth/login');
  } catch (error) {
    console.error('Registration failed:', error);
    alert('An error occurred during registration.');
  }
};
</script>

<style scoped>
/**
 * 회원가입 폼 스타일링
 * - 최대 너비 제한
 * - 중앙 정렬
 * - 둥근 모서리와 여백
 */
.register-form {
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
.form-control {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
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

.text-danger {
  color: var(--alert-color);
  font-size: 0.875rem;
}

h1 {
  color: var(--primary-color);
  font-weight: 600;
}

a {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 600;
}

a:hover {
  color: var(--interaction-hover-color);
}
</style>
