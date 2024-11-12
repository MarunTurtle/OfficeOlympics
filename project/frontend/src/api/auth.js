// src/api/auth.js
// import axios from './axios';

// export function login(email, password) {
//   return axios.post('/auth/login', { email, password });
// }

// export function register(email, password) {
//   return axios.post('/auth/register', { email, password });
// }

export const login = async (username, password) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (username === 'test' && password === 'password') {
        resolve({ data: { token: 'fake-jwt-token', user: { id: 1, username: 'test' } } });
      } else {
        reject(new Error('Invalid username or password'));
      }
    }, 500); // Simulate network delay
  });
};

export const logout = async () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ data: { success: true } });
    }, 500); // Simulate network delay
  });
};

export const register = async (userDetails) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ data: { success: true, user: { id: 2, ...userDetails } } });
    }, 500); // Simulate network delay
  });
};
