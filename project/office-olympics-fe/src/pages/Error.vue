<template>
  <MainLayout>
    <div class="error-page">
      <h1 class="text-danger text-center">Oops! Something went wrong.</h1>
      <p class="text-center">
        The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.
      </p>
      <div class="text-center mt-4">
        <RouterLink to="/" class="btn btn-primary">Go to Home</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
</script>

<style scoped>
.error-page {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  text-align: center;
}
.error-page h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
}
.error-page p {
  font-size: 1.2rem;
  color: #6c757d;
}
</style>
