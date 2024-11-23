<template>
  <div class="recommended-challenge-card">
    <div class="thumbnail-wrapper">
      <img :src="thumbnailUrl" :alt="title" class="card-img">
    </div>
    <div class="card-body">
      <h4 class="card-title">{{ title }}</h4>
      <RouterLink :to="`/challenges/${id}`" class="stretched-link">
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
  const videoId = props.videoUrl.split('/').pop();
  return `https://img.youtube.com/vi/${videoId}/mqdefault.jpg`;
});
</script>

<style scoped>
.recommended-challenge-card {
  position: relative;
  margin-bottom: 1rem;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.thumbnail-wrapper {
  position: relative;
  width: 100%;
  padding-top: 56.25%;
  /* 16:9 Aspect Ratio */
  overflow: hidden;
}

.card-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-body {
  padding: 0.75rem 1rem;
}

.card-title {
  margin: 0;
  font-size: 1rem;
  line-height: 1.4;
  height: 2.0em;
  font-weight: 500;
  color: #2c3e50;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.stretched-link::after {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1;
  content: "";
}

.recommended-challenge-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}
</style>
