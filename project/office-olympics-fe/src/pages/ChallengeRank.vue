<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5 text-primary">Current Challenge Rankings</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>

          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>

          <div v-else>
            <!-- Top 3 Players -->
            <div class="top-players mb-5">
              <div v-for="(player, index) in topThreePlayers" :key="index"
                   class="rank-card mb-3"
                   :class="getRankClass(index)">
                <div class="rank-content">
                  <div class="player-info">
                    <span class="rank-position">{{ index + 1 }}</span>
                    <span class="player-name">{{ player.playerName || 'Unknown Player' }}</span>
                  </div>
                  <div class="score">
                    Score: {{ formatScore(player.score || 0) }}
                  </div>
                </div>
              </div>
            </div>

            <!-- Other Players -->
            <div class="other-players">
              <div v-for="(player, index) in otherPlayers" :key="index + 3"
                   class="player-row">
                <div class="player-content">
                  <div class="player-info">
                    <span class="rank-number">{{ index + 4 }}</span>
                    <span class="player-name">{{ player.playerName || 'Unknown Player' }}</span>
                  </div>
                  <div class="score">
                    Score: {{ formatScore(player.score || 0) }}
                  </div>
                </div>
              </div>
            </div>

            <!-- Navigation Buttons -->
            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary" @click="nextChallenge">
                Next Challenge
              </button>
              <button class="btn btn-secondary" @click="endOlympics">
                End Olympics
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();

const rankings = ref([]);
const loading = ref(true);
const error = ref(null);

const topThreePlayers = computed(() => rankings.value.slice(0, 3));
const otherPlayers = computed(() => rankings.value.slice(3));

const formatScore = (score) => {
  if (!score && score !== 0) return 'N/A';
  return new Intl.NumberFormat().format(score);
};

const getRankClass = (index) => {
  switch (index) {
    case 0:
      return 'rank-card-gold';
    case 1:
      return 'rank-card-silver';
    case 2:
      return 'rank-card-bronze';
    default:
      return '';
  }
};

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await challengeStore.fetchChallengeRank(route.params.id);
    console.log('Rankings response:', response); // Debug log
    rankings.value = response || [];
  } catch (err) {
    console.error('Failed to fetch rankings:', err);
    error.value = 'Failed to load rankings. Please try again.';
  } finally {
    loading.value = false;
  }
});

const nextChallenge = () => {
  router.push('/');
};

const endOlympics = () => {
  router.push(`/challenges/${route.params.id}/final-rank`);
};
</script>

<style scoped>
.rank-card {
  padding: 1.5rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  color: white;
  font-size: 1.2rem;
  font-weight: 600;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.rank-card-gold {
  background-color: #f7c873;
}

.rank-card-silver {
  background-color: #c0c0c0;
}

.rank-card-bronze {
  background-color: #cd7f32;
}

.rank-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.player-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.rank-position, .rank-number {
  font-weight: bold;
  min-width: 2rem;
}

.player-name {
  font-size: 1.1rem;
}

.player-row {
  background: white;
  padding: 1.2rem;
  border-radius: 8px;
  margin-bottom: 0.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.player-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.score {
  color: var(--primary-color);
  font-weight: 600;
  min-width: 120px;
  text-align: right;
}

button {
  font-weight: 600;
  padding: 0.75rem 2rem;
}

button:hover {
  transform: scale(1.05);
  transition: transform 0.2s ease;
}
</style>
