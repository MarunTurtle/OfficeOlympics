<template>
  <div class="container">
    <h2 class="mt-4">Create New Olympic</h2>
    <form @submit.prevent="handleCreateOlympic">
      <div class="mb-3">
        <label class="form-label">Olympic Name</label>
        <input v-model="olympicName" type="text" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Number of Participants</label>
        <input v-model="participantCount" type="number" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary">Create</button>
    </form>
  </div>
</template>

<script>
import { createOlympic } from '@/api/olympics';

export default {
  name: 'OlympicCreate',
  data() {
    return {
      olympicName: '',
      participantCount: '',
    };
  },
  methods: {
    async handleCreateOlympic() {
      try {
        const data = {
          name: this.olympicName,
          participants: this.participantCount,
        };
        const response = await createOlympic(data);
        console.log('Olympic created successfully:', response.data);
        this.$router.push('/challenges'); // Navigate to challenges
      } catch (error) {
        console.error('Error creating Olympic:', error);
        alert('Failed to create Olympic. Please try again.');
      }
    },
  },
};
</script>

<style scoped>
/* Styles specific to OlympicCreate.vue */
</style>
