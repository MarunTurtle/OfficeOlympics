/**
 * @파일명: ChallengeScore.vue
 * @설명: 챌린지 점수 입력 및 제출 페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/challenges/{challengeId}/scores (플레이어 목록 조회)
 *   - POST /api/challenges/{challengeId}/scores (점수 제출)
 */

<template>
  <MainLayout>
    <div class="score-submission container py-4">
      <h1 class="text-center mb-5">챌린지 점수 제출</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!-- 로딩 상태 표시 -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!-- 에러 메시지 표시 -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>

          <!-- 점수 입력 폼 -->
          <div v-else class="score-form">
            <!-- 각 플레이어별 점수 입력 필드 -->
            <div v-for="(player, index) in players" :key="index" class="mb-4">
              <label :for="'player' + index" class="form-label">
                {{ player.player_name }} (총점: {{ player.total_score }})
              </label>
              <input type="number" class="form-control" v-model="scores[index]"
                :placeholder="player.player_name + ' 점수 입력'" min="0" step="1">
            </div>

            <!-- 제출/취소 버튼 -->
            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary" @click="submitScores" :disabled="!isValidSubmission">
                제출
              </button>
              <button class="btn btn-secondary" @click="cancel">
                취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
/**
 * @컴포넌트명: ChallengeScore
 * @설명: 챌린지 참가자들의 점수를 입력하고 제출하는 컴포넌트
 * @데이터구조: {
 *   players: Array<{
 *     player_name: string,
 *     total_score: number
 *   }>,
 *   scores: Array<number>
 * }
 */

import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';

// 라우터와 스토어 설정
const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();

// 상태 관리
const scores = ref([]);
const players = ref([]);
const loading = ref(true);
const error = ref(null);

/**
 * 점수 입력 유효성 검사
 * 모든 플레이어의 점수가 입력되었고 0 이상인지 확인
 */
const isValidSubmission = computed(() => {
  return scores.value.every(score => score !== '' && score >= 0);
});

/**
 * 컴포넌트 마운트 시 플레이어 목록 로드
 * 백엔드에서 현재 챌린지의 플레이어 정보를 가져옴
 */
onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await challengeStore.fetchChallengeScoreForm(route.params.id);
    players.value = response.playerNames;
    scores.value = new Array(players.value.length).fill('');

  } catch (err) {
    console.error('Failed to initialize:', err);
    error.value = '점수 입력 폼을 불러오는데 실패했습니다. 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
});

/**
 * 점수 제출 처리
 * 입력된 점수를 백엔드로 전송하고 순위 페이지로 이동
 */
const submitScores = async () => {
  if (!isValidSubmission.value) return;

  const scoreData = {
    playerNames: players.value.map(player => player.player_name),
    scores: scores.value.map(Number)
  };

  try {
    await challengeStore.submitScore(route.params.id, scoreData);
    router.push(`/challenges/${route.params.id}/rank`);
  } catch (error) {
    console.error('Failed to submit scores:', error);
    error.value = '점수 제출에 실패했습니다. 다시 시도해주세요.';
  }
};

/**
 * 취소 처리
 * 이전 페이지로 돌아감
 */
const cancel = () => {
  router.back();
};
</script>

<style scoped>
/*
  점수 입력 폼 스타일링:
  - 흰색 배경
  - 부드러운 그림자 효과
  - 둥근 모서리
*/
.score-form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
