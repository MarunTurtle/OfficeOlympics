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

      <!-- 메인 콘텐츠 -->
      <div v-else class="row">
        <!-- 왼쪽: 챌린지 상세 정보 -->
        <div class="col-lg-6">
          <div class="challenge-detail-card">
            <h1 class="challenge-title">{{ challenge?.challengeName }}</h1>
            <p class="challenge-desc">{{ challenge?.challengeDesc }}</p>

            <!-- 비디오가 있다면 표시 -->
            <div v-if="challenge?.challengeUrl" class="video-container mt-4">
              <div class="video-wrapper">
                <iframe
                  :src="transformYoutubeUrl(challenge.challengeUrl)"
                  frameborder="0"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowfullscreen>
                </iframe>
              </div>
            </div>
          </div>
        </div>

        <!-- 오른쪽: 점수 제출 폼 -->
        <div class="col-lg-6">
          <div class="score-form">
            <h2 class="text-center mb-4">점수 제출</h2>
            <div class="score-submission-form">
              <div v-for="(player, index) in players" :key="index" class="mb-4">
                <label :for="'player' + index" class="form-label player-name">
                  {{ player.player_name }}
                </label>
                <input
                  type="number"
                  class="form-control input-field"
                  v-model="scores[index]"
                  :placeholder="player.player_name + ' 점수 입력'"
                  min="0"
                  step="1"
                >
              </div>

              <div class="d-grid gap-2 mt-4">
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

// 챌린지 정보 상태 추가
const challenge = ref(null);

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

    // 챌린지 정보와 플레이어 정보를 병렬로 가져오기
    const [challengeDetails, scoreFormResponse] = await Promise.all([
      challengeStore.fetchChallengeDetails(route.params.id),
      challengeStore.fetchChallengeScoreForm(route.params.id)
    ]);

    challenge.value = challengeDetails;
    players.value = scoreFormResponse.playerNames;
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

// YouTube URL 변환 함수 추가
const transformYoutubeUrl = (url) => {
  if (!url) return '';
  if (url.includes('youtu.be/')) {
    const videoId = url.split('youtu.be/')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  } else if (url.includes('youtube.com/watch?v=')) {
    const videoId = url.split('v=')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  }
  return url;
};
</script>

<style scoped>
/* 기존 스타일 유지 */
.score-form {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
}

/* 챌린지 상세 카드 스타일 추가 */
.challenge-detail-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  height: 100%;
}

.challenge-title {
  color: var(--primary-color);
  font-weight: 600;
  margin-bottom: 1rem;
}

.challenge-desc {
  color: #666;
  font-size: 1.1rem;
  line-height: 1.6;
}

/* 비디오 컨테이너 스타일 */
.video-container {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 비율 */
  height: 0;
}

.video-wrapper iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

/* 반응형 스타일 */
@media (max-width: 992px) {
  .challenge-detail-card {
    margin-bottom: 1.5rem;
  }
}

/* Updated styles to match Login/Register */
.input-field {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.input-field:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

.btn-secondary {
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  transform: scale(1.02);
}

h1 {
  color: var(--primary-color);
  font-weight: 600;
}

/* 챌린지 정보 스타일 추가 */
.challenge-info {
  border-bottom: 2px solid var(--tertiary-color);
  padding-bottom: 1.5rem;
}

.challenge-desc {
  color: #666;
  font-size: 1.1rem;
  max-width: 800px;
  margin: 0 auto;
}

/* 플레이어 이름 스타일 수정 */
.player-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: 0.5rem;
}

/* 입력 필드 간격 조정 */
.score-submission-form > div {
  margin-bottom: 2rem;
}
</style>
