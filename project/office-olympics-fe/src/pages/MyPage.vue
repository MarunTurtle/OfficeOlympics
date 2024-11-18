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
          <button class="btn btn-primary mt-3" @click="editProfile">Edit Profile</button>
        </div>
      </div>

      <!-- Logout Button -->
      <div class="logout-button text-center mt-5">
        <button class="btn btn-danger" @click="logout">Log Out</button>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useAuthStore } from '@/stores/auth';
import { capitalize } from '@/utils/formatter'; // Import formatter utilities

// Store and State
const userStore = useUserStore();
const authStore = useAuthStore();
const user = ref(null); // Current user data

// Fetch User Profile
const fetchUserProfile = async () => {
  try {
    await userStore.fetchUser(); // Fetch user data from store
    user.value = userStore.user; // Set user data
  } catch (error) {
    console.error('Error fetching profile:', error);
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
    // Redirect to login page
    window.location.href = '/login';
  } catch (error) {
    console.error('Logout failed:', error);
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
