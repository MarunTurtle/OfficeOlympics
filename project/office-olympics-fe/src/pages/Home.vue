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

// Slideshow functionality
const images = Array.from(
  { length: 9 },
  (_, i) => new URL(`../assets/images/mainpage/mp${i + 1}.png`, import.meta.url).href
);
const currentImageIndex = ref(Math.floor(Math.random() * images.length));
const slideInterval = ref(null);

const startSlideshow = () => {
  slideInterval.value = setInterval(() => {
    currentImageIndex.value = (currentImageIndex.value + 1) % images.length;
  }, 5000); // Change image every 5 seconds
};

const getRankClass = (rank) => {
  if (rank === 1) return 'rank-1';
  if (rank === 2) return 'rank-2';
  if (rank === 3) return 'rank-3';
  return '';
};

const formatScore = (score) => {
  return formatNumber(score);
};

onMounted(async () => {
  startSlideshow();

  // Fetch leaderboard if needed
  if (isLoggedIn.value && hasOlympics.value) {
    try {
      leaderboard.value = await challengeStore.loadLeaderboard(olympicStore.userOlympicId);
    } catch (err) {
      errorMessage.value = "Failed to load leaderboard.";
    }
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
      <div class="slideshow-container">
        <img :src="images[currentImageIndex]" alt="Slideshow Image" class="slideshow-image" />

        <!-- Case 2: Logged In & No Olympics -->
        <template v-if="isLoggedIn && !hasOlympics">
          <div class="hero-content">
            <h1 class="welcome-message">Get your team moving!</h1>
            <button class="btn btn-warning mt-3" @click="$router.push('/olympic/create')">
              Get Started
            </button>
          </div>
        </template>

        <!-- Case 3: Logged Out -->
        <template v-else-if="!isLoggedIn">
          <div class="hero-content">
            <h1 class="welcome-message">Get your team moving!</h1>
            <button class="btn nav-button mt-3" @click="$router.push('/login')">
              Get Started
            </button>
          </div>
        </template>
      </div>

      <!-- Case 1: Logged In & Has Olympics -->
      <template v-if="isLoggedIn && hasOlympics">
        <div class="leaderboard-section">
          <h2 class="text-center mb-4">Current Rankings</h2>
          <div class="table-responsive">
            <table class="table table-hover">
              <thead class="table-light">
                <tr>
                  <th class="text-center">Rank</th>
                  <th>Player</th>
                  <th class="text-center">Score</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(player, index) in leaderboard" :key="index">
                  <td class="text-center">
                    <span :class="getRankClass(index + 1)">{{ index + 1 }}</span>
                  </td>
                  <td>{{ player.player_name }}</td>
                  <td class="text-center">{{ formatScore(player.total_score) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>

      <!-- Featured Challenges -->
      <div class="featured-challenges mt-5">
        <h2 class="text-center">Featured Challenges</h2>
        <div class="d-flex flex-wrap justify-content-center mt-3">
          <ChallengeCard v-for="challenge in challengeStore.challenges" :key="challenge.challenge_id"
            :id="challenge.challenge_id" :title="challenge.challenge_name" :videoUrl="challenge.challenge_url" />
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
</style>
