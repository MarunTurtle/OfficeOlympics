<template>
  <div class="challenge-card">
    <img :src="thumbnailUrl" :alt="title" class="card-img-top">
    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      <p class="card-description">{{ description }}</p>
      <RouterLink :to="`/challenges/${id}`" class="btn card-button mt-3">
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
  description: {
    type: String,
    required: true
  },
  videoUrl: {
    type: String,
    required: true
  }
});

const thumbnailUrl = computed(() => {
  const videoId = props.videoUrl.split('/').pop();
  return `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;
});
</script>

<style scoped>
.challenge-card {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  background: white;
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
  padding: 1.25rem;
  text-align: center;
}

.card-title {
  font-size: 1.2rem;
  margin-bottom: 0.75rem;
  height: 2.4rem;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-description {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 1rem;
  height: 4.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-button {
  font-size: 1rem;
  font-weight: 600;
  padding: 8px 16px;
  margin: 0 5px;
  color: #fff;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.card-button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}
</style>
