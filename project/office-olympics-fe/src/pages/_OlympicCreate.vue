<script setup>
import { ref, computed, watch } from "vue";
import { useOlympicStore } from "@/stores/olympic";
import { useRouter } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import { isNotEmpty, isPositiveInteger } from "@/utils/validation";
import { capitalize } from "@/utils/formatters";

const router = useRouter();
const olympicStore = useOlympicStore();

const eventName = ref("");
const playerCount = ref(0);
const players = ref([]);
const loading = ref(false);

// Validation errors
const eventNameError = ref(false);
const playerCountError = ref(false);
const playerErrors = ref([]);

// Add touched state for inputs to show error messages only after interaction
const eventNameTouched = ref(false);
const playerCountTouched = ref(false);
const playerTouched = ref([]);

// Function to generate player input fields dynamically
const generatePlayerFields = () => {
  players.value = Array.from({ length: playerCount.value }, (_, index) => ({
    id: index + 1,
    nickname: "",
  }));
  playerErrors.value = Array(playerCount.value).fill(false);
  playerTouched.value = Array(playerCount.value).fill(false);
};

// Watch inputs for validation
watch(eventName, (newValue) => {
  if (eventNameTouched.value) {
    eventNameError.value = !isNotEmpty(newValue);
  }
});

watch(playerCount, (newValue) => {
  if (playerCountTouched.value) {
    playerCountError.value = !isPositiveInteger(newValue);
  }
  if (newValue > 0) {
    generatePlayerFields();
  }
});

watch(players, (newPlayers) => {
  playerErrors.value = newPlayers.map((player, index) =>
    playerTouched.value[index] && !isNotEmpty(player.nickname)
  );
});

// Check if form is valid
const isFormValid = computed(() => {
  const areAllPlayersValid = players.value.every((player) =>
    isNotEmpty(player.nickname)
  );

  return (
    isNotEmpty(eventName.value) &&
    isPositiveInteger(playerCount.value) &&
    areAllPlayersValid
  );
});

// Submit function to create Olympic event
const onCreateOlympic = async () => {
  eventNameTouched.value = true;
  playerCountTouched.value = true;
  playerTouched.value = playerTouched.value.map(() => true);

  if (!isFormValid.value) {
    alert("Please correct the errors before submitting.");
    return;
  }

  loading.value = true;
  try {
    const olympicData = {
      olympicsName: capitalize(eventName.value),
      playerNames: players.value.map((player) => capitalize(player.nickname)),
    };
    const response = await olympicStore.createOlympicEvent(olympicData);
    olympicStore.setUserOlympicId(response.olympicsId);
    alert("Olympic event created successfully!");
    router.push("/"); // Navigate to the main page
  } catch (error) {
    console.error("Error creating Olympic:", error);
    alert("Failed to create Olympic event.");
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
        <!-- Input for Event Name -->
        <div class="mb-3">
          <label for="event-name" class="form-label">Event Name</label>
          <input
            type="text"
            id="event-name"
            class="form-control"
            v-model="eventName"
            @blur="eventNameTouched.value = true"
            placeholder="Enter the name of the Olympic event"
            required
          />
          <small v-if="eventNameError" class="text-danger">
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
            @blur="playerCountTouched.value = true"
            placeholder="Enter the number of players"
            required
          />
          <small v-if="playerCountError" class="text-danger">
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
            @blur="playerTouched.value[index] = true"
            placeholder="Enter nickname"
            required
          />
          <small
            v-if="playerErrors[index]"
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
  font-family: 'Pretendard', sans-serif;
}

h1 {
  font-size: 2.5rem;
  color: var(--primary-color);
  font-family: 'GongGothicMedium', sans-serif;
  text-align: center;
}

.form-label {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 5px;
}

input,
button {
  font-family: 'Pretendard', sans-serif;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  width: 100%;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

button {
  background-color: var(--primary-color);
  color: #fff;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease, transform 0.2s ease;
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

small {
  display: block;
  margin-top: -8px;
  margin-bottom: 10px;
}
</style>