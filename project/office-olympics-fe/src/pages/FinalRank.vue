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
      <h1 class="text-center mt-5 mb-5">올림픽 최종 결과</h1>

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
                  <div class="score">{{ formatScore(rankings[1].score) }} 점</div>
                </div>
              </div>

              <!-- 1등 표시 -->
              <div v-if="rankings[0]" class="podium-item first">
                <div class="rank-card bg-primary text-white p-4 rounded">
                  <div class="rank-position">1위</div>
                  <div class="player-name">{{ rankings[0].playerName }}</div>
                  <div class="score">{{ formatScore(rankings[0].score) }} 점</div>
                </div>
              </div>

              <!-- 3등 표시 -->
              <div v-if="rankings[2]" class="podium-item third">
                <div class="rank-card bg-warning p-4 rounded">
                  <div class="rank-position">3위</div>
                  <div class="player-name">{{ rankings[2].playerName }}</div>
                  <div class="score">{{ formatScore(rankings[2].score) }} 점</div>
                </div>
              </div>
            </div>

            <!-- 액션 버튼 -->
            <div class="d-flex justify-content-center gap-3 mt-5">
              <button class="btn btn-primary w-100" @click="confirmNewOlympics">
                새로운 올림픽 열기
              </button>
              <button class="btn btn-secondary w-100" @click="goToMain">
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
/* 시상대 컨테이너 기본 스타일 */
.podium-container {
  min-height: 25rem;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  gap: 1rem;
  padding: 1rem;
  flex-wrap: nowrap; /* 줄바꿈 방지 */
}

/* 각 순위 카드 스타일링 */
.podium-item {
  flex: 1;
  min-width: 8rem; /* 최소 너비 설정 */
  max-width: 15rem; /* 최대 너비 설정 */
}

.rank-card {
  text-align: left;
  display: flex;
  flex-direction: column;
  justify-content: top;
  gap: 0.6rem;
  padding: 1.5rem !important;
}

/* 순위별 높이와 위치 차이 설정 */
.first .rank-card {
  height: 25.25rem;
}

.second .rank-card {
  height: 19.25rem;
}

.third .rank-card {
  height: 16.25rem;
}

/* 순위 텍스트 반응형 조정 */
.rank-position {
  font-size: clamp(1rem, 1.5vw, 1.5rem);
  font-weight: bold;
  color: white;
}

.player-name {
  font-size: clamp(1rem, 1.5vw, 2.5rem);
  font-weight: bold;
  margin: 0.5rem 0;
  color: white;
  word-break: break-all;
}

.score {
  font-size: clamp(0.875rem, 1.2vw, 1.2rem);
  color: black;
  font-weight: 600;
}

/* 버튼 컨테이너 스타일 */
.d-flex.justify-content-center.gap-3.mt-5 {
  flex-wrap: wrap;
}

/* 버튼 공통 스타일 */
.btn {
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  min-width: 200px;
  flex: 1;
}

/* Primary 버튼 스타일 */
.btn-primary {
  background-color: var(--primary-color);
  border: none;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

/* Secondary 버튼 스타일 */
.btn-secondary {
  border: none;
}

.btn-secondary:hover {
  transform: scale(1.02);
}

/* 태블릿 화면 대응 */
@media (max-width: 992px) {
  .podium-container {
    padding: 0.5rem;
  }

  .rank-card {
    padding: 1rem !important;
  }
}

/* 모바일 화면 대응 */
@media (max-width: 768px) {
  .podium-container {
    gap: 0.5rem;
  }

  .first .rank-card {
    height: 21rem;
  }

  .second .rank-card {
    height: 17rem;
  }

  .third .rank-card {
    height: 14rem;
  }
}

/* 작은 모바일 화면 대응 */
@media (max-width: 480px) {
  .podium-item {
    min-width: 6rem;
  }

  .rank-card {
    padding: 0.75rem !important;
  }
}
</style>
