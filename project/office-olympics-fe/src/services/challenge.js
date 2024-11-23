import api from '@/services/api';

/**
 * ID로 챌린지 상세 정보 조회
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeDetails = (challengeId) => {
  return api.get(`/challenges/${challengeId}`);
};

/**
 * 특정 챌린지의 리더보드 조회
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeLeaderboard = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * 챌린지에 유저 점수 제출
 * @param {number} challengeId
 * @param {object} scoreData
 * @returns {Promise}
 */
export const submitChallengeScore = (challengeId, scoreData) => {
  return api.post(`/challenges/${challengeId}/score`, scoreData);
};

/**
 * 메인 페이지 데이터 조회
 * @returns {Promise}
 */
export const getMainPageData = () => {
  return api.get('/');
};

/**
 * 특정 챌린지의 점수 입력 폼 조회
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeScoreForm = (challengeId) => {
  return api.get(`/challenges/${challengeId}/score`);
};

/**
 * 특정 챌린지의 순위 조회
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * 특정 챌린지의 최종 순위 조회
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getFinalRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/final-rank`);
};

/**
 * 특정 유저의 올림픽 삭제
 * @param {number} olympicId
 * @returns {Promise}
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/${olympicId}`);
};
