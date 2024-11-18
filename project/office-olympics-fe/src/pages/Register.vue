<template>
  <AuthLayout>
    <div class="register-form">
      <h1 class="text-center mb-4">Register</h1>
      <form @submit.prevent="onRegister">
        <div class="mb-3">
          <label for="name" class="form-label">Name</label>
          <input
            type="text"
            id="name"
            class="form-control"
            v-model="name"
            placeholder="Enter your full name"
            required
          />
          <small v-if="!isNotEmpty(name)" class="text-danger">
            Name is required.
          </small>
        </div>

        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input
            type="email"
            id="email"
            class="form-control"
            v-model="email"
            placeholder="Enter your email"
            required
          />
          <small v-if="!isValidEmail(email)" class="text-danger">
            Please enter a valid email.
          </small>
        </div>

        <div class="mb-3">
          <label for="nickname" class="form-label">Nickname</label>
          <input
            type="text"
            id="nickname"
            class="form-control"
            v-model="nickname"
            placeholder="Enter a unique nickname"
            required
          />
          <small v-if="!isNotEmpty(nickname)" class="text-danger">
            Nickname is required.
          </small>
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input
            type="password"
            id="password"
            class="form-control"
            v-model="password"
            placeholder="Enter your password"
            required
          />
          <small v-if="!isValidPassword(password)" class="text-danger">
            Password must be at least 8 characters and include letters and numbers.
          </small>
        </div>

        <div class="mb-3">
          <label for="confirm-password" class="form-label">Confirm Password</label>
          <input
            type="password"
            id="confirm-password"
            class="form-control"
            v-model="confirmPassword"
            placeholder="Confirm your password"
            required
          />
          <small
            v-if="password.value && password.value !== confirmPassword"
            class="text-danger"
          >
            Passwords do not match.
          </small>
        </div>

        <button
          type="submit"
          class="btn btn-primary w-100"
          :disabled="
            !isNotEmpty(name) ||
            !isValidEmail(email) ||
            !isNotEmpty(nickname) ||
            !isValidPassword(password) ||
            password !== confirmPassword
          "
        >
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
import { ref } from 'vue';
import { isValidEmail, isValidPassword, isNotEmpty } from '@/utils/validation';
import AuthLayout from '@/layouts/AuthLayout.vue';

const name = ref('');
const email = ref('');
const nickname = ref('');
const password = ref('');
const confirmPassword = ref('');

const onRegister = () => {
  if (
    isNotEmpty(name.value) &&
    isValidEmail(email.value) &&
    isNotEmpty(nickname.value) &&
    isValidPassword(password.value) &&
    password.value === confirmPassword.value
  ) {
    console.log('User registered:', {
      name: name.value,
      email: email.value,
      nickname: nickname.value,
      password: password.value,
    });
    alert('Registration successful!');
  } else {
    alert('Please fill in all fields correctly.');
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
</style>
