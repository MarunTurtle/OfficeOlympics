<template>
  <MainLayout>
    <div class="leaderboard">
      <!-- Leaderboard Header -->
      <div class="leaderboard-header text-center">
        <h1>Leaderboard</h1>
        <p class="text-muted">Check out the top performers of this Olympic event!</p>
      </div>

      <!-- Top 3 Players -->
      <div class="top-players row text-center">
        <div v-for="(player, index) in topPlayers" :key="player.id" class="col-md-4 mb-4">
          <div class="card h-100"
            :class="{ 'bg-gold': index === 0, 'bg-silver': index === 1, 'bg-bronze': index === 2 }">
            <div class="card-body">
              <h3 class="player-rank">#{{ index + 1 }}</h3>
              <h4 class="player-name">{{ formatName(player.name) }}</h4>
              <p class="player-score">{{ formatScore(player.score) }} points</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Full Leaderboard -->
      <div class="full-leaderboard mt-5">
        <h2>All Participants</h2>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Score</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(player, index) in sortedPlayers" :key="player.id">
              <td>{{ index + 1 }}</td>
              <td>{{ formatName(player.name) }}</td>
              <td>{{ formatScore(player.score) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Navigation Buttons -->
      <div class="navigation mt-4 text-center">
        <RouterLink to="/main" class="btn btn-primary">Back to Home</RouterLink>
        <RouterLink to="/olympic/create" class="btn btn-secondary ms-3">Create New Olympic</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';
import { capitalize, formatNumber } from '@/utils/formatter'; // Import formatter utilities

const route = useRoute();
const challengeStore = useChallengeStore();
const players = ref([]);
const loading = ref(true);

onMounted(async () => {
  const olympicId = route.params.olympicId;
  try {
    await challengeStore.fetchLeaderboard(olympicId);
    players.value = challengeStore.leaderboard;
  } catch (error) {
    console.error('Failed to fetch leaderboard:', error);
  } finally {
    loading.value = false;
  }
});

const topPlayers = computed(() => players.value.slice(0, 3));
const otherPlayers = computed(() => players.value.slice(3));

const sortedPlayers = computed(() =>
  [...players.value].sort((a, b) => b.score - a.score)
);

// Utility Methods
const formatName = (name) => (name ? capitalize(name) : 'Unknown Player');
const formatScore = (score) => new Intl.NumberFormat().format(score);
</script>

<style scoped>
.leaderboard-header h1 {
  font-size: 2.5rem;
  color: var(--primary-color);
}

.top-players .card {
  padding: 20px;
  border-radius: 10px;
  color: white;
}

.bg-gold {
  background-color: #f7c873;
  /* Gold color */
}

.bg-silver {
  background-color: #c0c0c0;
  /* Silver color */
}

.bg-bronze {
  background-color: #cd7f32;
  /* Bronze color */
}

.player-rank {
  font-size: 1.5rem;
}

.player-name {
  font-size: 1.2rem;
  font-weight: bold;
}

.player-score {
  font-size: 1rem;
}

.full-leaderboard h2 {
  margin-top: 20px;
  font-size: 2rem;
  color: var(--primary-color);
}

.table {
  margin-top: 20px;
}

.navigation {
  margin-top: 40px;
}
</style>
