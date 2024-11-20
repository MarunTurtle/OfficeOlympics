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
  return api.post(`/challenges/${challengeId}/record`, scoreData);
};

/**
 * Fetch comments for a specific challenge
 * @param {number} challengeId
 * @returns {Promise}
 */
export const getChallengeComments = (challengeId) => {
  return api.get(`/challenges/${challengeId}/comments`);
};

/**
 * Add a new comment for a challenge
 * @param {number} challengeId
 * @param {object} commentData
 * @returns {Promise}
 */
export const addChallengeComment = (challengeId, commentData) => {
  return api.post(`/challenges/${challengeId}/comments`, commentData);
};

/**
 * Delete a comment for a challenge
 * @param {number} challengeId
 * @param {number} commentId
 * @returns {Promise}
 */
export const deleteChallengeComment = (challengeId, commentId) => {
  return api.delete(`/challenges/${challengeId}/comments/${commentId}`);
};

/**
 * Fetch main page data
 * @returns {Promise}
 */
export const getMainPageData = () => {
  return api.get('/');
};
