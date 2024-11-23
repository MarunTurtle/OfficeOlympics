<template>
  <MainLayout>
    <div class="error-page">
      <h1 class="text-danger text-center">앗! 문제가 발생했습니다.</h1>
      <p class="text-center">
        찾으시는 페이지가 삭제되었거나, 일시적으로 사용할 수 없습니다.
      </p>
      <div class="text-center mt-4">
        <RouterLink to="/" class="btn btn-primary">홈으로 이동</RouterLink>
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
