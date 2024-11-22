import { defineStore } from 'pinia';
import {
  getChallengeDetails,
  getChallengeLeaderboard,
  submitChallengeScore,
  getMainPageData,
  getChallengeScoreForm,
  getChallengeRank,
  getFinalRank,
} from '@/services/challenge';

export const useChallengeStore = defineStore('challenge', {
  state: () => ({
    leaderboard: [],
    loading: false,
    challenges: [], // Add this to store all challenges
    comments: [],
    challenge: null,
    currentChallenge: null,
    rankings: []
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
        return response.data;
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
        console.log('Store: submitting score data:', scoreData);
        const response = await submitChallengeScore(challengeId, scoreData);
        console.log('Store: score submission response:', response.data);
        return response.data;
      } catch (error) {
        console.error('Store: failed to submit score:', error.response?.data || error);
        throw error;
      }
    },

    async fetchMainPageData() {
      this.loading = true;
      try {
        const response = await getMainPageData();
        if (response.data) {
          this.leaderboard = response.data.leaderBoard || [];
          this.challenges = response.data.challengeList || [];

          // Transform challenges if needed
          this.challenges = this.challenges.map(challenge => ({
            ...challenge,
            challenge_id: challenge.challengeId,
            challenge_name: challenge.challengeName,
            challenge_desc: challenge.challengeDesc,
            challenge_url: challenge.challengeUrl
          }));
        }
        return response.data;
      } catch (error) {
        console.error('Failed to fetch main page data:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async fetchChallengeScoreForm(challengeId) {
      this.loading = true;
      try {
        const response = await getChallengeScoreForm(challengeId);
        return response.data;
      } catch (error) {
        console.error('Failed to fetch score form:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async fetchChallengeRank(challengeId) {
      try {
        const response = await getChallengeRank(challengeId);
        return response.data;
      } catch (error) {
        console.error('Error fetching challenge rank:', error);
        throw error;
      }
    },

    async fetchFinalRank(challengeId) {
      this.loading = true;
      try {
        const response = await getFinalRank(challengeId);
        return response.data;
      } catch (error) {
        console.error('Failed to fetch final rank:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    }
  },
});
