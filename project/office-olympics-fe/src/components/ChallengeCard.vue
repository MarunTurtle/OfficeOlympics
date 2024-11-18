<template>
  <div class="card" style="width: 18rem;">
    <img :src="image" class="card-img-top" alt="Challenge Image" />
    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      <p class="card-text">{{ description }}</p>
      <button @click="onDetails" class="btn btn-primary">Details</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

// Props are destructured at the top
defineProps({
  id: {
    type: Number,
    required: true,
  },
  image: String,
  title: String,
  description: String,
});

// Router instance
const router = useRouter();

// Destructure `id` from props
const onDetails = () => {
  router.push({ name: 'ChallengeDetail', params: { id } });
};
</script>