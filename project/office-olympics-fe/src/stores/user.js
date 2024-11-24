/**
 * @파일명: stores/user.js
 * @설명: 사용자 프로필 상태 관리 스토어
 * @관련서비스:
 *   - fetchUserProfile (프로필 조회)
 *   - updateUserProfile (프로필 수정)
 *   - deleteUserAccount (계정 삭제)
 */

import { defineStore } from 'pinia';
import { fetchUserProfile, updateUserProfile, deleteUserAccount } from '@/services/user';

/**
 * 사용자 스토어 정의
 */
export const useUserStore = defineStore('user', {
  /**
   * 상태 정의
   * @property {Object|null} user - 사용자 프로필 정보
   * @property {boolean} loading - API 호출을 위한 로딩 상태
   */
  state: () => ({
    user: null,
    loading: false,
  }),

  actions: {
    /**
     * 사용자 프로필 정보 조회
     * @param {number} userId - 사용자 식별자
     * @returns {Promise<Object>} 사용자 프로필 정보
     * @throws {Error} 조회 실패 시 에러
     */
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

    /**
     * 사용자 프로필 정보 수정
     * @param {number} userId - 사용자 식별자
     * @param {FormData} formData - 수정할 프로필 데이터
     * @returns {Promise<Object>} 수정된 사용자 정보
     * @throws {Error} 수정 실패 시 에러
     */
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

    /**
     * 사용자 계정 삭제
     * @param {number} userId - 사용자 식별자
     * @throws {Error} 삭제 실패 시 에러
     */
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
