<template>
  <MainLayout>
    <div class="my-page container py-4">
      <!-- Loading State -->
      <div v-if="loading" class="text-center">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger text-center">
        {{ error }}
      </div>

      <!-- Content -->
      <div v-else>
        <!-- Header -->
        <div class="profile-header text-center mb-5">
          <h1>My Profile</h1>
          <p class="text-muted">Manage your profile and account settings</p>
        </div>

        <!-- Profile Information -->
        <div class="row">
          <div class="col-md-6 mb-4">
            <div class="card">
              <div class="card-body">
                <h2 class="card-title">Profile Details</h2>
                <div class="profile-info">
                  <p><strong>Name:</strong> {{ user?.name ? formatName(user.name) : 'N/A' }}</p>
                  <p><strong>Email:</strong> {{ user?.email || 'N/A' }}</p>
                  <p><strong>Nickname:</strong> {{ user?.nickname ? formatName(user.nickname) : 'N/A' }}</p>
                  <div class="mt-3">
                    <button class="btn btn-primary me-2" @click="showEditModal">
                      Edit Profile
                    </button>
                    <button class="btn btn-danger" @click="confirmDeleteAccount">
                      Delete Account
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Olympic Information -->
          <div class="col-md-6 mb-4">
            <div class="card">
              <div class="card-body">
                <h2 class="card-title">Olympic Details</h2>
                <div v-if="players?.length > 0">
                  <h3 class="text-center mb-3">{{ players[0]?.olympics_name || 'Unnamed Olympic' }}</h3>
                  <div class="table-responsive">
                    <table class="table table-hover">
                      <thead>
                        <tr>
                          <th>Player Name</th>
                          <th class="text-end">Total Score</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="player in players" :key="player.player_name || Math.random()">
                          <td>{{ player.player_name || 'Unnamed Player' }}</td>
                          <td class="text-end">{{ player.total_score ? formatNumber(player.total_score) : '0' }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <button class="btn btn-danger mt-3" @click="confirmDeleteOlympic">
                    Delete Olympic
                  </button>
                </div>
                <div v-else class="text-center">
                  <p>No Olympic data available</p>
                  <RouterLink to="/olympic/create" class="btn btn-primary">
                    Create Olympic
                  </RouterLink>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Edit Profile Modal -->
      <div class="modal fade" id="editProfileModal" tabindex="-1" ref="editModal">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Edit Profile</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="handleEditSubmit">
                <div class="mb-3">
                  <label class="form-label">Nickname</label>
                  <input type="text" class="form-control" v-model="editForm.nickname" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Profile Image</label>
                  <input type="file" class="form-control" @change="handleImageChange" accept="image/*">
                </div>
                <div class="mb-3">
                  <label class="form-label">Olympics Name</label>
                  <input type="text" class="form-control" v-model="editForm.olympicsName" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Player Names</label>
                  <div v-for="(player, index) in editForm.playerNames" :key="index">
                    <input type="text" class="form-control mb-2" v-model="editForm.playerNames[index]" required>
                  </div>
                </div>
                <button type="submit" class="btn btn-primary">Save Changes</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useOlympicStore } from '@/stores/olympic';
import { capitalize } from '@/utils/formatters';
import { Modal } from 'bootstrap';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const olympicStore = useOlympicStore();

const user = ref(null);
const players = ref([]);
const editModal = ref(null);
const editForm = ref({
  nickname: '',
  profileImg: null,
  olympicsName: '',
  playerNames: []
});

const loading = ref(true);
const error = ref(null);

// Fetch user profile data
const fetchUserProfile = async () => {
  try {
    loading.value = true;
    error.value = null;
    const userId = route.params.userId;
    const response = await userStore.fetchUser(userId);

    if (!response || !response.userData) {
      throw new Error('No user data received');
    }

    user.value = response.userData;
    players.value = response.players || [];

    // Initialize edit form with safe defaults
    editForm.value = {
      nickname: user.value?.nickname || '',
      profileImg: null,
      olympicsName: players.value[0]?.olympics_name || '',
      playerNames: players.value.map(p => p.player_name || '') || []
    };

  } catch (error) {
    console.error('Error fetching profile:', error);
    error.value = 'Failed to load profile data. Please try again.';
  } finally {
    loading.value = false;
  }
};

// Modal handling
const showEditModal = () => {
  const modal = new Modal(editModal.value);
  modal.show();
};

const handleImageChange = (event) => {
  editForm.value.profileImg = event.target.files[0];
};

const handleEditSubmit = async () => {
  try {
    if (!user.value?.userId) {
      throw new Error('User ID not found');
    }

    if (!editForm.value) {
      throw new Error('Form data is missing');
    }

    const formData = new FormData();
    formData.append('nickname', editForm.value.nickname || '');
    if (editForm.value.profileImg) {
      formData.append('profileImg', editForm.value.profileImg);
    }
    formData.append('olympicsName', editForm.value.olympicsName || '');
    if (Array.isArray(editForm.value.playerNames)) {
      editForm.value.playerNames.forEach(name => {
        if (name) formData.append('playerNames', name);
      });
    }

    await userStore.updateUser(user.value.userId, formData);
    const modalInstance = Modal.getInstance(editModal.value);
    if (modalInstance) {
      modalInstance.hide();
    }
    await fetchUserProfile();
  } catch (error) {
    console.error('Failed to update profile:', error);
    alert('Failed to update profile. Please try again.');
  }
};

const confirmDeleteOlympic = async () => {
  if (!olympicStore.userOlympicId) {
    alert('No Olympic event found to delete');
    return;
  }

  if (confirm('Are you sure you want to delete this Olympic event? This action cannot be undone.')) {
    try {
      await olympicStore.deleteOlympicEvent(olympicStore.userOlympicId);
      alert('Olympic deleted successfully');
      router.push('/olympic/create');
    } catch (error) {
      console.error('Failed to delete Olympic:', error);
      alert('Failed to delete Olympic. Please try again.');
    }
  }
};

const confirmDeleteAccount = async () => {
  if (!user.value?.userId) {
    alert('User information not found');
    return;
  }

  if (confirm('WARNING: This will permanently delete your account and all associated data. This action cannot be undone. Are you sure?')) {
    try {
      await userStore.deleteAccount(user.value.userId);
      alert('Account deleted successfully');
      router.push('/auth/login');
    } catch (error) {
      console.error('Failed to delete account:', error);
      alert('Failed to delete account. Please try again.');
    }
  }
};

// Utility functions
const formatName = (name) => name ? capitalize(name) : 'N/A';
const formatNumber = (num) => new Intl.NumberFormat().format(num);

onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
.my-page {
  max-width: 1200px;
  margin: 0 auto;
}

.card {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.card-title {
  color: var(--primary-color);
  margin-bottom: 1.5rem;
}

.table {
  margin-bottom: 0;
}

.modal-dialog {
  max-width: 500px;
}
</style>
