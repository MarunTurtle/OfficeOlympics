/**
 * @파일명: ChallengeRank.vue
 * @설명: 챌린지 결과 및 순위를 표시하는 페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/challenges/{challengeId}/rank (챌린지 순위 정보)
 */

<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">Current Leaderboard</h1>

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
            <div class="rankings-list">
              <!-- Top 3 Rankings -->
              <div v-for="player in topThree" :key="player.rank"
                   :class="['ranking-card', `rank-${player.rank}-card`]">
                <div class="d-flex justify-content-between align-items-center p-4">
                  <div class="d-flex align-items-center gap-4">
                    <div class="rank-number">{{ player.rank }}위</div>
                    <div class="player-info">
                      <div class="player-name">{{ player.playerName }}</div>
                    </div>
                  </div>
                  <div class="score">{{ player.score }} 점</div>
                </div>
              </div>

              <!-- Regular Rankings -->
              <div v-for="player in remainingPlayers" :key="player.rank"
                   class="regular-ranking-item">
                <div class="d-flex justify-content-between align-items-center p-3">
                  <div class="d-flex align-items-center gap-4">
                    <div class="regular-rank">{{ player.rank }}위</div>
                    <div class="regular-player-name">{{ player.playerName }}</div>
                  </div>
                  <div class="regular-score">{{ player.score }} 점</div>
                </div>
              </div>

              <!-- 네비게이션 버튼 - rankings-list 안으로 이동 -->
              <div class="d-flex justify-content-between gap-3 mt-5">
                <button
                  type="button"
                  class="btn btn-primary flex-grow-1"
                  @click="nextChallenge"
                >
                  다음 챌린지
                </button>
                <button
                  type="button"
                  class="btn btn-alert flex-grow-1"
                  @click="endOlympics"
                >
                  올림픽 종료
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
/**
 * @컴포넌트명: ChallengeRank
 * @설명: 챌린지 완료 후 참가자들의 순위를 표시하는 컴포넌트
 * @데이터구조: {
 *   rankings: Array<{
 *     rank: number,
 *     playerName: string,
 *     score: number
 *   }>
 * }
 */

import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';

// 라우터와 스토어 설정
const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();

// 상태 관리
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

// 컴퓨티드 프로퍼티 추가
const topThree = computed(() => rankings.value.slice(0, 3));
const remainingPlayers = computed(() => rankings.value.slice(3));
</script>

<style scoped>
.rankings-list {
  max-width: 800px;
  margin: 0 auto;
}

.ranking-card {
  border-radius: 12px;
  margin-bottom: 1rem;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.ranking-card:hover {
  transform: translateY(-3px);
}

/* 금메달 색상 */
.rank-1-card {
  background-color: var(--tertiary-color);
}

.rank-1-card .rank-number {
  color: #FFD700;
}

/* 은메달 색상 */
.rank-2-card {
    background-color: var(--tertiary-color);
}

.rank-2-card .rank-number {
  color: #C0C0C0;
}

/* 동메달 색상 */
.rank-3-card {
  background-color: var(--tertiary-color);
}

.rank-3-card .rank-number {
  color: #CD7F32;
}

.rank-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: rgba(0, 0, 0, 0.7);
}

.player-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.8);
}

.score {
  font-size: 1.3rem;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.7);
}

.regular-ranking-item {
  background-color: white;
  border-radius: 12px;
  margin-bottom: 0.75rem;
  transition: all 0.2s ease;
  border: 1px solid #eee;
}

.regular-ranking-item:hover {
  transform: translateY(-3px);
  background-color: #f8f9fa;
}

.regular-rank {
  font-size: 1.2rem;
  font-weight: 600;
  color: #666;
}

.regular-player-name {
  font-size: 1.2rem;
  font-weight: 500;
  color: #333;
}

.regular-score {
  font-size: 1.2rem;
  font-weight: 600;
  color: #666;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .rank-number, .player-name, .score {
    font-size: 1.1rem;
  }

  .regular-rank, .regular-player-name, .regular-score {
    font-size: 1rem;
  }
}

/**
 * 버튼 공통 스타일
 * - 일관된 크기와 패딩
 * - 부드러운 전환 효과
 */
.btn {
  padding: 1rem 0.75rem;
  font-weight: 600;
  font-size: 1.1rem;
  transition: all 0.3s ease;
  border: none;
  border-radius: 8px;
}

/**
 * Primary 버튼 스타일
 * - 다음 챌린지 버튼
 */
.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

/**
 * Alert 버튼 스타일
 * - 올림픽 종료 버튼
 */
.btn-alert {
  background-color: var(--alert-color);
  color: white;
}

.btn-alert:hover {
  background-color: #e84c76; /* alert color의 더 진한 버전 */
  transform: scale(1.02);
}
</style>
