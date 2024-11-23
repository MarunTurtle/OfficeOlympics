<template>
  <div class="modal fade" id="editProfileModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">프로필 수정</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <!-- Nickname -->
            <div class="mb-3">
              <label class="form-label">닉네임</label>
              <input
                type="text"
                class="form-control input-field"
                v-model="formData.nickname"
                required
              >
            </div>

            <!-- Profile Image -->
            <div class="mb-3">
              <label class="form-label">프로필 이미지</label>
              <input
                type="file"
                class="form-control input-field"
                @change="handleFileChange"
                accept="image/jpeg,image/png"
              >
            </div>

            <!-- Olympics Name -->
            <div class="mb-3">
              <label class="form-label">올림픽 이름</label>
              <input
                type="text"
                class="form-control input-field"
                v-model="formData.olympicsName"
                required
              >
            </div>

            <!-- Player Names -->
            <div class="mb-3">
              <label class="form-label">올림픽 플레이어</label>
              <div v-for="(player, index) in formData.playerNames" :key="index">
                <input
                  type="text"
                  class="form-control input-field mb-2"
                  v-model="formData.playerNames[index]"
                  :placeholder="`플레이어 ${index + 1}`"
                  required
                >
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-primary"
            style="width: 48%;"
            @click="handleSubmit"
            :disabled="loading"
          >
            {{ loading ? '저장 중...' : '변경사항 저장' }}
          </button>
          <button
            type="button"
            class="btn btn-secondary cancel-button"
            style="width: 48%;"
            data-bs-dismiss="modal"
          >
            취소
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { Modal } from 'bootstrap';
import { useUserStore } from '@/stores/user';

const props = defineProps({
  userData: {
    type: Object,
    required: true
  },
  players: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['update']);

const userStore = useUserStore();
const modal = ref(null);
const loading = ref(false);

const formData = ref({
  nickname: '',
  profileImg: null,
  olympicsName: '',
  playerNames: ['']
});

watch(() => props.userData, (newData) => {
  if (newData) {
    formData.value.nickname = newData.nickname || '';
  }
}, { immediate: true });

watch(() => props.players, (newPlayers) => {
  if (newPlayers && newPlayers.length > 0) {
    formData.value.olympicsName = newPlayers[0].olympics_name || '';
    formData.value.playerNames = newPlayers.map(p => p.player_name);
  }
}, { immediate: true });

const handleFileChange = (event) => {
  formData.value.profileImg = event.target.files[0];
};

const handleSubmit = async () => {
  try {
    loading.value = true;
    const submitData = new FormData();

    // Required fields
    submitData.append('nickname', formData.value.nickname);
    submitData.append('olympicsName', formData.value.olympicsName);

    // Add player names as separate entries
    formData.value.playerNames.forEach(name => {
      submitData.append('playerNames', name);
    });

    // Optional profile image
    if (formData.value.profileImg) {
      submitData.append('profileImg', formData.value.profileImg);
    }

    await userStore.updateUser(props.userData.userId, submitData);
    modal.value.hide();
    emit('update');
  } catch (error) {
    console.error('Failed to update profile:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  modal.value = new Modal(document.getElementById('editProfileModal'));
});

defineExpose({
  show: () => modal.value.show(),
  hide: () => modal.value.hide()
});
</script>

<style scoped>
/* Add consistent styling */
.form-control {
  background: var(--tertiary-color);
  border-radius: 8px;
  /* padding: 0.5rem; */
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

.btn-secondary {
  background-color: grey;
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  color: black;
}

.btn-secondary:hover {
  background-color: darkgrey;
  transform: scale(1.02);
}

.modal-content {
  border-radius: 12px;
}

.modal-header {
  border-bottom: none;
}

.modal-footer {
  border-top: none;
  display: flex;
  justify-content: space-between;
  padding: 1rem 1rem;
}

.modal-title {
  color: var(--primary-color);
  font-weight: 600;
}

.form-label {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.mb-3 {
  margin-bottom: 0.75rem !important;
}

.cancel-button {
  background-color: var(--tertiary-color);
}
</style>
