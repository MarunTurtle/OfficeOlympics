import { defineStore } from 'pinia';
import { login, register, logout } from '@/services/auth';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null,
  }),
  actions: {
    async loginUser(credentials) {
      try {
        const response = await login(credentials);
        this.user = response.data.user;
        this.token = response.data.token;
      } catch (error) {
        console.error('Login failed:', error);
      }
    },
    async logoutUser() {
      try {
        await logout();
        this.user = null;
        this.token = null;
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
    async registerUser(data) {
      try {
        await register(data);
      } catch (error) {
        console.error('Registration failed:', error);
      }
    },
  },
});
