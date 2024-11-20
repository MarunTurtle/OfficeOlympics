<template>
  <MainLayout>
    <div class="challenge-detail">
      <div v-if="loading" class="text-center my-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <div v-else-if="challenge" class="row">
        <!-- Main Content -->
        <div class="col-lg-8">
          <!-- Video Player -->
          <div class="video-container mb-4">
            <div class="video-wrapper">
              <iframe v-if="challenge.videoUrl" :src="challenge.videoUrl" frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen></iframe>
            </div>
          </div>

          <h1 class="challenge-title mb-3">{{ challenge.title }}</h1>

          <div class="action-bar mb-4">
            <button class="btn btn-primary btn-lg" @click="startChallenge">
              Start Challenge
            </button>
          </div>

          <div class="challenge-description mb-4">
            <h3>Challenge Description</h3>
            <p>{{ challenge.description }}</p>
          </div>
        </div>
      </div>

      <div v-else class="text-center my-5">
        <h2>Challenge not found</h2>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import MainLayout from '@/layouts/MainLayout.vue';

const router = useRouter();
const route = useRoute();
const challengeStore = useChallengeStore();

const challenge = ref(null);
const comments = ref([]);
const newComment = ref('');
const loading = ref(true);

const challengeId = parseInt(route.params.id);

const transformYoutubeUrl = (url) => {
  if (!url) return '';
  // Handle both youtu.be and youtube.com URLs
  if (url.includes('youtu.be/')) {
    const videoId = url.split('youtu.be/')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  } else if (url.includes('youtube.com/watch?v=')) {
    const videoId = url.split('v=')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  }
  return url;
};

onMounted(async () => {
  try {
    loading.value = true;
    const response = await challengeStore.fetchChallengeDetails(challengeId);

    challenge.value = {
      id: response.challengeId,
      title: response.challengeName,
      description: response.challengeDesc,
      videoUrl: transformYoutubeUrl(response.challengeUrl)
    };

    await challengeStore.fetchComments(challengeId);
    comments.value = challengeStore.comments;
  } catch (error) {
    console.error('Failed to fetch challenge details:', error);
  } finally {
    loading.value = false;
  }
});

const startChallenge = () => {
  router.push(`/challenges/${challengeId}/score`);
};

const addComment = async () => {
  if (newComment.value.trim()) {
    try {
      await challengeStore.addComment(challengeId, { content: newComment.value });
      newComment.value = '';
      // Refresh comments after adding
      await challengeStore.fetchComments(challengeId);
      comments.value = challengeStore.comments;
    } catch (error) {
      console.error('Failed to add comment:', error);
    }
  }
};
</script>

<style scoped>
.challenge-detail {
  margin-top: 20px;
}

.challenge-header {
  margin-bottom: 20px;
}

.challenge-title {
  font-size: 2rem;
  color: var(--primary-color);
}

.challenge-description {
  font-size: 1.2rem;
  color: #555;
}

.video-player {
  margin-top: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.comments-section {
  margin-top: 30px;
}

.comments-list {
  list-style: none;
  padding: 0;
}

.comment-item {
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.related-challenges {
  margin-top: 20px;
}

.list-group-item {
  border: none;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.video-container {
  width: 100%;
  background: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%;
  /* 16:9 Aspect Ratio */
  height: 0;
  overflow: hidden;
}

.video-wrapper iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 0;
}
</style>
