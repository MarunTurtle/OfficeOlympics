import { defineStore } from 'pinia';
import { fetchUserProfile, updateUserProfile, deleteUserAccount } from '@/services/user';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    loading: false,
  }),

  actions: {
    async fetchUser(userId) {
      this.loading = true;
      try {
        const response = await fetchUserProfile(userId);
        this.user = response.userData;
        console.log('Response from server:', response);
        return response;
      } catch (error) {
        console.error('Failed to fetch user profile:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async updateUser(userId, formData) {
      this.loading = true;
      try {
        const response = await updateUserProfile(userId, formData);
        this.user = response.data;
        return response;
      } catch (error) {
        console.error('Failed to update user profile:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async deleteUser() {
      if (!this.user?.id) {
        console.error('No user ID available to delete.');
        return;
      }

      this.loading = true;
      try {
        await deleteUserAccount(this.user.id);
        this.user = null;
        console.log('User account deleted successfully.');
        // Consider redirecting the user here
      } catch (error) {
        console.error('Failed to delete user account:', error);
        // Consider adding user feedback here
      } finally {
        this.loading = false;
      }
    },
  },
});
