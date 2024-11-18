import api from '@/services/api';

/**
 * Create a new Olympic event
 * @param {object} olympicData - Data for the Olympic event
 * @returns {Promise}
 */
export const createOlympic = (olympicData) => {
  return api.post('/olympics/team', olympicData);
};

/**
 * Get details of an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const getOlympicDetails = (olympicId) => {
  return api.get(`/olympics/team/${olympicId}`);
};

/**
 * Delete an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/team/${olympicId}`);
};

/**
 * Fetch all players in an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const getOlympicPlayers = (olympicId) => {
  return api.get(`/olympics/team/${olympicId}/players`);
};
