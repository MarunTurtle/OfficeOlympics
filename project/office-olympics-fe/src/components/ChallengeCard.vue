<template>
  <div class="challenge-card">
    <img :src="thumbnailUrl" :alt="title" class="card-img-top">
    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      <RouterLink :to="`/challenges/${id}`" class="btn btn-primary">
        Join Challenge
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  videoUrl: {
    type: String,
    required: true
  }
});

const thumbnailUrl = computed(() => {
  const videoId = props.videoUrl.split('v=')[1];
  return `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;
});
</script>

<style scoped>
.challenge-card {
  width: 300px;
  margin: 15px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.challenge-card:hover {
  transform: translateY(-5px);
}

.card-img-top {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card-body {
  padding: 15px;
  text-align: center;
}

.card-title {
  font-size: 1.2rem;
  margin-bottom: 15px;
}
</style>
