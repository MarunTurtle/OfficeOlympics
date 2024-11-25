/**
 * @파일명: Home.vue
 * @설명: 메인 홈페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/challenges (추천 챌린지 목록)
 *   - GET /api/olympics/{olympicId}/leaderboard (현재 순위)
 */

<script setup>
/**
 * @컴포넌트명: Home
 * @설명: 사용자 상태에 따라 다른 화면을 보여주는 홈페이지
 * @상태:
 *   - 로그인 + 올림픽 있음: 순위표와 추천 챌린지
 *   - 로그인 + 올림픽 없음: 올림픽 생성 유도
 *   - 비로그인: 로그인 유도
 * @데이터구조: {
 *   leaderboard: Array<{
 *     playerName: string,
 *     score: number
 *   }>,
 *   challenges: Array<{
 *     challengeId: string,
 *     challengeName: string,
 *     challengeDesc: string,
 *     challengeUrl: string
 *   }>
 * }
 */

// 필요한 Vue 컴포지션 API와 컴포넌트 임포트
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
import MainLayout from "@/layouts/MainLayout.vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useChallengeStore } from "@/stores/challenge";
import ChallengeCard from "@/components/ChallengeCard.vue";
import { formatNumber } from "@/utils/formatters";

/**
 * Store 초기화
 * - authStore: 인증 상태 관리
 * - olympicStore: 올림픽 정보 관리
 * - challengeStore: 챌린지 데이터 관리
 */
const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const challengeStore = useChallengeStore();

/**
 * 컴퓨티드 속성
 * - isLoggedIn: 사용자 로그인 상태
 * - hasOlympics: 올림픽 존재 여부
 */
const isLoggedIn = computed(() => authStore.isAuthenticated);
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

/**
 * 로컬 상태 관리
 * - leaderboard: 순위표 데이터
 * - errorMessage: 에러 메시지
 * - loading: 로딩 상태
 */
const leaderboard = ref([]);
const errorMessage = ref("");
const loading = ref(false);

/**
 * 슬라이드쇼 기능 구현
 * - 9개의 이미지를 순환
 * - 5초마다 이미지 변경
 * @returns {string[]} 이미지 URL 배열
 */
const images = Array.from(
  { length: 9 },
  (_, i) => new URL(`../assets/images/mainpage/mp${i + 1}.png`, import.meta.url).href
);
const currentImageIndex = ref(Math.floor(Math.random() * images.length));
const slideInterval = ref(null);

/**
 * 슬라이드쇼 시작 함수
 * 로그인하지 않았거나 올림픽이 없는 경우에만 실행
 */
const startSlideshow = () => {
  if (!isLoggedIn.value || !hasOlympics.value) {
    slideInterval.value = setInterval(() => {
      currentImageIndex.value = (currentImageIndex.value + 1) % images.length;
    }, 5000);
  }
};

/**
 * 순위별 스타일 클래스 반환
 * @param {number} rank - 순위
 * @returns {string} CSS 클래스명
 */
const getRankClass = (rank) => {
  if (rank === 1) return 'rank-1';
  if (rank === 2) return 'rank-2';
  if (rank === 3) return 'rank-3';
  return '';
};

/**
 * 점수 포맷팅 함수
 * @param {number} score - 포맷팅할 점수
 * @returns {string} 포맷팅된 점수 문자열
 */
const formatScore = (score) => {
  if (score === undefined || score === null) return '0';
  return formatNumber(score);
};

/**
 * 컴포넌트 마운트 시 초기화
 * - 슬라이드쇼 시작
 * - 메인 페이지 데이터 로드
 * - 올림픽 ID 확인 및 순위표 로드
 */
onMounted(async () => {
  startSlideshow();

  try {
    loading.value = true;
    await challengeStore.fetchMainPageData();

    const olympicId = olympicStore.userOlympicId || localStorage.getItem('olympicsId');

    if (olympicId && isLoggedIn.value) {
      olympicStore.setUserOlympicId(olympicId);
      leaderboard.value = challengeStore.leaderboard;
    }
  } catch (err) {
    console.error('Error loading main page data:', err);
    errorMessage.value = err.response?.data?.message || "데이터 로딩에 실패했습니다. 다시 시도해주세요.";
  } finally {
    loading.value = false;
  }
});

/**
 * 컴포넌트 언마운트 시 정리
 * - 슬라이드쇼 인터벌 제거
 */
onBeforeUnmount(() => {
  if (slideInterval.value) {
    clearInterval(slideInterval.value);
  }
});
</script>

<template>
  <MainLayout>
    <div class="home-page">
      <!--
        Case 1: 로그인 + 올림픽 있음
        - 순위표 표시
        - 추천 챌린지 목록 표시
      -->
      <template v-if="isLoggedIn && hasOlympics">
        <div class="leaderboard-section">
          <h2 class="text-center mb-4">현재 순위</h2>

          <!-- 에러 메시지 표시 -->
          <div v-if="errorMessage" class="alert alert-danger text-center">
            {{ errorMessage }}
          </div>

          <!-- 순위 데이터 없음 메시지 -->
          <div v-else-if="leaderboard.length === 0" class="text-center">
            아직 순위가 없습니다.
          </div>

          <!-- 순위표 표시 -->
          <div v-else class="table-responsive">
            <table class="table table-hover">
              <thead class="table-light">
                <tr>
                  <th class="text-center">순위</th>
                  <th>참가자</th>
                  <th class="text-center">점수</th>
                </tr>
              </thead>
              <tbody>
                <!-- 각 참가자의 순위 정보 -->
                <tr v-for="(player, index) in leaderboard" :key="index">
                  <td class="text-center">
                    <span :class="getRankClass(index + 1)">{{ index + 1 }}</span>
                  </td>
                  <td>{{ player.playerName }}</td>
                  <td class="text-center">{{ formatScore(player.score) }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 로딩 스피너 -->
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>
        </div>
      </template>

      <!--
        Cases 2 & 3: 비로그인 또는 올림픽 없음
        - 슬라이드쇼 배경
        - 상태별 다른 메시지와 버튼
      -->
      <template v-else>
        <div class="slideshow-container">
          <!-- 슬라이드쇼 이미지 -->
          <img :src="images[currentImageIndex]" alt="슬라이드쇼 이미지" class="slideshow-image" />

          <!-- Case 2: 로그인 + 올림픽 없음 -->
          <template v-if="isLoggedIn && !hasOlympics">
            <div class="hero-content">
              <h1 class="welcome-message">팀원들과 함께 올림픽을 열어보세요!</h1>
              <button class="btn nav-button nav-button-yellow mt-3" @click="$router.push('/olympic/create')">
                시작하기
              </button>
            </div>
          </template>

          <!-- Case 3: 비로그인 -->
          <template v-else>
            <div class="hero-content">
              <h1 class="welcome-message">오직 우리 사무실을 위한 오피스 올림픽</h1>
              <button class="btn nav-button mt-3" @click="$router.push('/auth/login')">
                시작하기
              </button>
            </div>
          </template>
        </div>
      </template>

      <!--
        추천 챌린지 섹션
        - 로딩 상태 처리
        - 챌린지 없음 메시지
        - 챌린지 그리드 표시
      -->
      <div class="featured-challenges mt-5 mb-5">
        <h2 class="text-center mb-4">추천 챌린지</h2>

        <!-- 로딩 스피너 -->
        <div v-if="loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">로딩중...</span>
          </div>
        </div>

        <!-- 챌린지 없음 메시지 -->
        <div v-else-if="challengeStore.challenges.length === 0" class="text-center">
          플레이 가능한 챌린지가 없습니다.
        </div>

        <!-- 챌린지 카드 그리드 -->
        <div v-else class="challenges-grid">
          <ChallengeCard
            v-for="challenge in challengeStore.challenges"
            :key="challenge.challengeId"
            :id="challenge.challengeId"
            :title="challenge.challengeName"
            :description="challenge.challengeDesc"
            :videoUrl="challenge.challengeUrl"
          />
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
/**
 * 슬라이드쇼 컨테이너 스타일링
 * - 전체 너비 사용
 * - 고정 높이 설정
 * - 오버플로우 숨김 처리
 */
.slideshow-container {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
}

/* 슬라이드쇼 이미지 스타일링 - 컨테이너에 맞춤 */
.slideshow-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/**
 * 히어로 콘텐츠 스타일링
 * - 절대 위치로 중앙 정렬
 * - 오버레이보다 위에 표시 (z-index)
 */
.hero-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 2;
}

/**
 * 환영 메시지 스타일링
 * - 큰 폰트 사이즈
 * - 굵은 폰트 웨이트
 * - 그림자로 가독성 향상
 */
.welcome-message {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.7);
  margin-bottom: 1rem;
}

/**
 * 슬라이드쇼 다크 오버레이
 * - 텍스트 가독성 향상을 위한 반투명 검은 배경
 * - 가상 요소로 구현
 */
.slideshow-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}

/**
 * 네비게이션 버튼 스타일링
 * - 일관된 크기와 패딩
 * - 부드러운 전환 효과
 * - 호버 시 스케일 변화
 */
.nav-button {
  font-size: 1rem;
  font-weight: 600;
  padding: 8px 16px;
  margin: 0 5px;
  color: #fff;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

/* 네비게이션 버튼 호버 효과 */
.nav-button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}

/* 노란색 버튼 변형 */
.nav-button-yellow {
  background-color: var(--warning-color) !important;
}

/**
 * 추천 챌린지 섹션 스타일링
 * - 최대 너비 제한
 * - 좌우 패딩
 * - 중앙 정렬
 */
.featured-challenges {
  padding: 0 2rem;
  max-width: 1400px;
}

/**
 * 챌린지 그리드 레이아웃
 * - 3열 그리드
 * - 반응형 그리드 시스템
 * - 일정한 간격
 */
.challenges-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

/**
 * 반응형 미디어 쿼리
 * - 1200px 미만: 2열 그리드
 * - 768px 미만: 1열 그리드
 */
@media (max-width: 1200px) {
  .challenges-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .challenges-grid {
    grid-template-columns: 1fr;
  }
}
</style>
