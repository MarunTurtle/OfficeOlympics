<template>
  <AuthLayout>
    <div class="login-form">
      <h1 class="text-center">Login</h1>
      <form @submit.prevent="onLogin">
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input
            type="email"
            id="email"
            class="form-control"
            v-model="email"
            :class="{ 'is-invalid': emailError }"
            required
          />
          <div v-if="emailError" class="invalid-feedback">
            Please enter a valid email address.
          </div>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input
            type="password"
            id="password"
            class="form-control"
            v-model="password"
            :class="{ 'is-invalid': passwordError }"
            required
          />
          <div v-if="passwordError" class="invalid-feedback">
            Password cannot be empty.
          </div>
        </div>
        <button type="submit" class="btn btn-primary w-100">Login</button>
      </form>
      <p class="text-center mt-3">
        Don't have an account?
        <RouterLink to="/auth/register">Sign Up</RouterLink>
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
    alert('Please correct the errors and try again.');
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

    alert(`Welcome, ${authStore.user.nickname}!`);
  } catch (error) {
    alert(error.message || 'Login failed. Please try again.');
  }
};
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.is-invalid {
  border-color: #f2668b;
  /* Alert/Emergency Color */
}

.invalid-feedback {
  color: #f2668b;
  /* Alert/Emergency Color */
}
</style>
