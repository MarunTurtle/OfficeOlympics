import api from './api';

// Function for logging in a user
export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);
  console.log('Raw API Response:', response.data);

  return {
    user: {
      nickname: response.data.loginUserId,
    },
    olympicsId: response.data.olympicsId || null,
    token: null,
  };
};

// For register, we only need to specify Content-Type since it differs from the global setting
export const register = async (data) => {
  return api.post('/auth/register', data, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export const logout = async () => {
  return api.post('/auth/logout');
};
