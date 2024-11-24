/**
 * @파일명: FinalRank.vue
 * @설명: 올림픽 최종 순위를 표시하는 페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/olympics/{olympicId}/final-rank (최종 순위 조회)
 *   - DELETE /api/olympics/{olympicId} (올림픽 삭제)
 */

<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">올림픽 최종 결과</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!-- 로딩 상태 표시 -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!-- 에러 상태 표시 -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>

          <!-- 결과 표시 섹션 -->
          <div v-else class="results-display">
            <!-- 시상대 표시 -->
            <div class="podium-container d-flex justify-content-around align-items-end mb-5">
              <!-- 2등 표시 -->
              <div v-if="rankings[1]" class="podium-item second">
                <div class="rank-card bg-success text-white p-4 rounded">
                  <div class="rank-position">2위</div>
                  <div class="player-name">{{ rankings[1].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[1].score) }}</div>
                </div>
              </div>

              <!-- 1등 표시 -->
              <div v-if="rankings[0]" class="podium-item first">
                <div class="rank-card bg-primary text-white p-4 rounded">
                  <div class="rank-position">1위</div>
                  <div class="player-name">{{ rankings[0].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[0].score) }}</div>
                </div>
              </div>

              <!-- 3등 표시 -->
              <div v-if="rankings[2]" class="podium-item third">
                <div class="rank-card bg-warning p-4 rounded">
                  <div class="rank-position">3위</div>
                  <div class="player-name">{{ rankings[2].playerName }}</div>
                  <div class="score">총점: {{ formatScore(rankings[2].score) }}</div>
                </div>
              </div>
            </div>

            <!-- 액션 버튼 -->
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
/**
 * @컴포넌트명: FinalRank
 * @설명: 올림픽 최종 순위를 시상대 형태로 표시하는 컴포넌트
 * @데이터구조: {
 *   rankings: Array<{
 *     playerName: string,
 *     score: number,
 *     rank: number
 *   }>
 * }
 */

import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import { useOlympicStore } from '@/stores/olympic';
import MainLayout from '@/layouts/MainLayout.vue';

// 상태 관리 설정
const route = useRoute();
const router = useRouter();
const challengeStore = useChallengeStore();
const olympicStore = useOlympicStore();

// 로컬 상태 관리
const rankings = ref([]);
const loading = ref(true);
const error = ref(null);

/**
 * 컴포넌트 마운트 시 최종 순위 데이터 로드
 */
onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await challengeStore.fetchFinalRank(route.params.id);
    rankings.value = response;
  } catch (err) {
    console.error('Failed to fetch final rankings:', err);
    error.value = '최종 순위를 불러오는데 실패했습니다. 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
});

/**
 * 점수 포맷팅 함수
 * 천 단위 구분자 추가
 */
const formatScore = (score) => new Intl.NumberFormat().format(score);

/**
 * 새 올림픽 시작 확인 및 처리
 * 현재 올림픽 데이터 삭제 후 새 올림픽 생성 페이지로 이동
 */
const confirmNewOlympics = async () => {
  if (confirm('현재 올림픽 데이터가 삭제됩니다. 계속하시겠습니까?')) {
    try {
      await olympicStore.deleteOlympicEvent(olympicStore.userOlympicId);
      olympicStore.clearOlympicData();
      alert('올림픽이 성공적으로 삭제되었습니다!');
      router.push('/olympic/create');
    } catch (err) {
      console.error('Failed to delete Olympics:', err);
      error.value = '올림픽 삭제에 실패했습니다.';
      alert('올림픽 삭제에 실패했습니다. 다시 시도해주세요.');
    }
  }
};

/**
 * 메인 페이지로 이동
 */
const goToMain = () => {
  router.push('/');
};
</script>

<style scoped>
/*
  시상대 컨테이너 스타일링:
  - 최소 높이 설정으로 시각적 안정감
  - 순위별 높이 차이 표현
*/
.podium-container {
  min-height: 400px;
}

/* 각 순위 카드 스타일링 */
.podium-item {
  width: 30%;
}

.rank-card {
  text-align: center;
}

/* 순위별 높이 차이 설정 */
.first { transform: translateY(-50px); }
.second { transform: translateY(-25px); }
.third { transform: translateY(0); }

/* 순위 텍스트 스타일링 */
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
