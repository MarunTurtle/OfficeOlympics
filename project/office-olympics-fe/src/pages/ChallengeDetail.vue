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

          <!-- Comments Section -->
          <div class="comments-section">
            <h3>Comments</h3>

            <!-- Add Comment Form -->
            <div class="comment-form mb-4">
              <div class="input-group">
                <input type="text" class="form-control" v-model="newComment" placeholder="Add a comment...">
                <button class="btn btn-primary" @click="addComment" :disabled="!newComment.trim()">
                  Post
                </button>
              </div>
            </div>

            <!-- Comments List -->
            <div class="comments-list">
              <div v-if="!comments.length" class="text-center text-muted">
                No comments yet. Be the first to comment!
              </div>

              <div v-else v-for="comment in comments" :key="comment.id" class="comment-item">
                <div class="d-flex justify-content-between align-items-start">
                  <div>
                    <strong class="comment-author">{{ comment.userName }}</strong>
                    <p class="comment-content mb-1">{{ comment.content }}</p>
                    <small class="text-muted">{{ new Date(comment.createdAt).toLocaleDateString() }}</small>
                  </div>
                  <button v-if="comment.userId === currentUserId" class="btn btn-sm btn-danger"
                    @click="deleteComment(comment.id)">
                    Delete
                  </button>
                </div>
              </div>
            </div>
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
      const commentData = {
        challengeId: challengeId,
        commentText: newComment.value.trim()
      };

      await challengeStore.addComment(challengeId, commentData);
      newComment.value = '';
      // Refresh comments after adding
      await challengeStore.fetchComments(challengeId);
      comments.value = challengeStore.comments;
    } catch (error) {
      console.error('Failed to add comment:', error);
    }
  }
};

const deleteComment = async (commentId) => {
  try {
    await challengeStore.deleteComment(challengeId, commentId);
    comments.value = comments.value.filter(c => c.id !== commentId);
  } catch (error) {
    console.error('Failed to delete comment:', error);
  }
};

// Add currentUserId ref (near the top with other refs)
const currentUserId = ref(null); // You'll need to get this from your auth store
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

.comment-form {
  margin-bottom: 2rem;
}

.comment-item {
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.comment-author {
  color: var(--primary-color);
  margin-right: 0.5rem;
}

.comment-content {
  margin: 0.5rem 0;
}

.comments-list {
  max-height: 500px;
  overflow-y: auto;
}
</style>
