<template>
  <MainLayout>
    <div class="olympic-create-form">
      <h1 class="text-center mb-4">Create an Olympic Event</h1>
      <form @submit.prevent="onCreateOlympic">
        <!-- Input for Event Name -->
        <div class="mb-3">
          <label for="event-name" class="form-label">Event Name</label>
          <input
            type="text"
            id="event-name"
            class="form-control"
            v-model="eventName"
            placeholder="Enter the name of the Olympic event"
            required
          />
          <small v-if="!isNotEmpty(eventName.value)" class="text-danger">
            Event name is required.
          </small>
        </div>

        <!-- Input for Number of Players -->
        <div class="mb-3">
          <label for="player-count" class="form-label">Number of Players</label>
          <input
            type="number"
            id="player-count"
            class="form-control"
            v-model.number="playerCount"
            placeholder="Enter the number of players"
            @change="generatePlayerFields"
            required
          />
          <small v-if="!isPositiveInteger(playerCount.value)" class="text-danger">
            Please enter a positive number for players.
          </small>
        </div>

        <!-- Dynamically Generated Player Nicknames -->
        <div v-for="(player, index) in players" :key="index" class="mb-3">
          <label :for="'player-' + index" class="form-label">
            Player {{ index + 1 }} Nickname
          </label>
          <input
            type="text"
            :id="'player-' + index"
            class="form-control"
            v-model="players[index].nickname"
            placeholder="Enter nickname"
            required
          />
          <small
            v-if="!isNotEmpty(players[index].nickname)"
            class="text-danger"
          >
            Nickname is required for player {{ index + 1 }}.
          </small>
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          class="btn btn-primary w-100"
          :disabled="!isFormValid || loading"
        >
          {{ loading ? 'Creating...' : 'Start Olympics' }}
        </button>
      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useOlympicStore } from '@/stores/olympic';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { isNotEmpty, isPositiveInteger } from '@/utils/validation';
import { capitalize } from '@/utils/formatters';

const router = useRouter();
const olympicStore = useOlympicStore();

const eventName = ref('');
const playerCount = ref(0);
const players = ref([]);
const loading = ref(false);

// Function to generate player input fields dynamically
const generatePlayerFields = () => {
  players.value = Array.from({ length: playerCount.value }, (_, index) => ({
    id: index + 1,
    nickname: '',
  }));
};

// Form Validation
const isFormValid = computed(() => {
  return (
    isNotEmpty(eventName.value) &&
    isPositiveInteger(playerCount.value) &&
    players.value.every(player => isNotEmpty(player.nickname))
  );
});

// Submit function to create Olympic event
const onCreateOlympic = async () => {
  if (!isFormValid.value) {
    alert('Please correct the errors before submitting.');
    return;
  }

  loading.value = true;
  try {
    const olympicData = {
      name: capitalize(eventName.value),
      playerCount: playerCount.value,
      players: players.value.map(player => capitalize(player.nickname)),
    };
    await olympicStore.createOlympicEvent(olympicData);
    alert('Olympic event created successfully!');
    router.push('/main');
  } catch (error) {
    console.error('Error creating Olympic:', error);
    alert('Failed to create Olympic event.');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.olympic-create-form {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
