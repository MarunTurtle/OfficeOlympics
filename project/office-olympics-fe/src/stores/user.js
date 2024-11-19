import { defineStore } from 'pinia';
import { fetchUserProfile, updateUserProfile, deleteUserAccount } from '@/services/user';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null, // Current user data
    loading: false, // Loading state for API interactions
  }),

  actions: {
    async fetchUser() {
      this.loading = true;
      if (!this.user) {
        try {
          const response = await fetchUserProfile();
          this.user = response.data; // Adjust based on API response structure
          console.log('User profile fetched:', this.user);
        } catch (error) {
          console.error('Failed to fetch user profile:', error);
        } finally {
          this.loading = false;
        }
      }
    },

    /**
     * Update the current user's profile
     * @param {object} updatedData - Updated user data
     */
    async updateUser(updatedData) {
      if (!this.user?.id) {
        console.error('No user ID available to update.');
        return;
      }

      this.loading = true;
      try {
        const response = await updateUserProfile(this.user.id, updatedData);
        this.user = response.data; // Update the user state with new data
        console.log('User profile updated:', this.user);
      } catch (error) {
        console.error('Failed to update user profile:', error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * Delete the current user's account
     */
    async deleteUser() {
      if (!this.user?.id) {
        console.error('No user ID available to delete.');
        return;
      }

      this.loading = true;
      try {
        await deleteUserAccount(this.user.id);
        this.user = null; // Clear user state
        console.log('User account deleted successfully.');
        // Redirect to login or home page if needed
      } catch (error) {
        console.error('Failed to delete user account:', error);
      } finally {
        this.loading = false;
      }
    },
  },
});
