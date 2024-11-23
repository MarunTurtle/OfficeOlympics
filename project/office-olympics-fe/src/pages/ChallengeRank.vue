<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">챌린지 결과</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!-- Loading State -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!-- Error State -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>

          <!-- Rankings Display -->
          <div v-else>
            <div class="rankings-list">
              <!-- Display each ranking -->
              <div v-for="player in rankings" :key="player.rank"
                   class="ranking-item p-3 mb-2 border rounded">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <span class="me-3">#{{ player.rank }}</span>
                    <span>{{ player.playerName }}</span>
                  </div>
                  <div>
                    <strong>{{ player.score }}</strong> 점
                  </div>
                </div>
              </div>
            </div>

            <!-- Navigation buttons -->
            <div class="d-flex justify-content-center gap-3 mt-4">
              <button class="btn btn-primary" @click="nextChallenge">
                다음 챌린지
              </button>
              <button class="btn btn-secondary" @click="endOlympics">
                올림픽 종료
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
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();

const rankings = ref([]);
const loading = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await challengeStore.fetchChallengeRank(route.params.id);
    rankings.value = response;
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
