<template>
  <div class="container">
    <h2 class="mt-4">My Page</h2>
    <!-- Profile information -->
    <form @submit.prevent="updateProfile" class="mt-4">
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input v-model="user.email" type="email" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">Nickname</label>
        <input v-model="user.nickname" type="text" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary">Update Profile</button>
    </form>
    <!-- Liked challenges -->
    <h3 class="mt-5">Liked Challenges</h3>
    <ul class="list-group mt-3">
      <li class="list-group-item" v-for="challenge in likedChallenges" :key="challenge.id">
        {{ challenge.name }}
      </li>
    </ul>
  </div>
</template>

<script>
import { getUserProfile, updateUserProfile } from '@/api/user';

export default {
  name: 'MyPage',
  data() {
    return {
      user: {
        email: '',
        nickname: '',
      },
      likedChallenges: [], // Mock data for liked challenges
    };
  },
  async created() {
    try {
      const response = await getUserProfile(1); // Mock user ID
      this.user = response.data;
      this.likedChallenges = [
        { id: 1, name: 'Wall Sit Challenge' },
        { id: 2, name: 'Speed Typing Challenge' },
      ]; // Add mock liked challenges for display
    } catch (error) {
      console.error(error);
      alert('Failed to load user profile');
    }
  },
  methods: {
    async updateProfile() {
      try {
        const response = await updateUserProfile(1, { nickname: this.user.nickname }); // Mock user ID
        console.log('Profile updated:', response.data);
        alert('Profile updated successfully');
      } catch (error) {
        console.error(error);
        alert('Failed to update profile');
      }
    },
  },
};
</script>

<style scoped>
/* Styles specific to MyPage.vue */
</style>
