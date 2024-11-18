<script setup>
import MainLayout from "@/layouts/MainLayout.vue";
import { ref, computed, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useChallengeStore } from "@/stores/challenge";
import ChallengeCard from "@/components/ChallengeCard.vue";

const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const challengeStore = useChallengeStore();

const isLoggedIn = computed(() => authStore.isAuthenticated);
const hasOlympics = computed(() => olympicStore.userOlympicId !== null);
const leaderboard = ref([]);
const errorMessage = ref("");

// Fetch leaderboard if user has an Olympic
onMounted(async () => {
  if (isLoggedIn.value && hasOlympics.value) {
    try {
      leaderboard.value = await challengeStore.loadLeaderboard(
        olympicStore.userOlympicId
      );
    } catch (err) {
      errorMessage.value = "Failed to load leaderboard.";
    }
  }
});
</script>

<template>
  <MainLayout>
    <div class="home-page">
      <!-- Case 1: Logged In & Has Olympics -->
      <template v-if="isLoggedIn && hasOlympics">
        <h1 class="text-center mt-4">Leaderboard</h1>
        <table class="table table-striped text-center mt-3">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Nickname</th>
              <th>Total Score</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(player, index) in leaderboard" :key="index">
              <td>{{ index + 1 }}</td>
              <td>{{ player.player_name }}</td>
              <td>{{ player.total_score }}</td>
            </tr>
          </tbody>
        </table>
      </template>

      <!-- Case 2: Logged In & No Olympics -->
      <template v-else-if="isLoggedIn && !hasOlympics">
        <div class="hero-section">
          <img src="@/assets/images/mainpage.png" alt="Main Page" class="main-img" />
          <div class="hero-content">
            <h1 class="welcome-message">Get your team moving!</h1>
            <button
              class="btn btn-success mt-3"
              @click="$router.push('/olympic/create')"
            >
              Get Started
            </button>
          </div>
        </div>
      </template>

      <!-- Case 3: Logged Out -->
      <template v-else>
        <div class="hero-section">
          <img src="@/assets/images/mainpage.png" alt="Main Page" class="main-img" />
          <div class="hero-content">
            <h1 class="welcome-message">Get your team moving!</h1>
            <button
              class="btn btn-primary mt-3"
              @click="$router.push('/login')"
            >
              Get Started
            </button>
          </div>
        </div>
      </template>

      <!-- Featured Challenges -->
      <div class="featured-challenges mt-5">
        <h2 class="text-center">Featured Challenges</h2>
        <div class="d-flex flex-wrap justify-content-center mt-3">
          <ChallengeCard
            v-for="challenge in challengeStore.challenges"
            :key="challenge.challenge_id"
            :thumbnail="challenge.thumbnail"
            :title="challenge.challenge_name"
            :description="challenge.challenge_desc"
            :id="challenge.challenge_id"
          />
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
/* Hero Section */
.hero-section {
  position: relative;
  text-align: center;
  color: white;
}

.main-img {
  width: 100%;
  height: auto;
  opacity: 50%;
}

.hero-content {
  position: absolute;
  top: 50%; /* Position content in the middle of the image */
  left: 50%;
  transform: translate(-50%, -50%); /* Center content horizontally and vertically */
  text-align: center;
}

.welcome-message {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.7);
}

.hero-content .btn {
  font-size: 1.2rem;
  padding: 10px 20px;
  border-radius: 5px;
}
</style>
