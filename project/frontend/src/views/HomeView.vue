<template>
  <div class="container">
    <h1 class="mt-4">Welcome to Office Olympics!</h1>

    <!-- Login and Register Links -->
    <div class="d-flex justify-content-end my-4">
      <router-link to="/login" class="btn btn-outline-primary me-2">Login</router-link>
      <router-link to="/register" class="btn btn-outline-secondary">Register</router-link>
    </div>

    <!-- Challenge List Section -->
    <h2 class="mt-5">Challenges</h2>
    <div class="row">
      <div class="col-md-4" v-for="challenge in challenges" :key="challenge.id">
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="card-title">{{ challenge.name }}</h5>
            <p class="card-text">{{ challenge.description }}</p>
            <router-link :to="`/challenges/${challenge.id}`" class="btn btn-primary">
              View Details
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getChallenges } from '@/api/challenges';

export default {
  name: 'Home',
  data() {
    return {
      challenges: [],
    };
  },
  async mounted() {
    try {
      const response = await getChallenges();
      this.challenges = response.data;
    } catch (error) {
      console.error('Failed to fetch challenges:', error);
    }
  },
};
</script>

<style scoped>
/* Styles specific to HomeView.vue */

/* Styling for Login and Register buttons */
.btn-outline-primary,
.btn-outline-secondary {
  font-weight: bold;
}
</style>
