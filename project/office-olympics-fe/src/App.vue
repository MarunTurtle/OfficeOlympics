<template>
  <div class="app-wrapper">
    <div class="content-wrapper">
      <!-- Display Navbar -->
      <Navbar />

      <!-- Main Application Content -->
      <RouterView />
    </div>

    <!-- Display Footer -->
    <Footer />
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

onMounted(() => {
  if (!authStore.user) {
    authStore.loadUser();
  }
});
</script>

<style>
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1 0 auto;
}
</style>
