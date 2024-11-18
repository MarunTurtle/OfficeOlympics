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
    loading: false, // Loading state for API calls
  }),

  actions: {
    // Create a new Olympic event
    async createOlympicEvent(olympicData) {
      this.loading = true;
      try {
        const response = await createOlympic(olympicData);
        this.olympic = response.data;
        console.log('Olympic created successfully:', this.olympic);
      } catch (error) {
        console.error('Failed to create Olympic event:', error);
      } finally {
        this.loading = false;
      }
    },

    // Fetch Olympic event details
    async fetchOlympicDetails(olympicId) {
      this.loading = true;
      try {
        const response = await getOlympicDetails(olympicId);
        this.olympic = response.data;
      } catch (error) {
        console.error('Failed to fetch Olympic details:', error);
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
        console.log('Olympic deleted successfully.');
      } catch (error) {
        console.error('Failed to delete Olympic event:', error);
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
      } catch (error) {
        console.error('Failed to fetch Olympic players:', error);
      } finally {
        this.loading = false;
      }
    },
  },
});
