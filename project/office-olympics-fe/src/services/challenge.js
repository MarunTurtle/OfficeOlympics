/**
 * @파일명: services/challenge.js
 * @설명: 챌린지 관련 API 요청 처리 서비스
 * @관련백엔드:
 *   - GET /challenges/{id} (챌린지 상세)
 *   - GET /challenges/{id}/rank (리더보드)
 *   - POST /challenges/{id}/score (점수 제출)
 */

import api from '@/services/api';

/**
 * ID로 챌린지 상세 정보 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Object>} 챌린지 상세 정보
 * @property {string} title - 챌린지 제목
 * @property {string} description - 챌린지 설명
 * @property {string} status - 챌린지 상태
 */
export const getChallengeDetails = (challengeId) => {
  return api.get(`/challenges/${challengeId}`);
};

/**
 * 특정 챌린지의 리더보드 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Array>} 순위 목록
 * @property {string} playerName - 참가자 이름
 * @property {number} score - 획득 점수
 */
export const getChallengeLeaderboard = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * 챌린지에 유저 점수 제출
 * @param {number} challengeId - 챌린지 식별자
 * @param {object} scoreData - 제출할 점수 데이터
 * @property {number} score - 획득 점수
 * @property {string} [evidence] - 증빙 자료
 * @returns {Promise<Object>} 제출 결과
 */
export const submitChallengeScore = (challengeId, scoreData) => {
  return api.post(`/challenges/${challengeId}/score`, scoreData);
};

/**
 * 메인 페이지 데이터 조회
 * @returns {Promise<Object>} 메인 페이지 데이터
 * @property {Array} challenges - 추천 챌린지 목록
 * @property {Array} leaderboard - 전체 순위
 */
export const getMainPageData = () => {
  return api.get('/');
};

/**
 * 특정 챌린지의 점수 입력 폼 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Object>} 점수 입력 폼 데이터
 * @property {Object} formFields - 입력 필드 정보
 */
export const getChallengeScoreForm = (challengeId) => {
  return api.get(`/challenges/${challengeId}/score`);
};

/**
 * 특정 챌린지의 순위 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Array>} 순위 목록
 */
export const getChallengeRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * 특정 챌린지의 최종 순위 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Array>} 최종 순위 목록
 */
export const getFinalRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/final-rank`);
};

/**
 * 특정 유저의 올림픽 삭제
 * @param {number} olympicId - 올림픽 식별자
 * @returns {Promise<void>} 삭제 결과
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/${olympicId}`);
};
