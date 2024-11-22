import { defineStore } from 'pinia';
import {
  getChallengeComments,
  addChallengeComment,
  updateChallengeComment,
  deleteChallengeComment,
  addCommentReply,
  updateCommentReply
} from '@/services/comment';

export const useCommentStore = defineStore('comment', {
  state: () => ({
    comments: [],
    loading: false,
    error: null
  }),

  actions: {
    setError(message) {
      this.error = message;
      setTimeout(() => {
        this.error = null;
      }, 3000);
    },

    async fetchComments(challengeId) {
      this.loading = true;
      try {
        const response = await getChallengeComments(challengeId);
        this.comments = response.data;
        return this.comments;
      } catch (error) {
        console.error('Failed to fetch comments:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async addComment(challengeId, commentData) {
      try {
        const response = await addChallengeComment(challengeId, commentData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        this.setError(error.response?.data || 'Failed to add comment');
        throw error;
      }
    },

    async updateComment(challengeId, commentId, commentData) {
      try {
        const response = await updateChallengeComment(challengeId, commentId, commentData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        const message = handleApiError(error);
        this.setError(message);
        throw error;
      }
    },

    async deleteComment(challengeId, commentId) {
      try {
        await deleteChallengeComment(challengeId, commentId);
        await this.fetchComments(challengeId);
      } catch (error) {
        this.setError(error.response?.data || 'Failed to delete comment');
        throw error;
      }
    },

    async addReply(challengeId, commentId, replyData) {
      try {
        const response = await addCommentReply(challengeId, commentId, replyData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        this.setError(error.response?.data || 'Failed to add reply');
        throw error;
      }
    },

    async updateReply(challengeId, commentId, replyId, replyData) {
      try {
        const response = await updateCommentReply(challengeId, commentId, replyId, replyData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        this.setError(error.response?.data || 'Failed to update reply');
        throw error;
      }
    }
  }
});

const handleApiError = (error) => {
  if (!error.response) return '네트워크 오류가 발생했습니다.';

  switch (error.response.status) {
    case 400:
      return error.response.data || '잘못된 요청입니다.';
    case 401:
      return '로그인이 필요하거나 권한이 없습니다.';
    case 404:
      return '댓글을 찾을 수 없습니다.';
    default:
      return '서버 오류가 발생했습니다.';
  }
};
