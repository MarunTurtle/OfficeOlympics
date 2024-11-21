<template>
  <MainLayout>
    <div class="score-submission container py-4">
      <h1 class="text-center mb-5">Challenge Score Submission</h1>

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

          <div v-else class="score-form">
            <div v-for="(player, index) in players" :key="index" class="mb-4">
              <label :for="'player' + index" class="form-label">
                {{ player.player_name }} (Total Score: {{ player.total_score }})
              </label>
              <input type="number" class="form-control" v-model="scores[index]"
                :placeholder="'Enter score for ' + player.player_name" min="0" step="1">
            </div>

            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary" @click="submitScores" :disabled="!isValidSubmission">
                Submit
              </button>
              <button class="btn btn-secondary" @click="cancel">
                Cancel
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

const scores = ref([]);
const players = ref([]);
const loading = ref(true);
const error = ref(null);

const isValidSubmission = computed(() => {
  return scores.value.every(score => score !== '' && score >= 0);
});

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await challengeStore.fetchChallengeScoreForm(route.params.id);
    players.value = response.playerNames.map(player => ({
      player_name: player.player_name,
      player_id: player.player_id
    }));
    scores.value = new Array(players.value.length).fill('');

  } catch (err) {
    console.error('Failed to initialize:', err);
    error.value = 'Failed to initialize score submission. Please try again.';
  } finally {
    loading.value = false;
  }
});

const submitScores = async () => {
  if (!isValidSubmission.value) return;

  const scoreData = {
    playerNames: players.value.map(player => player.player_name),
    playerId: players.value.map(player => player.player_id),
    scores: scores.value.map(Number)
  };

  try {
    await challengeStore.submitScore(route.params.id, scoreData);
    router.push(`/challenges/${route.params.id}/rank`);
  } catch (error) {
    console.error('Failed to submit scores:', error);
    error.value = 'Failed to submit scores. Please try again.';
  }
};

const cancel = () => {
  router.back();
};
</script>

<style scoped>
.score-form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
