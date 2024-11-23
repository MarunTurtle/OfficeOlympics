<script setup>
import MainLayout from "@/layouts/MainLayout.vue";
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useChallengeStore } from "@/stores/challenge";
import ChallengeCard from "@/components/ChallengeCard.vue";
import { formatNumber } from "@/utils/formatters";

const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const challengeStore = useChallengeStore();

const isLoggedIn = computed(() => authStore.isAuthenticated);
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

const leaderboard = ref([]);
const errorMessage = ref("");
const loading = ref(false);

// Slideshow functionality
const images = Array.from(
  { length: 9 },
  (_, i) => new URL(`../assets/images/mainpage/mp${i + 1}.png`, import.meta.url).href
);
const currentImageIndex = ref(Math.floor(Math.random() * images.length));
const slideInterval = ref(null);

const startSlideshow = () => {
  // Only start slideshow if user is not logged in or doesn't have Olympics
  if (!isLoggedIn.value || !hasOlympics.value) {
    slideInterval.value = setInterval(() => {
      currentImageIndex.value = (currentImageIndex.value + 1) % images.length;
    }, 5000);
  }
};

const getRankClass = (rank) => {
  if (rank === 1) return 'rank-1';
  if (rank === 2) return 'rank-2';
  if (rank === 3) return 'rank-3';
  return '';
};

const formatScore = (score) => {
  if (score === undefined || score === null) return '0';
  return formatNumber(score);
};

onMounted(async () => {
  startSlideshow();

  try {
    loading.value = true;
    await challengeStore.fetchMainPageData();

    // Check if Olympic ID exists in store or localStorage
    const olympicId = olympicStore.userOlympicId || localStorage.getItem('olympicsId');

    if (olympicId && isLoggedIn.value) {
      olympicStore.setUserOlympicId(olympicId); // Ensure store is updated
      leaderboard.value = challengeStore.leaderboard;
    }
  } catch (err) {
    console.error('Error loading main page data:', err);
    errorMessage.value = err.response?.data?.message || "Failed to load data. Please try again later.";
  } finally {
    loading.value = false;
  }
});

onBeforeUnmount(() => {
  if (slideInterval.value) {
    clearInterval(slideInterval.value);
  }
});
</script>

<template>
  <MainLayout>
    <div class="home-page">
      <!-- Case 1: Logged In & Has Olympics -->
      <template v-if="isLoggedIn && hasOlympics">
        <div class="leaderboard-section">
          <h2 class="text-center mb-4">현재 순위</h2>
          <div v-if="errorMessage" class="alert alert-danger text-center">
            {{ errorMessage }}
          </div>
          <div v-else-if="leaderboard.length === 0" class="text-center">
            아직 순위가 없습니다.
          </div>
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
          <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">로딩중...</span>
            </div>
          </div>
        </div>
      </template>

      <!-- Cases 2 & 3: Show slideshow for non-Olympics users -->
      <template v-else>
        <div class="slideshow-container">
          <img :src="images[currentImageIndex]" alt="슬라이드쇼 이미지" class="slideshow-image" />

          <!-- Case 2: Logged In & No Olympics -->
          <template v-if="isLoggedIn && !hasOlympics">
            <div class="hero-content">
              <h1 class="welcome-message">팀원들과 함께 올림픽을 열어보세요!</h1>
              <button class="btn nav-button nav-button-yellow mt-3" @click="$router.push('/olympic/create')">
                시작하기
              </button>
            </div>
          </template>

          <!-- Case 3: Logged Out -->
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

      <!-- Featured Challenges -->
      <div class="featured-challenges mt-5">
        <h2 class="text-center mb-4">추천 챌린지</h2>
        <div v-if="loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">로딩중...</span>
          </div>
        </div>
        <div v-else-if="challengeStore.challenges.length === 0" class="text-center">
          플레이 가능한 챌린지가 없습니다.
        </div>
        <div v-else class="challenges-grid">
          <ChallengeCard v-for="challenge in challengeStore.challenges" :key="challenge.challengeId"
            :id="challenge.challengeId" :title="challenge.challengeName" :description="challenge.challengeDesc"
            :videoUrl="challenge.challengeUrl" />
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.slideshow-container {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
}

.slideshow-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 2;
}

.welcome-message {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.7);
  margin-bottom: 1rem;
}

/* Add a dark overlay to make text more readable */
.slideshow-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}

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

.nav-button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}

.nav-button-yellow {
  background-color: var(--warning-color) !important;
}

.featured-challenges {
  padding: 0 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.challenges-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

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
