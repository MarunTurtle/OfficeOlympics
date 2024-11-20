<template>
  <MainLayout>
    <div class="my-page">
      <!-- Header -->
      <div class="profile-header text-center">
        <h1>My Profile</h1>
        <p class="text-muted">Manage your profile and account settings.</p>
      </div>

      <!-- Profile Information -->
      <div class="profile-info card">
        <div class="card-body">
          <h2>Profile Details</h2>
          <p><strong>Name:</strong> {{ formatName(user?.name) }}</p>
          <p><strong>Email:</strong> {{ user?.email }}</p>
          <p><strong>Nickname:</strong> {{ formatName(user?.nickname) }}</p>
          <button class="btn btn-primary mt-3 me-2" @click="editProfile">Edit Profile</button>

          <!-- Add Delete Olympic button if user has an Olympic -->
          <button v-if="hasOlympics" class="btn btn-danger mt-3" @click="deleteOlympic">
            Delete Olympic
          </button>
        </div>
      </div>

      <!-- Logout Button -->
      <!-- <div class="logout-button text-center mt-5">
        <button class="btn btn-danger" @click="logout">Log Out</button>
      </div> -->
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useAuthStore } from '@/stores/auth';
import { capitalize } from '@/utils/formatters'; // Import formatter utilities
import { useOlympicStore } from '@/stores/olympic';
import { useRouter, useRoute } from 'vue-router';

// Store and State
const userStore = useUserStore();
const authStore = useAuthStore();
const user = ref(null); // Current user data
const olympicStore = useOlympicStore();
const router = useRouter();
const route = useRoute();

// Add computed property for Olympic status
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

// Fetch User Profile
const fetchUserProfile = async () => {
  try {
    const userId = route.params.userId;
    if (!userId) {
      throw new Error('No user ID available');
    }
    const response = await userStore.fetchUser(userId);
    console.log('User profile response:', response);
    user.value = response.userData;
  } catch (error) {
    console.error('Error fetching profile:', error);
    router.push('/auth/login');
  }
};

// Edit Profile
const editProfile = () => {
  // Navigate to a profile editing page (e.g., /my-profile/edit)
  console.log('Edit Profile clicked');
  alert('Profile editing functionality coming soon!');
};

// Logout
const logout = async () => {
  try {
    await authStore.logoutUser();
    alert('You have been logged out.');
    router.push('/auth/login');
  } catch (error) {
    console.error('Logout failed:', error);
  }
};

// Add delete Olympic handler
const deleteOlympic = async () => {
  if (!confirm('Are you sure you want to delete this Olympic event? This action cannot be undone.')) {
    return;
  }

  try {
    await olympicStore.deleteOlympicEvent(olympicStore.userOlympicId);
    alert('Olympic event deleted successfully.');
    router.push('/olympic/create');
  } catch (error) {
    console.error('Failed to delete Olympic:', error);
    alert('Failed to delete Olympic event.');
  }
};

// Utility Methods
const formatName = (name) => (name ? capitalize(name) : 'N/A');

// Fetch Data on Mount
onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
.my-page {
  margin-top: 20px;
}

.profile-header h1 {
  font-size: 2.5rem;
  color: var(--primary-color);
}

.profile-info {
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.logout-button button {
  padding: 10px 20px;
  font-size: 1.1rem;
}
</style>
