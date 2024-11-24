/**
 * @파일명: stores/olympic.js
 * @설명: 올림픽 이벤트 상태 관리 스토어
 * @관련서비스:
 *   - createOlympic (올림픽 생성)
 *   - getOlympicDetails (올림픽 상세 조회)
 *   - deleteOlympic (올림픽 삭제)
 *   - getOlympicPlayers (참가자 목록 조회)
 */

import { defineStore } from 'pinia';
import {
  createOlympic,
  getOlympicDetails,
  deleteOlympic,
  getOlympicPlayers,
} from '@/services/olympic';

/**
 * 올림픽 스토어 정의
 */
export const useOlympicStore = defineStore('olympic', {
  /**
   * 상태 정의
   * @property {Object|null} olympic - 현재 올림픽 이벤트 상세 정보
   * @property {Array} players - 올림픽 이벤트 참가자 목록
   * @property {string|null} userOlympicId - 사용자가 참여 중인 올림픽 ID
   * @property {boolean} loading - API 호출을 위한 로딩 상태
   */
  state: () => ({
    olympic: null,
    players: [],
    userOlympicId: localStorage.getItem('olympicsId') || null,
    loading: false,
  }),

  actions: {
    /**
     * 사용자 올림픽 ID 설정
     * @param {string|null} olympicId - 올림픽 식별자
     */
    setUserOlympicId(olympicId) {
      this.userOlympicId = olympicId;
      if (olympicId) {
        localStorage.setItem('olympicsId', olympicId);
      } else {
        localStorage.removeItem('olympicsId');
      }
    },

    /**
     * 올림픽 데이터 초기화 (로그아웃 시 호출)
     */
    clearOlympicData() {
      this.olympic = null;
      this.players = [];
      this.userOlympicId = null;
      localStorage.removeItem('olympicsId');
    },

    /**
     * 새로운 올림픽 이벤트 생성
     * @param {Object} olympicData - 올림픽 생성 데이터
     * @returns {Promise<Object>} 생성된 올림픽 정보
     * @throws {Error} 생성 실패 시 에러 (3회 재시도 후)
     */
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

    /**
     * 올림픽 이벤트 상세 정보 조회
     * @param {string} olympicId - 올림픽 식별자
     * @throws {Error} 조회 실패 시 에러
     */
    async fetchOlympicDetails(olympicId) {
      this.loading = true;
      try {
        const response = await getOlympicDetails(olympicId);
        this.olympic = response.data;
        console.log('Fetched Olympic details:', this.olympic);
      } catch (error) {
        console.error('Failed to fetch Olympic details:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    /**
     * 올림픽 이벤트 삭제
     * @param {string} olympicId - 올림픽 식별자
     * @throws {Error} 삭제 실패 시 에러
     */
    async deleteOlympicEvent(olympicId) {
      this.loading = true;
      try {
        await deleteOlympic(olympicId);
        this.olympic = null;
        this.userOlympicId = null;
        console.log('Olympic deleted successfully.');
      } catch (error) {
        console.error('Failed to delete Olympic event:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    /**
     * 올림픽 이벤트 참가자 목록 조회
     * @param {string} olympicId - 올림픽 식별자
     * @returns {Promise<Array>} 참가자 목록
     * @throws {Error} 조회 실패 시 에러
     */
    async fetchOlympicPlayers(olympicId) {
      this.loading = true;
      try {
        const response = await getOlympicPlayers(olympicId);
        this.players = response.data.players;
        console.log('Fetched players:', this.players);
        return this.players;
      } catch (error) {
        console.error('Failed to fetch Olympic players:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },
  },
});
