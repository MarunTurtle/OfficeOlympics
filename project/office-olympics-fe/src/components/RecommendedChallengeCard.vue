<template>
  <div class="recommended-challenge-card">
    <div class="thumbnail-wrapper">
      <img :src="thumbnailUrl" :alt="title" class="card-img">
    </div>
    <div class="card-body">
      <h6 class="card-title">{{ title }}</h6>
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
  margin-bottom: 0.75rem;
  border-radius: 4px;
  overflow: hidden;
  background: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
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
  padding: 0.5rem;
}

.card-title {
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.2;
  height: 2.2em;
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
}
</style>
