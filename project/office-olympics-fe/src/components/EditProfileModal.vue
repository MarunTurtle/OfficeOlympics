<template>
  <div class="modal fade" id="editProfileModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Profile</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <!-- Add preview of current profile image -->
            <div class="mb-3 text-center">
              <img
                :src="currentImageSrc"
                class="rounded-circle profile-preview mb-3"
                alt="Profile Preview"
              >
            </div>

            <!-- Nickname -->
            <div class="mb-3">
              <label class="form-label">Nickname</label>
              <input
                type="text"
                class="form-control"
                v-model="formData.nickname"
                required
              >
            </div>

            <!-- Profile Image -->
            <div class="mb-3">
              <label class="form-label">Profile Image</label>
              <input
                type="file"
                class="form-control"
                @change="handleFileChange"
                accept="image/jpeg,image/png"
              >
            </div>

            <!-- Olympics Name -->
            <div class="mb-3">
              <label class="form-label">Olympics Name</label>
              <input
                type="text"
                class="form-control"
                v-model="formData.olympicsName"
                required
              >
            </div>

            <!-- Player Names -->
            <div class="mb-3">
              <label class="form-label">Team Members</label>
              <div v-for="(player, index) in formData.playerNames" :key="index" class="d-flex gap-2 mb-2">
                <input
                  type="text"
                  class="form-control"
                  v-model="formData.playerNames[index]"
                  :placeholder="`Player ${index + 1}`"
                  required
                >
                <button
                  type="button"
                  class="btn btn-danger"
                  @click="removePlayer(index)"
                  v-if="formData.playerNames.length > 1"
                >
                  X
                </button>
              </div>
              <button type="button" class="btn btn-secondary mt-2" @click="addPlayer">
                Add Player
              </button>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            @click="handleSubmit"
            :disabled="loading"
          >
            {{ loading ? 'Saving...' : 'Save Changes' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { Modal } from 'bootstrap';
import { useUserStore } from '@/stores/user';
import defaultProfileImage from '@/assets/images/default_profile.png';

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

const currentImageSrc = computed(() => {
  if (formData.value.profileImg) {
    return URL.createObjectURL(formData.value.profileImg);
  }
  return props.userData?.ImgSrc || defaultProfileImage;
});

const formData = ref({
  nickname: '',
  profileImg: null,
  olympicsName: '',
  playerNames: [''],
  currentImgSrc: ''
});

watch(() => props.userData, (newData) => {
  if (newData) {
    formData.value.nickname = newData.nickname || '';
    formData.value.currentImgSrc = newData.ImgSrc || '';
  }
}, { immediate: true });

watch(() => props.players, (newPlayers) => {
  if (newPlayers && newPlayers.length > 0) {
    formData.value.olympicsName = newPlayers[0].olympics_name || '';
    formData.value.playerNames = newPlayers.map(p => p.player_name);
  }
}, { immediate: true });

const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    formData.value.profileImg = file;
  }
};

const addPlayer = () => {
  formData.value.playerNames.push('');
};

const removePlayer = (index) => {
  formData.value.playerNames.splice(index, 1);
};

const handleSubmit = async () => {
  try {
    loading.value = true;
    const submitData = new FormData();

    // Required field
    submitData.append('nickname', formData.value.nickname);

    // Optional fields
    if (formData.value.olympicsName) {
      submitData.append('olympicsName', formData.value.olympicsName);
    }

    // Add player names as separate entries
    if (formData.value.playerNames && formData.value.playerNames.length > 0) {
      formData.value.playerNames.forEach(name => {
        if (name.trim()) {  // Only append non-empty names
          submitData.append('playerNames', name.trim());
        }
      });
    }

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

onBeforeUnmount(() => {
  // Clean up any object URLs we created
  if (formData.value.profileImg) {
    URL.revokeObjectURL(currentImageSrc.value);
  }
});
</script>

<style scoped>
.profile-preview {
  width: 150px;
  height: 150px;
  object-fit: cover;
}
</style>
