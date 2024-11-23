<template>
  <div class="challenge-card" @click="navigateToChallenge">
    <div class="thumbnail-wrapper">
      <img
        :src="thumbnailUrl"
        :alt="title"
        class="card-img-top"
        @error="handleImageError"
      >
    </div>

    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      <p class="card-description">{{ description }}</p>

      <RouterLink
        :to="challengeRoute"
        class="btn card-button mt-3"
        @click.stop
      >
        도전하기
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { getYoutubeThumbnail } from '@/utils/youtube';

// Props 정의
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  title: {
    type: String,
    required: true,
    validator: value => value.length > 0
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

const router = useRouter();
const fallbackImage = ref('/images/thumbnail-fallback.jpg');

// 컴퓨티드 속성
const thumbnailUrl = computed(() => getYoutubeThumbnail(props.videoUrl));
const challengeRoute = computed(() => `/challenges/${props.id}`);

// 메서드
const navigateToChallenge = () => {
  router.push(challengeRoute.value);
};

const handleImageError = (e) => {
  e.target.src = fallbackImage.value;
};
</script>

<style scoped>
.challenge-card {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  cursor: pointer;
}

.challenge-card:hover {
  transform: translateY(-5px);
}

.thumbnail-wrapper {
  position: relative;
  padding-top: 56.25%; /* 16:9 비율 */
  overflow: hidden;
}

.card-img-top {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-body {
  padding: 1.25rem;
}

.card-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.1 rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 2.4em;
}

.card-description {
  font-size: 1rem;
  color: #666;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 4.5em;
}

.card-button {
  width: 100%;
  font-weight: 600;
  color: white;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.card-button:hover {
  background-color: var(--interaction-hover-color);
}
</style>
