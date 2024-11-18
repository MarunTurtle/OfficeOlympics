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
    challenge: null, // Stores details of a specific challenge
    leaderboard: [], // Stores leaderboard data
    comments: [], // Stores comments for the challenge
    loading: false, // Loading state for API calls
  }),

  actions: {
    // Fetch challenge details
    async fetchChallengeDetails(challengeId) {
      this.loading = true;
      try {
        const response = await getChallengeDetails(challengeId);
        this.challenge = response.data;
      } catch (error) {
        console.error('Failed to fetch challenge details:', error);
      } finally {
        this.loading = false;
      }
    },

    // Fetch leaderboard for a challenge
    async fetchChallengeLeaderboard(challengeId) {
      this.loading = true;
      try {
        const response = await getChallengeLeaderboard(challengeId);
        this.leaderboard = response.data.players; // Adjust based on API response structure
      } catch (error) {
        console.error('Failed to fetch leaderboard:', error);
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
        this.comments = response.data.comments; // Adjust based on API response structure
      } catch (error) {
        console.error('Failed to fetch comments:', error);
      } finally {
        this.loading = false;
      }
    },

    // Add a comment to a challenge
    async addComment(challengeId, commentData) {
      try {
        const response = await addChallengeComment(challengeId, commentData);
        this.comments.push(response.data); // Add new comment to the state
        console.log('Comment added successfully!');
      } catch (error) {
        console.error('Failed to add comment:', error);
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
  },
});
