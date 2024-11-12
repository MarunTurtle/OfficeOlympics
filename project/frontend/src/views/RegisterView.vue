<!-- src/views/Register.vue -->
<template>
  <div class="container">
    <form @submit.prevent="handleRegister" class="mt-4">
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input v-model="email" type="email" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Password</label>
        <input v-model="password" type="password" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary">Register</button>
    </form>
  </div>
</template>

<script>
import { register } from '@/api/auth';

export default {
  name: 'Register',
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async handleRegister() {
      try {
        await register(this.email, this.password);
        this.$router.push('/login');
      } catch (error) {
        console.error(error);
        // Error handling
      }
    },
  },
};
</script>

<style scoped>
/* Styles specific to Register.vue */
</style>
