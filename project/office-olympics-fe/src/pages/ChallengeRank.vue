/**
 * @파일명: ChallengeRank.vue
 * @설명: 챌린지 결과 및 순위를 표시하는 페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/challenges/{challengeId}/rank (챌린지 순위 정보)
 */

<template>
  <MainLayout>
    <div class="container py-4">
      <h1 class="text-center mb-5">챌린지 결과</h1>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <!--
            로딩 상태 표시
            데이터 로딩 중일 때 스피너 표시
          -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>

          <!--
            에러 상태 표시
            데이터 로딩 실패 시 에러 메시지 표시
          -->
          <div v-else-if="error" class="alert alert-danger text-center">
            {{ error }}
          </div>

          <!-- 순위 표시 섹션 -->
          <div v-else>
            <div class="rankings-list">
              <!--
                각 플레이어의 순위 아이템
                순위, 이름, 점수를 한 줄에 표시
              -->
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

            <!--
              네비게이션 버튼
              - 다음 챌린지로 이동
              - 올림픽 종료 및 최종 순위 확인
            -->
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

import { ref, onMounted } from 'vue';
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
</script>
