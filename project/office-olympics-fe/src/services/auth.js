import api from './api';

// Function for logging in a user
export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);

  return {
    user: {
      nickname: response.data.loginUserId, // Adjust based on backend response
    },
    olympicsId: response.data.olympicsId || null, // Store olympicsId if available
    token: null, // Optional, for future token handling
  };
};

export const register = async (data) => {
  return api.post('/auth/regist', data, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};


// Function for logging out a user
export const logout = async () => {
  return api.post('/auth/logout');
};
