import { defineStore } from 'pinia';
import { fetchUserProfile, updateUserProfile, deleteUserAccount } from '@/services/user';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    loading: false,
  }),

  actions: {
    // 사용자 정보 가져오기
    async fetchUser(userId) {
      this.loading = true;
      try {
        const response = await fetchUserProfile(userId);
        this.user = response.data.userData;
        console.log('Response from server:', response);
        return response;
      } catch (error) {
        console.error('Failed to fetch user profile:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // 사용자 정보 업데이트
    async updateUser(userId, formData) {
      this.loading = true;
      try {
        const response = await updateUserProfile(userId, formData);
        this.user = {
          ...this.user,
          ...response.data,
          imgSrc: response.data.ImgSrc || response.data.imgSrc
        };
        return response;
      } catch (error) {
        console.error('Failed to update user profile:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // 사용자 계정 삭제
    async deleteUser(userId) {
      if (!userId) {
        console.error('No user ID available to delete.');
        return;
      }

      this.loading = true;
      try {
        await deleteUserAccount(userId);
        this.user = null;
        console.log('User account deleted successfully.');
      } catch (error) {
        console.error('Failed to delete user account:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },
  },
});
