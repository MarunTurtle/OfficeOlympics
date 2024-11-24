/**
 * @파일명: stores/challenge.js
 * @설명: 챌린지 상태 관리 스토어
 * @관련서비스:
 *   - getChallengeDetails (챌린지 상세 조회)
 *   - getChallengeLeaderboard (리더보드 조회)
 *   - submitChallengeScore (점수 제출)
 */

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

/**
 * 챌린지 스토어 정의
 */
export const useChallengeStore = defineStore('challenge', {
  /**
   * 상태 정의
   * @property {Array} leaderboard - 리더보드 데이터
   * @property {boolean} loading - 로딩 상태
   * @property {Array} challenges - 전체 챌린지 목록
   * @property {Array} comments - 챌린지 댓글
   * @property {Object|null} challenge - 현재 선택된 챌린지
   * @property {Object|null} currentChallenge - 현재 진행 중인 챌린지
   * @property {Array} rankings - 순위 목록
   */
  state: () => ({
    leaderboard: [],
    loading: false,
    challenges: [],
    comments: [],
    challenge: null,
    currentChallenge: null,
    rankings: []
  }),

  actions: {
    /**
     * 전체 챌린지 목록 조회
     * @throws {Error} 조회 실패 시 에러
     */
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

    /**
     * 챌린지 상세 정보 조회
     * @param {number} challengeId - 챌린지 식별자
     * @returns {Promise<Object>} 챌린지 상세 정보
     */
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

    /**
     * 리더보드 조회
     * @param {number} olympicId - 올림픽 식별자
     * @returns {Promise<Array>} 리더보드 데이터
     */
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

    /**
     * 챌린지 점수 제출
     * @param {number} challengeId - 챌린지 식별자
     * @param {Object} scoreData - 제출할 점수 데이터
     * @returns {Promise<Object>} 제출 결과
     */
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

    /**
     * 메인 페이지 데이터 조회
     * - 리더보드와 챌린지 목록 포함
     * @returns {Promise<Object>} 메인 페이지 데이터
     */
    async fetchMainPageData() {
      this.loading = true;
      try {
        const response = await getMainPageData();
        if (response.data) {
          this.leaderboard = response.data.leaderBoard || [];
          this.challenges = response.data.challengeList || [];

          // 챌린지 데이터 변환
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

    /**
     * 챌린지 점수 입력 폼 조회
     * @param {number} challengeId - 챌린지 식별자
     * @returns {Promise<Object>} 점수 입력 폼 데이터
     * @throws {Error} 조회 실패 시 에러
     */
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

    /**
     * 챌린지 순위 조회
     * @param {number} challengeId - 챌린지 식별자
     * @returns {Promise<Array>} 순위 목록
     * @throws {Error} 조회 실패 시 에러
     */
    async fetchChallengeRank(challengeId) {
      try {
        const response = await getChallengeRank(challengeId);
        return response.data;
      } catch (error) {
        console.error('Error fetching challenge rank:', error);
        throw error;
      }
    },

    /**
     * 챌린지 최종 순위 조회
     * @param {number} challengeId - 챌린지 식별자
     * @returns {Promise<Array>} 최종 순위 목록
     * @throws {Error} 조회 실패 시 에러
     */
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
