<template>
  <AuthLayout>
    <div class="auth-form">
      <h1 class="text-center mb-4">로그인</h1>
      <form @submit.prevent="onLogin">
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
          <div v-if="emailError" class="invalid-feedback">
            유효한 이메일 주소를 입력해주세요.
          </div>
        </div>
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
          <div v-if="passwordError" class="invalid-feedback">
            비밀번호를 입력해주세요.
          </div>
        </div>
        <button type="submit" class="btn btn-primary w-100">로그인</button>
      </form>
      <p class="text-center mt-3">
        계정이 없으신가요?
        <RouterLink to="/auth/register" class="link-primary">회원가입</RouterLink>
      </p>
    </div>
  </AuthLayout>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useOlympicStore } from '@/stores/olympic';
import { isValidEmail, isNotEmpty } from '@/utils/validation'; // Import validation functions
import { useRouter } from 'vue-router';
import AuthLayout from '@/layouts/AuthLayout.vue';

const router = useRouter();
const email = ref('');
const password = ref('');
const emailError = ref(false);
const passwordError = ref(false);
const authStore = useAuthStore();
const olympicStore = useOlympicStore();

const validateInputs = () => {
  emailError.value = !isValidEmail(email.value);
  passwordError.value = !isNotEmpty(password.value);

  return !emailError.value && !passwordError.value;
};

const onLogin = async () => {
  if (!validateInputs()) {
    alert('입력 내용을 확인하고 다시 시도해주세요.');
    return;
  }

  try {
    // Login and set user
    await authStore.loginUser({ email: email.value, password: password.value });
    const olympicId = olympicStore.userOlympicId;

    // Navigate based on Olympic ID
    if (olympicId) {
      router.push('/'); // Redirect to home page if Olympic ID exists
    } else {
      router.push('/olympic/create'); // Redirect to Olympic creation page otherwise
    }

    // Use the correct nickname from the auth store
    const nickname = authStore.user?.nickname || '사용자';
    alert(`환영합니다, ${nickname}님!`);
  } catch (error) {
    alert(error.message || '로그인에 실패했습니다. 다시 시도해주세요.');
  }
};

</script>

<style scoped>
.auth-form {
  max-width: 30rem;
  margin: 20px auto;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); */
}

.input-field {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.input-field:focus {
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

.link-primary {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 600;
}

.link-primary:hover {
  color: var(--interaction-hover-color);
}

.invalid-feedback {
  color: var(--alert-color);
  font-size: 0.875rem;
}

h1 {
  color: var(--primary-color);
  font-weight: 600;
}
</style>
