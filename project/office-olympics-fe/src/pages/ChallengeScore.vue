<template>
  <MainLayout>
    <div class="score-submission container py-4">
      <h1 class="text-center mb-5">Challenge Score Submission</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <h2 class="text-center mb-4">Score Submission</h2>

          <div class="score-form">
            <div v-for="(player, index) in players" :key="index" class="mb-4">
              <label :for="'player' + index" class="form-label">
                {{ player.nickname }}
              </label>
              <input type="number" class="form-control" v-model="scores[index]"
                :placeholder="'Enter score for ' + player.nickname">
            </div>

            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary" @click="submitScores">
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
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useOlympicStore } from '@/stores/olympic';
import { useChallengeStore } from '@/stores/challenge';

const route = useRoute();
const router = useRouter();
const olympicStore = useOlympicStore();
const challengeStore = useChallengeStore();

const players = ref([]);
const scores = ref([]);

onMounted(async () => {
  // Get players from the current Olympic
  players.value = olympicStore.players;
  scores.value = new Array(players.value.length).fill('');
});

const submitScores = async () => {
  const scoreData = {
    playerNames: players.value.map(p => p.nickname),
    scores: scores.value.map(Number)
  };

  try {
    await challengeStore.submitScore(route.params.id, scoreData);
    router.push('/challenges/' + route.params.id);
  } catch (error) {
    console.error('Failed to submit scores:', error);
  }
};

const cancel = () => {
  router.back();
};
</script>