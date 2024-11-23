import { defineStore } from 'pinia';
import {
  createOlympic,
  getOlympicDetails,
  deleteOlympic,
  getOlympicPlayers,
} from '@/services/olympic';

export const useOlympicStore = defineStore('olympic', {
  state: () => ({
    olympic: null, // 현재 올림픽 이벤트 상세 정보
    players: [], // 올림픽 이벤트 참가자 목록
    userOlympicId: localStorage.getItem('olympicsId') || null, // localStorage에서 초기화
    loading: false, // API 호출을 위한 로딩 상태
  }),

  actions: {
    // 사용자 올림픽 ID 설정
    setUserOlympicId(olympicId) {
      this.userOlympicId = olympicId;
      if (olympicId) {
        localStorage.setItem('olympicsId', olympicId);
      } else {
        localStorage.removeItem('olympicsId');
      }
    },

    // 올림픽 데이터 초기화 (로그아웃 시 호출)
    clearOlympicData() {
      this.olympic = null;
      this.players = [];
      this.userOlympicId = null;
      localStorage.removeItem('olympicsId');
    },

    // 새로운 올림픽 이벤트 생성
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

    // 올림픽 이벤트 상세 정보 가져오기
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

    // 올림픽 이벤트 삭제
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

    // 올림픽 이벤트 참가자 가져오기
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
