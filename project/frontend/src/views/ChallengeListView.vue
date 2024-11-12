<!-- src/views/ChallengeList.vue -->
<template>
  <div class="container">
    <h2 class="mt-4">Challenge List</h2>
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
  name: 'ChallengeList',
  data() {
    return {
      challenges: [],
    };
  },
  async created() {
    try {
      const response = await getChallenges();
      this.challenges = response.data;
    } catch (error) {
      console.error(error);
      // Error handling
    }
  },
};
</script>

<style scoped>
/* Styles specific to ChallengeList.vue */
</style>
