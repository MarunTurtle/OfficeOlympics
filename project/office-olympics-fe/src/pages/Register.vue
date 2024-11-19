<template>
  <AuthLayout>
    <div class="register-form">
      <h1 class="text-center mb-4">Register</h1>
      <form @submit.prevent="onRegister">
        <div class="mb-3">
          <label for="name" class="form-label">Name</label>
          <input type="text" id="name" class="form-control" v-model="name" placeholder="Enter your full name"
            @blur="nameTouched = true" />
          <small v-if="nameTouched && !isNotEmpty(name)" class="text-danger">
            Name is required.
          </small>
        </div>

        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="email" id="email" class="form-control" v-model="email" placeholder="Enter your email"
            @blur="emailTouched = true" />
          <small v-if="emailTouched && !isValidEmail(email)" class="text-danger">
            Please enter a valid email.
          </small>
        </div>

        <div class="mb-3">
          <label for="nickname" class="form-label">Nickname</label>
          <input type="text" id="nickname" class="form-control" v-model="nickname" placeholder="Enter a unique nickname"
            @blur="nicknameTouched = true" />
          <small v-if="nicknameTouched && !isNotEmpty(nickname)" class="text-danger">
            Nickname is required.
          </small>
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input type="password" id="password" class="form-control" v-model="password" placeholder="Enter your password"
            @blur="passwordTouched = true" />
          <small v-if="passwordTouched && !isValidPassword(password)" class="text-danger">
            Password must be at least 8 characters.
          </small>
        </div>

        <div class="mb-3">
          <label for="confirm-password" class="form-label">Confirm Password</label>
          <input type="password" id="confirm-password" class="form-control" v-model="confirmPassword"
            placeholder="Confirm your password" @blur="confirmPasswordTouched = true" />
          <small v-if="confirmPasswordTouched && password !== confirmPassword" class="text-danger">
            Passwords do not match.
          </small>
        </div>

        <div class="mb-3">
          <label for="profile-img" class="form-label">Profile Image (Optional)</label>
          <input type="file" id="profile-img" class="form-control" @change="onFileChange" />
        </div>

        <button type="submit" class="btn btn-primary w-100" :disabled="!isFormValid">
          Sign Up
        </button>
      </form>

      <p class="text-center mt-3">
        Already have an account?
        <RouterLink to="/login">Login here</RouterLink>.
      </p>
    </div>
  </AuthLayout>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { isValidEmail, isValidPassword, isNotEmpty } from '@/utils/validation';
import AuthLayout from '@/layouts/AuthLayout.vue';
import router from '@/router';

const authStore = useAuthStore();

const name = ref('');
const email = ref('');
const nickname = ref('');
const password = ref('');
const confirmPassword = ref('');
const profileImg = ref(null);

const nameTouched = ref(false);
const emailTouched = ref(false);
const nicknameTouched = ref(false);
const passwordTouched = ref(false);
const confirmPasswordTouched = ref(false);

// File input change handler
const onFileChange = (e) => {
  profileImg.value = e.target.files[0];
};

// Check if form is valid
const isFormValid = computed(() => {
  return (
    isNotEmpty(name.value) &&
    isValidEmail(email.value) &&
    isNotEmpty(nickname.value) &&
    isValidPassword(password.value) &&
    password.value === confirmPassword.value
  );
});

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
.register-form {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.text-danger {
  color: var(--alert-color);
  font-size: 0.875rem;
}
</style>
