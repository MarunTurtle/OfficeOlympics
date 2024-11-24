<template>
  <MainLayout>
    <div class="my-page container py-5">
      <!-- Loading State -->
      <div v-if="loading" class="loading-spinner">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">로딩중...</span>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger shadow-sm">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        {{ error }}
      </div>

      <!-- Content -->
      <div v-else class="row g-4">
        <!-- User Profile Section -->
        <div class="col-md-4">
          <div class="card shadow-sm hover-shadow">
            <div class="card-body text-center p-4">
              <div class="mb-4">
                <img
                  :src="userData?.imgSrc || defaultProfileImage"
                  class="rounded-circle profile-image border border-3 border-white shadow"
                  alt="프로필 이미지"
                >
              </div>
              <h3 class="card-title fw-bold mb-2">{{ userData?.nickname }}</h3>
              <p class="text-muted mb-1">
                <i class="bi bi-envelope-fill me-2"></i>{{ userData?.email }}
              </p>
              <p class="text-muted">
                <i class="bi bi-person-fill me-2"></i>{{ userData?.name }}
              </p>
            </div>
          </div>
        </div>

        <!-- Players Section -->
        <div class="col-md-8">
          <div class="card shadow-sm hover-shadow">
            <div class="card-header bg-primary text-white py-3">
              <h4 class="mb-0 fw-bold">
                <i class="bi bi-trophy-fill me-2"></i>
                {{ players[0]?.olympics_name || '올림픽 정보' }}
              </h4>
            </div>
            <div class="card-body p-4">
              <div v-if="players.length === 0" class="text-center py-5">
                <i class="bi bi-people-fill text-muted fs-1 mb-3 d-block"></i>
                <p class="text-muted mb-0">생성된 올림픽이 없습니다</p>
              </div>
              <div v-else class="table-responsive">
                <table class="table table-hover align-middle">
                  <thead class="table-light">
                    <tr>
                      <th class="py-3">이름</th>
                      <th class="py-3 text-end">점수</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="player in players" :key="player.player_name">
                      <td class="py-3">
                        <i class="bi bi-person-circle me-2"></i>
                        {{ player.player_name }}
                      </td>
                      <td class="py-3 text-end fw-bold">
                        {{ player.total_score }}
                        <span class="ms-2">점</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="row mt-5">
        <div class="col-12 text-center">
          <button class="btn btn-primary btn-lg me-3 px-4 shadow-sm" @click="editProfile">
            <i class="bi bi-pencil-fill me-2"></i>프로필 수정
          </button>
          <button class="btn btn-outline-danger btn-lg px-4" @click="confirmDelete">
            <i class="bi bi-trash-fill me-2"></i>계정 삭제
          </button>
        </div>
      </div>
    </div>
    <EditProfileModal
      ref="editProfileModal"
      :userData="userData"
      :players="players"
      @update="fetchUserProfile"
    />
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useAuthStore } from '@/stores/auth';
import defaultProfileImage from '@/assets/images/default_profile.png';
import EditProfileModal from '@/components/EditProfileModal.vue';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();

const userData = ref(null);
const players = ref([]);
const loading = ref(true);
const error = ref(null);
const editProfileModal = ref(null);

const fetchUserProfile = async () => {
  try {
    loading.value = true;
    error.value = null;

    console.log('Auth Store User:', authStore.user);
    const userId = authStore.user?.id || authStore.user?.userId;
    console.log('Extracted User ID:', userId);

    if (!userId) {
      throw new Error('No user ID found');
    }

    const response = await userStore.fetchUser(userId);
    console.log('Profile Response:', response);

    userData.value = {
      ...response.data.userData,
      imgSrc: response.data.userData.ImgSrc || response.data.userData.imgSrc || defaultProfileImage
    };
    players.value = response.data.players;

  } catch (err) {
    console.error('Error fetching profile:', err);
    error.value = err.message || 'Failed to load profile data';
  } finally {
    loading.value = false;
  }
};

const editProfile = () => {
  editProfileModal.value.show();
};

const confirmDelete = async () => {
  if (confirm('정말로 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
    try {
      const userId = authStore.user?.id || authStore.user?.userId;
      if (!userId) {
        throw new Error('No user ID found');
      }
      await userStore.deleteUser(userId);
      await authStore.logoutUser();
      router.push('/');
    } catch (err) {
      error.value = '계정 삭제에 실패했습니다. 다시 시도해 주세요.';
    }
  }
};

onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
.my-page {
  max-width: 1200px;
  margin: 0 auto;
}

.loading-spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.profile-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
}

.card {
  border-radius: 8px;
  border: 1px solid var(--tertiary-color);
}

.card-header {
  background-color: var(--primary-color);
  color: white;
  border-bottom: 1px solid var(--tertiary-color);
}

.table th {
  font-weight: 600;
  color: #495057;
  background-color: var(--secondary-color);
}

.btn-primary {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  font-weight: 600;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  border-color: var(--interaction-hover-color);
}

.btn-outline-danger {
  color: var(--alert-color);
  border-color: var(--alert-color);
  font-weight: 600;
}

.btn-outline-danger:hover {
  background-color: var(--alert-color);
  border-color: var(--alert-color);
  color: white;
}
</style>
