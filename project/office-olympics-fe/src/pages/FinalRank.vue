<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">올림픽 최종 결과</h1>

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

          <!-- Results Display -->
          <div v-else class="results-display">
            <!-- Podium Display -->
            <div class="podium-container d-flex justify-content-around align-items-end mb-5">
              <!-- 2nd Place -->
              <div v-if="rankings[1]" class="podium-item second">
                <div class="rank-card bg-success text-white p-4 rounded">
                  <div class="rank-position">2위</div>
                  <div class="player-name">{{ rankings[1].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[1].score) }}</div>
                </div>
              </div>

              <!-- 1st Place -->
              <div v-if="rankings[0]" class="podium-item first">
                <div class="rank-card bg-primary text-white p-4 rounded">
                  <div class="rank-position">1위</div>
                  <div class="player-name">{{ rankings[0].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[0].score) }}</div>
                </div>
              </div>

              <!-- 3rd Place -->
              <div v-if="rankings[2]" class="podium-item third">
                <div class="rank-card bg-warning p-4 rounded">
                  <div class="rank-position">3위</div>
                  <div class="player-name">{{ rankings[2].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[2].score) }}</div>
                </div>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary" @click="confirmNewOlympics">
                새로운 올림픽 열기
              </button>
              <button class="btn btn-secondary" @click="goToMain">
                홈페이지로 돌아가기
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
import { useOlympicStore } from '@/stores/olympic';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();
const olympicStore = useOlympicStore();

const rankings = ref([]);
const loading = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await challengeStore.fetchFinalRank(route.params.id);
    rankings.value = response;
  } catch (err) {
    console.error('Failed to fetch final rankings:', err);
    error.value = 'Failed to load final rankings. Please try again.';
  } finally {
    loading.value = false;
  }
});

const formatScore = (score) => new Intl.NumberFormat().format(score);

const confirmNewOlympics = async () => {
  if (confirm('현재 올림픽 데이터가 삭제됩니다. 계속하시겠습니까?')) {
    try {
      await olympicStore.deleteOlympicEvent(olympicStore.userOlympicId);
      olympicStore.clearOlympicData();
      alert('올림픽이 성공적으로 삭제되었습니다!');
      router.push('/olympic/create');
    } catch (err) {
      console.error('Failed to delete Olympics:', err);
      error.value = 'Failed to delete current Olympics.';
      alert('올림픽 삭제에 실패했습니다. 다시 시도해주세요.');
    }
  }
};

const goToMain = () => {
  router.push('/');
};
</script>

<style scoped>
.podium-container {
  min-height: 400px;
}

.podium-item {
  width: 30%;
}

.rank-card {
  text-align: center;
}

.first { transform: translateY(-50px); }
.second { transform: translateY(-25px); }
.third { transform: translateY(0); }

.rank-position {
  font-size: 1.5rem;
  font-weight: bold;
}

.player-name {
  font-size: 1.2rem;
  margin: 10px 0;
}

.score {
  font-size: 1rem;
}
</style>
