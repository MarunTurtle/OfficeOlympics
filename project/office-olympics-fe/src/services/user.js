import api from '@/services/api';

/**
 * Fetch the current user's profile
 * @returns {Promise}
 */
export const fetchUserProfile = () => {
  return api.get('/accounts/:user_id'); // Replace `:user_id` dynamically in store or component
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
