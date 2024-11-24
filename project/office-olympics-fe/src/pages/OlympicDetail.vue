/**
 * @파일명: OlympicDetail.vue
 * @설명: 올림픽 상세 정보 표시 컴포넌트
 * @관련백엔드:
 *   - GET /api/olympics/{olympicId} (올림픽 상세 정보 조회)
 *   - GET /api/olympics/{olympicId}/players (올림픽 참가자 목록 조회)
 */

<template>
  <MainLayout>
    <!-- 올림픽 상세 정보 컨테이너 -->
    <div class="olympic-detail">
      <!-- 올림픽 기본 정보 -->
      <h1 class="text-center mb-4">{{ olympic?.name }}</h1>
      <p class="text-center text-muted">올림픽 ID: {{ olympic?.id }}</p>

      <!-- 참가자 목록 섹션 -->
      <div class="player-list mt-5">
        <h2>플레이어</h2>
        <ul class="list-group">
          <!-- 각 참가자 정보 -->
          <li
            v-for="(player, index) in players"
            :key="player.id"
            class="list-group-item"
          >
            <strong>플레이어 {{ index + 1 }}:</strong> {{ player.nickname }}
          </li>
        </ul>
      </div>

      <!-- 네비게이션 버튼 -->
      <div class="mt-5 text-center">
        <RouterLink to="/main" class="btn btn-secondary">홈으로 돌아가기</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
/**
 * @컴포넌트명: OlympicDetail
 * @설명: 올림픽 상세 정보와 참가자 목록을 표시
 * @상태:
 *   - olympic: 올림픽 상세 정보
 *   - players: 참가자 목록
 */

import { ref, onMounted } from 'vue';
import MainLayout from '@/layouts/MainLayout.vue';
import { useOlympicStore } from '@/stores/olympic';

// 올림픽 스토어 초기화
const olympicStore = useOlympicStore();

/**
 * 상태 관리
 * - olympic: 올림픽 정보 객체
 * - players: 참가자 배열
 */
const olympic = ref(null);
const players = ref([]);

/**
 * 컴포넌트 마운트 시 데이터 로드
 * - 올림픽 상세 정보 조회
 * - 참가자 목록 조회
 * @TODO: olympicId를 라우트 파라미터에서 동적으로 가져오도록 수정
 */
const olympicId = 1; // 라우트 파라미터로 변경 필요
onMounted(async () => {
  try {
    await olympicStore.fetchOlympicDetails(olympicId);
    await olympicStore.fetchOlympicPlayers(olympicId);
    olympic.value = olympicStore.olympic;
    players.value = olympicStore.players;
  } catch (error) {
    console.error('Error fetching Olympic details:', error);
  }
});
</script>

<style scoped>
/**
 * 올림픽 상세 페이지 스타일링
 * - 최대 너비 제한
 * - 중앙 정렬
 * - 그림자 효과와 둥근 모서리
 */
.olympic-detail {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/**
 * 참가자 목록 섹션 스타일링
 * - 상단 여백
 */
.player-list {
  margin-top: 20px;
}
</style>
