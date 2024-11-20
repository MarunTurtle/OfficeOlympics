import api from '@/services/api';

/**
 * Fetch the current user's profile
 * @returns {Promise}
 */
export const fetchUserProfile = async (userId) => {
  if (!userId) {
    throw new Error('User ID is required');
  }

  const response = await fetch(`http://localhost:8080/accounts/${userId}`);
  return response.json();
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
