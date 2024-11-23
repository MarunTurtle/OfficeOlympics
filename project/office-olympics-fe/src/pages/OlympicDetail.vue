<template>
  <MainLayout>
    <div class="olympic-detail">
      <h1 class="text-center mb-4">{{ olympic?.name }}</h1>
      <p class="text-center text-muted">올림픽 ID: {{ olympic?.id }}</p>

      <!-- Player List -->
      <div class="player-list mt-5">
        <h2>플레이어</h2>
        <ul class="list-group">
          <li
            v-for="(player, index) in players"
            :key="player.id"
            class="list-group-item"
          >
            <strong>플레이어 {{ index + 1 }}:</strong> {{ player.nickname }}
          </li>
        </ul>
      </div>

      <!-- Navigation Buttons -->
      <div class="mt-5 text-center">
        <RouterLink to="/main" class="btn btn-secondary">홈으로 돌아가기</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MainLayout from '@/layouts/MainLayout.vue';
import { useOlympicStore } from '@/stores/olympic';

// Olympic Store
const olympicStore = useOlympicStore();

// State
const olympic = ref(null);
const players = ref([]);

// Fetch Olympic details and players on component mount
const olympicId = 1; // Replace this with a dynamic ID from the route or props
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
.olympic-detail {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.player-list {
  margin-top: 20px;
}
</style>
