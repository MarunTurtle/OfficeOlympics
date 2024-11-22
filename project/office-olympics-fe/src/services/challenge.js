import api from '@/services/api';

/**
 * Fetch challenge details by ID
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeDetails = (challengeId) => {
  return api.get(`/challenges/${challengeId}`);
};

/**
 * Fetch leaderboard for a specific challenge
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeLeaderboard = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * Submit user score for a challenge
 * @param {number} challengeId
 * @param {object} scoreData
 * @returns {Promise}
 */
export const submitChallengeScore = (challengeId, scoreData) => {
  return api.post(`/challenges/${challengeId}/score`, scoreData);
};

/**
 * Fetch main page data
 * @returns {Promise}
 */
export const getMainPageData = () => {
  return api.get('/');
};

/**
 * Fetch challenge score form for a specific challenge
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeScoreForm = (challengeId) => {
  return api.get(`/challenges/${challengeId}/score`);
};

/**
 * Fetch rank for a specific challenge
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/rank`);
};

/**
 * Fetch final rank for a specific challenge
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getFinalRank = (challengeId) => {
  return api.get(`/challenges/${challengeId}/final-rank`);
};

/**
 * Delete an Olympic for a specific user
 * @param {number} olympicId
 * @returns {Promise}
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/${olympicId}`);
};
