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
    alert("모든 필수 항목을 입력해주세요.");
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

    alert("올림픽 이벤트가 성공적으로 생성되었습니다!");
    router.push("/");
  } catch (error) {
    console.error("Error creating Olympic event:", error);
    alert("올림픽 이벤트 생성에 실패했습니다. 다시 시도해주세요.");
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <MainLayout>
    <div class="olympic-create-form">
      <h1 class="text-center mb-4">오피스 올림픽 생성</h1>
      <form @submit.prevent="onCreateOlympic">
        <!-- Event Name -->
        <div class="mb-3">
          <label for="event-name" class="form-label">오피스 올림픽 이름</label>
          <input type="text" id="event-name" class="form-control" v-model="eventName"
            placeholder="오피스 올림픽 이름을 입력하세요" required />
          <small v-if="!eventName" class="text-danger">
            해당 항목은 필수입니다.
          </small>
        </div>

        <!-- Number of Players -->
        <div class="mb-3">
          <label for="player-count" class="form-label">플레이어 수</label>
          <input type="number" id="player-count" class="form-control" v-model.number="playerCount"
            @input="generatePlayerFields" placeholder="플레이어 수를 입력하세요" required />
          <small v-if="playerCount <= 0" class="text-danger">
            유효한 플레이어 수를 입력해주세요.
          </small>
        </div>

        <!-- Player Nicknames -->
        <div v-for="(player, index) in players" :key="player.id" class="mb-3">
          <label :for="'player-' + player.id" class="form-label">
            플레이어 {{ index + 1 }} 닉네임
          </label>
          <input type="text" :id="'player-' + player.id" class="form-control" v-model="player.nickname"
            placeholder="플레이어의 닉네임을 입력하세요" required />
          <small v-if="!player.nickname.trim()" class="text-danger">
            플레이어 {{ index + 1 }}의 닉네임은 필수입니다.
          </small>
        </div>

        <!-- Submit Button -->
        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? "생성 중..." : "올림픽 시작하기" }}
        </button>
      </form>
    </div>
  </MainLayout>
</template>

<style scoped>
.olympic-create-form {
  max-width: 30rem;
  margin: 20px auto;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); */
}

.form-control {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

.btn-primary:disabled {
  background-color: #ccc;
  transform: none;
}

.text-danger {
  color: var(--alert-color);
  font-size: 0.875rem;
}

h1 {
  color: var(--primary-color);
  font-weight: 600;
  font-size: 1.75rem;
  margin-bottom: 1rem;
}

.form-label {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.mb-3 {
  margin-bottom: 0.75rem !important;
}
</style>
