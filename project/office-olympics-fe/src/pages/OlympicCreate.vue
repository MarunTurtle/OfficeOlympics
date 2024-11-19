<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import { useOlympicStore } from "@/stores/olympic";

const router = useRouter();
const olympicStore = useOlympicStore();

const eventName = ref("");
const playerCount = ref(0);
const players = ref([]);
const loading = ref(false);

const eventNameError = ref(false);
const playerCountError = ref(false);
const playerErrors = ref([]);

const maxPlayerCount = 10;

const isPlayerCountValid = computed(() => {
  return playerCount.value > 0 && playerCount.value <= maxPlayerCount;
});

const hasDuplicateNicknames = computed(() => {
  const nicknames = players.value.map(p => p.nickname.trim().toLowerCase());
  return new Set(nicknames).size !== nicknames.length;
});

// Function to generate player input fields dynamically
const generatePlayerFields = () => {
  players.value = Array.from({ length: playerCount.value }, (_, index) => ({
    id: index + 1,
    nickname: "",
  }));
  playerErrors.value = Array(playerCount.value).fill(false);
};

// Validate the entire form
const isFormValid = computed(() => {
  const eventNameValid = eventName.value.trim().length > 0;
  const playerCountValid = isPlayerCountValid.value;
  const allPlayersValid = players.value.every((player) => player.nickname.trim().length > 0);
  const noDuplicates = !hasDuplicateNicknames.value;

  return eventNameValid && playerCountValid && allPlayersValid && noDuplicates;
});

// Submit handler for creating Olympic event
const onCreateOlympic = async () => {
  if (!isFormValid.value) {
    alert("Please fill out all required fields.");
    return;
  }

  loading.value = true;
  try {
    const olympicData = {
      olympicsName: eventName.value.trim(),
      playerNames: players.value.map((player) => player.nickname.trim()),
    };

    const response = await olympicStore.createOlympicEvent(olympicData);
    olympicStore.setUserOlympicId(response.olympicsId);

    alert("Olympic event created successfully!");
    router.push("/");
  } catch (error) {
    console.error("Error creating Olympic event:", error);
    alert("Failed to create Olympic event. Please try again.");
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <MainLayout>
    <div class="olympic-create-form">
      <h1 class="text-center mb-4">Create an Olympic Event</h1>
      <form @submit.prevent="onCreateOlympic">
        <!-- Event Name -->
        <div class="mb-3">
          <label for="event-name" class="form-label">Event Name</label>
          <input type="text" id="event-name" class="form-control" v-model="eventName"
            placeholder="Enter the Olympic event name" required />
          <small v-if="!eventName" class="text-danger">
            Event name is required.
          </small>
        </div>

        <!-- Number of Players -->
        <div class="mb-3">
          <label for="player-count" class="form-label">Number of Players</label>
          <input type="number" id="player-count" class="form-control" v-model.number="playerCount"
            @input="generatePlayerFields" placeholder="Enter the number of players" required />
          <small v-if="playerCount <= 0" class="text-danger">
            Please enter a valid number of players.
          </small>
        </div>

        <!-- Player Nicknames -->
        <div v-for="(player, index) in players" :key="player.id" class="mb-3">
          <label :for="'player-' + player.id" class="form-label">
            Player {{ index + 1 }} Nickname
          </label>
          <input type="text" :id="'player-' + player.id" class="form-control" v-model="player.nickname"
            placeholder="Enter the player's nickname" required />
          <small v-if="!player.nickname.trim()" class="text-danger">
            Nickname is required for Player {{ index + 1 }}.
          </small>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? "Creating..." : "Start Olympics" }}
        </button>
      </form>
    </div>
  </MainLayout>
</template>

<style scoped>
.olympic-create-form {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  background-color: var(--secondary-color);
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
  font-size: 2rem;
  color: var(--primary-color);
}

.form-label {
  font-size: 1rem;
  font-weight: bold;
}

input {
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
}

input:focus {
  border-color: var(--primary-color);
  outline: none;
}

button {
  background-color: var(--primary-color);
  color: white;
  padding: 10px;
  font-weight: bold;
  transition: background-color 0.3s, transform 0.2s;
}

button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.text-danger {
  color: var(--alert-color);
  font-size: 0.875rem;
}
</style>
