import api from './api';

// Function for logging in a user
export const login = async (credentials) => {
  return api.post('/auth/login', credentials);
};

// Function for registering a new user
export const register = async (data) => {
  return api.post('/auth/register', data);
};

// Function for logging out a user
export const logout = async () => {
  return api.post('/auth/logout');
};
