import { defineStore } from 'pinia';
import { login, logout, register } from '@/services/auth';
import { useOlympicStore } from '@/stores/olympic';
import axios from 'axios';

axios.defaults.withCredentials = true;

export const useAuthStore = defineStore('auth', {
  state: () => ({
    userId: localStorage.getItem('userId') || null,
    user: JSON.parse(localStorage.getItem('user')) || null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.user,
  },
  actions: {
    async loginUser(credentials) {
      try {
        const { data } = await login(credentials);
        console.log('Login Response:', data);

        // Set both user and userId with the correct property names
        this.user = {
          ...data,
          id: data.loginUserId,
          nickname: data.nickname
        };
        this.userId = data.loginUserId;

        console.log('Stored User Data:', this.user);
        localStorage.setItem('user', JSON.stringify(this.user));
        localStorage.setItem('userId', data.loginUserId);

        // Update Olympic store if needed
        if (data.olympicsId) {
          const olympicStore = useOlympicStore();
          olympicStore.setUserOlympicId(data.olympicsId);
        }

      } catch (error) {
        console.error('Login failed:', error);
        throw new Error('Invalid email or password.');
      }
    },

    async registerUser(registerData) {
      try {
        const response = await register(registerData);
        console.log('Registration successful:', response.data);

        // Optional: Log the user in immediately after registration
        // this.user = {
        //   nickname: registerData.get('nickname'), // Assuming FormData is passed
        // };
        // localStorage.setItem('user', JSON.stringify(this.user));
        alert('Registration successful! Please log in.');
      } catch (error) {
        console.error('Registration failed:', error);
        throw new Error('An error occurred during registration.');
      }
    },
    async logoutUser() {
      try {
        await logout();
        this.user = null;
        this.token = null;
        localStorage.removeItem('user');
        localStorage.removeItem('olympicsId');
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },

    loadUser() {
      const user = localStorage.getItem('user');
      if (user) {
        this.user = JSON.parse(user);
      } else {
        this.user = null; // Clear user if not found in localStorage
      }
    },
    setUser(userData) {
      this.user = userData;
      this.userId = userData.loginUserId;
      localStorage.setItem('userId', userData.loginUserId);
      localStorage.setItem('user', JSON.stringify(userData));
    },
    clearUser() {
      this.user = null;
      this.userId = null;
      localStorage.removeItem('userId');
      localStorage.removeItem('user');
    }
  },
});
