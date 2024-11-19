<template>
  <MainLayout>
    <div class="challenge-detail">
      <div class="row">
        <!-- Main Content -->
        <div class="col-lg-8">
          <!-- Video Player -->
          <div class="video-container mb-4">
            <div class="video-wrapper">
              <iframe v-if="challenge?.videoUrl" :src="challenge.videoUrl" frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen></iframe>
            </div>
          </div>

          <h1 class="challenge-title mb-3">{{ challenge?.title }}</h1>

          <div class="action-bar mb-4">
            <button class="btn btn-primary btn-lg" @click="startChallenge">
              Start Challenge
            </button>
          </div>

          <div class="challenge-description mb-4">
            <h3>Challenge Description</h3>
            <p>{{ challenge?.description }}</p>
          </div>

          <!-- Comments Section -->
          <div class="comments-section">
            <h3>Comments</h3>
            <div class="comment-form mb-4">
              <textarea v-model="newComment" class="form-control mb-2" placeholder="Add a comment..."
                rows="3"></textarea>
              <button class="btn btn-primary" @click="addComment" :disabled="!newComment.trim()">
                Post Comment
              </button>
            </div>

            <div class="comments-list">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <div class="comment-header">
                  <strong>{{ comment.user }}</strong>
                </div>
                <p>{{ comment.content }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="col-lg-4">
          <h3>Other Challenges</h3>
          <div class="other-challenges">
            <div v-for="otherChallenge in otherChallenges" :key="otherChallenge.challenge_id"
              class="challenge-card mb-3" @click="navigateToChallenge(otherChallenge.challenge_id)">
              <img :src="getThumbnail(otherChallenge.challenge_url)" :alt="otherChallenge.challenge_name"
                class="challenge-thumbnail">
              <div class="challenge-info p-2">
                <h5>{{ otherChallenge.challenge_name }}</h5>
              </div>
            </div>
          </div>
        </div>
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

onMounted(async () => {
  try {
    await challengeStore.fetchChallengeDetails(challengeId);
    await challengeStore.fetchComments(challengeId);

    challenge.value = {
      id: challengeStore.challenge.challenge_id,
      title: challengeStore.challenge.challenge_name,
      description: challengeStore.challenge.challenge_desc,
      videoUrl: challengeStore.challenge.challenge_url.replace('youtu.be/', 'youtube.com/embed/')
    };

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
