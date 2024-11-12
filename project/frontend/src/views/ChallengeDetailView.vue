<!-- src/views/ChallengeDetail.vue -->
<template>
  <div class="container">
    <h2 class="mt-4">{{ challenge.name }}</h2>
    <p>{{ challenge.description }}</p>
    <!-- Record submission form -->
    <form @submit.prevent="submitRecord" class="mt-4">
      <div class="mb-3">
        <label class="form-label">Your Record</label>
        <input v-model="record" type="text" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary">Submit Record</button>
    </form>
    <!-- Leaderboard -->
    <h3 class="mt-5">Leaderboard</h3>
    <ul class="list-group mt-3">
      <li class="list-group-item" v-for="entry in leaderboard" :key="entry.userId">
        {{ entry.username }}: {{ entry.record }}
      </li>
    </ul>
  </div>
</template>

<script>
import {
  getChallengeDetail,
  submitChallengeRecord,
  getLeaderboard,
} from '@/api/challenges';

export default {
  name: 'ChallengeDetail',
  props: ['id'],
  data() {
    return {
      challenge: {},
      record: '',
      leaderboard: [],
    };
  },
  async created() {
    try {
      const [challengeRes, leaderboardRes] = await Promise.all([
        getChallengeDetail(this.id),
        getLeaderboard(this.id),
      ]);
      this.challenge = challengeRes.data;
      this.leaderboard = leaderboardRes.data;
    } catch (error) {
      console.error(error);
      // Error handling
    }
  },
  methods: {
    async submitRecord() {
      try {
        await submitChallengeRecord(this.id, this.record);
        // Refresh leaderboard
        const leaderboardRes = await getLeaderboard(this.id);
        this.leaderboard = leaderboardRes.data;
      } catch (error) {
        console.error(error);
        // Error handling
      }
    },
  },
};
</script>

<style scoped>
/* Styles specific to ChallengeDetail.vue */
</style>
