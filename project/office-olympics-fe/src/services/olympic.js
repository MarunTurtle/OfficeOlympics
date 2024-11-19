import api from '@/services/api';

/**
 * Create a new Olympic event
 * @param {object} olympicData - Data for the Olympic event
 * @returns {Promise}
 */
export const createOlympic = (olympicData) => {
  return api.post('/olympics', olympicData);
};

/**
 * Get details of an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const getOlympicDetails = (olympicId) => {
  return api.get(`/olympics/${olympicId}`);
};

/**
 * Delete an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/${olympicId}`);
};

/**
 * Fetch all players in an Olympic event
 * @param {number} olympicId - ID of the Olympic event
 * @returns {Promise}
 */
export const getOlympicPlayers = (olympicId) => {
  return api.get(`/olympics/${olympicId}/players`);
};
