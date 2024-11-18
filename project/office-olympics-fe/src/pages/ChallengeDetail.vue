<template>
  <MainLayout>
    <div class="challenge-detail">
      <div class="row">
        <!-- Challenge Video and Details -->
        <div class="col-lg-8">
          <div class="challenge-header">
            <h1 class="challenge-title">{{ challenge?.title }}</h1>
            <p class="challenge-description">{{ challenge?.description }}</p>
          </div>

          <!-- Video Player -->
          <div class="video-player">
            <iframe
              v-if="challenge?.videoUrl"
              width="100%"
              height="315"
              :src="challenge.videoUrl"
              title="Challenge Video"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen
            ></iframe>
          </div>

          <!-- Start Challenge Button -->
          <div class="mt-4">
            <button class="btn btn-primary" @click="startChallenge">
              Start Challenge
            </button>
          </div>

          <!-- Comments Section -->
          <div class="comments-section mt-5">
            <h3>Comments</h3>
            <form @submit.prevent="addComment">
              <textarea
                v-model="newComment"
                class="form-control mb-3"
                placeholder="Add a comment..."
                rows="3"
              ></textarea>
              <button class="btn btn-secondary" type="submit">Post Comment</button>
            </form>

            <ul class="comments-list mt-4">
              <li
                v-for="(comment, index) in comments"
                :key="index"
                class="comment-item"
              >
                <p><strong>{{ comment.user }}</strong>: {{ comment.text }}</p>
              </li>
            </ul>
          </div>
        </div>

        <!-- Related Challenges Sidebar -->
        <div class="col-lg-4">
          <div class="related-challenges">
            <h3>Related Challenges</h3>
            <ul class="list-group">
              <li
                v-for="(relatedChallenge, index) in relatedChallenges"
                :key="index"
                class="list-group-item"
              >
                <p class="mb-1"><strong>{{ relatedChallenge.title }}</strong></p>
                <p class="small">{{ relatedChallenge.description }}</p>
                <button
                  class="btn btn-link p-0"
                  @click="viewChallenge(relatedChallenge.id)"
                >
                  View Details
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useChallengeStore } from '@/stores/challenge';
import { useRoute } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';

// Challenge Store
const challengeStore = useChallengeStore();

// State
const challenge = ref(null);
const comments = ref([]);
const relatedChallenges = ref([]);
const newComment = ref('');

const route = useRoute();
const challengeId = ref(parseInt(route.params.id)); // Extract `id` from route params

// Fetch Data on Mount
onMounted(() => {
  const mockChallenges = [
    { id: 1, title: 'Challenge 1', description: 'Run 1km', videoUrl: 'https://www.youtube.com/embed/example' },
    { id: 2, title: 'Challenge 2', description: 'Do 20 push-ups', videoUrl: 'https://www.youtube.com/embed/example2' },
  ];
  challenge.value = mockChallenges.find(c => c.id === challengeId.value);
});

// Methods
const startChallenge = () => {
  alert('Challenge started!');
  // Redirect to Challenge Score Input Page
  // For example: router.push(`/challenges/${challenge.value.id}/score`);
};

const addComment = async () => {
  if (newComment.value.trim()) {
    await challengeStore.addComment(challenge.id, { text: newComment.value });
    comments.value = challengeStore.comments;
    newComment.value = '';
  }
};

const viewChallenge = (id) => {
  router.push(`/challenges/${id}`);
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
</style>
