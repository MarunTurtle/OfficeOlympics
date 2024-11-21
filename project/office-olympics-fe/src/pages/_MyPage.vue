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

      <!-- Raw Data Display -->
      <div v-else>
        <h2>User Data</h2>
        <pre>{{ JSON.stringify(userData, null, 2) }}</pre>

        <h2 class="mt-4">Players Data</h2>
        <pre>{{ JSON.stringify(playersData, null, 2) }}</pre>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

const userData = ref(null);
const playersData = ref([]);
const loading = ref(true);
const error = ref(null);

const fetchUserProfile = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await userStore.fetchUser(1); // Hardcoded ID for testing
    console.log('Raw response:', response);

    // Store the entire response data
    userData.value = response.userData;
    playersData.value = response.players;

  } catch (err) {
    console.error('Error fetching profile:', err);
    error.value = err.message || 'Failed to load profile data';
  } finally {
    loading.value = false;
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
  padding: 20px;
}

pre {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 5px;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
