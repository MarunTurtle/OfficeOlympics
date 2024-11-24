/**
 * @파일명: OlympicCreate.vue
 * @설명: 오피스 올림픽 생성 페이지 컴포넌트
 * @관련백엔드:
 *   - POST /api/olympics (올림픽 이벤트 생성)
 */

<script setup>
/**
 * @컴포넌트명: OlympicCreate
 * @설명: 올림픽 이벤트 생성 및 참가자 등록
 * @상태:
 *   - eventName: 올림픽 이벤트 이름
 *   - playerCount: 참가자 수
 *   - players: 참가자 목록
 *   - loading: 로딩 상태
 */

import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import { useOlympicStore } from "@/stores/olympic";

// 라우터 및 스토어 초기화
const router = useRouter();
const olympicStore = useOlympicStore();

// 폼 상태 관리
const eventName = ref("");
const playerCount = ref(0);
const players = ref([]);
const loading = ref(false);

// 유효성 검사 상태
const eventNameError = ref(false);
const playerCountError = ref(false);
const playerErrors = ref([]);

// 최대 참가자 수 제한
const maxPlayerCount = 10;

/**
 * 참가자 수 유효성 검사
 * @returns {boolean} 참가자 수가 유효한지 여부
 */
const isPlayerCountValid = computed(() => {
  return playerCount.value > 0 && playerCount.value <= maxPlayerCount;
});

/**
 * 중복 닉네임 검사
 * @returns {boolean} 중복된 닉네임이 있는지 여부
 */
const hasDuplicateNicknames = computed(() => {
  const nicknames = players.value.map(p => p.nickname.trim().toLowerCase());
  return new Set(nicknames).size !== nicknames.length;
});

/**
 * 참가자 입력 필드 생성
 * - playerCount에 따라 동적으로 입력 필드 생성
 * - 각 참가자의 에러 상태도 함께 초기화
 */
const generatePlayerFields = () => {
  players.value = Array.from({ length: playerCount.value }, (_, index) => ({
    id: index + 1,
    nickname: "",
  }));
  playerErrors.value = Array(playerCount.value).fill(false);
};

/**
 * 폼 전체 유효성 검사
 * - 이벤트 이름 검증
 * - 참가자 수 검증
 * - 모든 참가자 닉네임 검증
 * - 닉네임 중복 검사
 */
const isFormValid = computed(() => {
  const eventNameValid = eventName.value.trim().length > 0;
  const playerCountValid = isPlayerCountValid.value;
  const allPlayersValid = players.value.every((player) => player.nickname.trim().length > 0);
  const noDuplicates = !hasDuplicateNicknames.value;

  return eventNameValid && playerCountValid && allPlayersValid && noDuplicates;
});

/**
 * 올림픽 이벤트 생성 처리
 * - 폼 유효성 검사
 * - 올림픽 생성 요청
 * - 성공 시 홈페이지로 리다이렉션
 */
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
    <!-- 올림픽 생성 폼 -->
    <div class="olympic-create-form">
      <h1 class="text-center mb-4">오피스 올림픽 생성</h1>
      <form @submit.prevent="onCreateOlympic">
        <!-- 이벤트 이름 입력 -->
        <div class="mb-3">
          <label for="event-name" class="form-label">오피스 올림픽 이름</label>
          <input type="text" id="event-name" class="form-control" v-model="eventName"
            placeholder="오피스 올림픽 이름을 입력하세요" required />
          <small v-if="!eventName" class="text-danger">
            해당 항목은 필수입니다.
          </small>
        </div>

        <!-- 참가자 수 입력 -->
        <div class="mb-3">
          <label for="player-count" class="form-label">플레이어 수</label>
          <input type="number" id="player-count" class="form-control" v-model.number="playerCount"
            @input="generatePlayerFields" placeholder="플레이어 수를 입력하세요" required />
          <small v-if="playerCount <= 0" class="text-danger">
            유효한 플레이어 수를 입력해주세요.
          </small>
        </div>

        <!-- 참가자 닉네임 입력 -->
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

        <!-- 제출 버튼 -->
        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? "생성 중..." : "올림픽 시작하기" }}
        </button>
      </form>
    </div>
  </MainLayout>
</template>

<style scoped>
/* 폼 컨테이너 */
.olympic-create-form {
  max-width: 30rem;
  margin: 20px auto;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
}

/* 입력 필드 기본 스타일 */
.form-control {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

/* 입력 필드 포커스 스타일 */
.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

/* 기본 버튼 스타일 */
.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

/* 버튼 호버 효과 */
.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

/* 비활성화된 버튼 스타일 */
.btn-primary:disabled {
  background-color: #ccc;
  transform: none;
}

/* 에러 메시지 스타일 */
.text-danger {
  color: var(--alert-color);
  font-size: 0.875rem;
}

/* 제목 스타일 */
h1 {
  color: var(--primary-color);
  font-weight: 600;
  font-size: 1.75rem;
  margin-bottom: 1rem;
}

/* 라벨 스타일 */
.form-label {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

/* 하단 여백 유틸리티 클래스 */
.mb-3 {
  margin-bottom: 0.75rem !important;
}
</style>
