<template>
  <div class="container">
    <form @submit.prevent="handleLogin" class="mt-4">
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input v-model="email" type="email" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Password</label>
        <input v-model="password" type="password" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary">Login</button>
    </form>
  </div>
</template>

<script>
import { login } from '@/api/auth';

export default {
  name: 'Login',
  data() {
    return {
      email: '',
      password: '',
      errorMessage: '', // Added for displaying errors
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await login(this.email, this.password);
        console.log('Login successful:', response.data);
        this.$router.push('/'); // Redirect to the home page
      } catch (error) {
        console.error(error);
        this.errorMessage = 'Invalid email or password';
        alert(this.errorMessage); // Temporary alert for error feedback
      }
    },
  },
};
</script>

<style scoped>
/* Styles specific to LoginView.vue */
</style>
