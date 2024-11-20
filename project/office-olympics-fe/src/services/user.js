import api from '@/services/api';

/**
 * Fetch the current user's profile
 * @returns {Promise}
 */
export const fetchUserProfile = (userId) => {
  return api.get(`/accounts/${userId}`);
};

/**
 * Update user profile information
 * @param {number} userId - User ID
 * @param {object} updatedData - Updated user data
 * @returns {Promise}
 */
export const updateUserProfile = (userId, updatedData) => {
  return api.put(`/accounts/${userId}`, updatedData);
};

/**
 * Delete user account
 * @param {number} userId - User ID
 * @returns {Promise}
 */
export const deleteUserAccount = (userId) => {
  return api.delete(`/accounts/${userId}`);
};

/**
 * Fetch all users (admin functionality, optional)
 * @returns {Promise}
 */
export const fetchAllUsers = () => {
  return api.get('/accounts');
};
