import { defineStore } from 'pinia';
import {
  getChallengeDetails,
  getChallengeLeaderboard,
  submitChallengeScore,
  getChallengeComments,
  addChallengeComment,
  deleteChallengeComment,
} from '@/services/challenge';

export const useChallengeStore = defineStore('challenge', {
  state: () => ({
    challenges: [], // Add this to store all challenges
    challenge: null,
    leaderboard: [],
    comments: [],
    loading: false,
  }),

  actions: {
    // Add this action
    async fetchChallenges() {
      this.loading = true;
      try {
        const response = await getChallengeDetails();
        this.challenges = response.data;
      } catch (error) {
        console.error('Failed to fetch challenges:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // Fetch challenge details
    async fetchChallengeDetails(challengeId) {
      this.loading = true;
      try {
        const response = await getChallengeDetails(challengeId);
        this.challenge = response.data;
        return this.challenge;
      } catch (error) {
        console.error('Failed to fetch challenge details:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // Fetch leaderboard for a challenge
    async fetchLeaderboard(olympicId) {
      this.loading = true;
      try {
        const response = await getChallengeLeaderboard(olympicId);
        this.leaderboard = response.data;
        return this.leaderboard;
      } catch (error) {
        console.error('Failed to fetch leaderboard:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // Submit a score for a challenge
    async submitScore(challengeId, scoreData) {
      try {
        await submitChallengeScore(challengeId, scoreData);
        console.log('Score submitted successfully!');
      } catch (error) {
        console.error('Failed to submit score:', error);
      }
    },

    // Fetch comments for a challenge
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

    // Add a comment to a challenge
    async addComment(challengeId, commentData) {
      try {
        const response = await addChallengeComment(challengeId, commentData);
        return response.data;
      } catch (error) {
        console.error('Failed to add comment:', error);
        throw error;
      }
    },

    // Delete a comment from a challenge
    async deleteComment(challengeId, commentId) {
      try {
        await deleteChallengeComment(challengeId, commentId);
        this.comments = this.comments.filter(comment => comment.id !== commentId);
        console.log('Comment deleted successfully!');
      } catch (error) {
        console.error('Failed to delete comment:', error);
      }
    },

    // Add this new action
    async fetchMainPageData() {
      this.loading = true;
      try {
        const response = await api.get('/');
        if (response.data) {
          this.leaderboard = response.data.leaderBoard || [];
          this.challenges = response.data.challengeList || [];
        }
        return response.data;
      } catch (error) {
        console.error('Failed to fetch main page data:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    }
  },
});
