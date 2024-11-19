<template>
  <div id="app">
    <!-- Display Navbar -->
    <Navbar />
    
    <!-- Main Application Content -->
    <RouterView />
    
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

// Ensure the user's session is loaded when the app starts
onMounted(() => {
  if (!authStore.user) {
    authStore.loadUser(); // Restore session from localStorage
  }
});
</script>

<style scoped>
</style>
