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
      <div v-else-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <!-- Content -->
      <div v-else class="row">
        <!-- User Profile Section -->
        <div class="col-md-4">
          <div class="card">
            <div class="card-body text-center">
              <img
                :src="userData?.profileImg || '/images/default_profile.png'"
                class="rounded-circle mb-3 profile-image"
                alt="Profile Image"
              >
              <h3 class="card-title">{{ userData?.nickname }}</h3>
              <p class="text-muted">{{ userData?.email }}</p>
              <p class="text-muted">{{ userData?.name }}</p>
            </div>
          </div>
        </div>

        <!-- Players Section -->
        <div class="col-md-8">
          <div class="card">
            <div class="card-header">
              <h4 class="mb-0">{{ players[0]?.olympics_name || 'Olympic Team Members' }}</h4>
            </div>
            <div class="card-body">
              <div v-if="players.length === 0" class="text-center text-muted">
                No team members found
              </div>
              <div v-else class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Score</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="player in players" :key="player.player_name">
                      <td>{{ player.player_name }}</td>
                      <td>{{ player.total_score }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="row mt-4">
        <div class="col-12 text-center">
          <button class="btn btn-primary me-2" @click="editProfile">
            Edit Profile
          </button>
          <button class="btn btn-danger" @click="confirmDelete">
            Delete Account
          </button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();

const userData = ref(null);
const players = ref([]);
const loading = ref(true);
const error = ref(null);

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

    userData.value = response.data.userData;
    players.value = response.data.players;

  } catch (err) {
    console.error('Error fetching profile:', err);
    error.value = err.message || 'Failed to load profile data';
  } finally {
    loading.value = false;
  }
};

const editProfile = () => {
  router.push('/profile/edit');
};

const confirmDelete = async () => {
  if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
    try {
      await userStore.deleteUser();
      await authStore.logout();
      router.push('/login');
    } catch (err) {
      error.value = 'Failed to delete account. Please try again.';
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

.profile-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
}

.card {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
  border: none;
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.table {
  margin-bottom: 0;
}

.badge {
  font-weight: 500;
}
</style>
