import { defineStore } from 'pinia';
import {
  createOlympic,
  getOlympicDetails,
  deleteOlympic,
  getOlympicPlayers,
} from '@/services/olympic';

export const useOlympicStore = defineStore('olympic', {
  state: () => ({
    olympic: null, // Current Olympic event details
    players: [], // List of players in the Olympic event
    userOlympicId: null, // Olympic ID for the logged-in user
    loading: false, // Loading state for API calls
  }),

  actions: {

    // Set user Olympic ID
    setUserOlympicId(olympicId) {
      this.userOlympicId = olympicId;
    },

    // Clear Olympic data (called upon logout)
    clearOlympicData() {
      this.olympic = null;
      this.players = [];
      this.userOlympicId = null;
    },    

    // Create a new Olympic event
    async createOlympicEvent(olympicData) {
      this.loading = true;
      let retries = 3;
      
      while (retries > 0) {
        try {
          const response = await createOlympic(olympicData);
          this.olympic = response.data;
          this.userOlympicId = response.data.olympicsId;
          localStorage.setItem('olympicsId', response.data.olympicsId);
          return response.data;
        } catch (error) {
          retries--;
          if (retries === 0) {
            console.error('Failed to create Olympic event:', error);
            throw error;
          }
          await new Promise(resolve => setTimeout(resolve, 1000));
        }
      }
    },

    // Fetch Olympic event details
    async fetchOlympicDetails(olympicId) {
      this.loading = true;
      try {
        const response = await getOlympicDetails(olympicId);
        this.olympic = response.data;
        console.log('Fetched Olympic details:', this.olympic);
      } catch (error) {
        console.error('Failed to fetch Olympic details:', error);
        throw error; // Rethrow error for error handling in the caller=
      } finally {
        this.loading = false;
      }
    },

    // Delete an Olympic event
    async deleteOlympicEvent(olympicId) {
      this.loading = true;
      try {
        await deleteOlympic(olympicId);
        this.olympic = null;
        this.userOlympicId = null; // Clear the user Olympic ID
        console.log('Olympic deleted successfully.');
      } catch (error) {
        console.error('Failed to delete Olympic event:', error);
        throw error; // Rethrow error for error handling in the caller
      } finally {
        this.loading = false;
      }
    },

    // Fetch players in an Olympic event
    async fetchOlympicPlayers(olympicId) {
      this.loading = true;
      try {
        const response = await getOlympicPlayers(olympicId);
        this.players = response.data.players; // Adjust based on API response
        console.log('Fetched players:', this.players);
        return this.players; // Return the players list
      } catch (error) {
        console.error('Failed to fetch Olympic players:', error);
        throw error; // Rethrow error for error handling in the caller
      } finally {
        this.loading = false;
      }
    },
  },
});
