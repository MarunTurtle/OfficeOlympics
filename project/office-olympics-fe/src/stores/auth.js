import { defineStore } from 'pinia';
import { login, logout, register } from '@/services/auth';
import { useOlympicStore } from '@/stores/olympic';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.user,
  },
  actions: {
    async loginUser(credentials) {
      try {
        const { user, olympicsId } = await login(credentials);
        console.log('Login Response:', { user, olympicsId });
        this.user = {
          ...user,
          id: user.nickname
        };
        console.log('Stored User Data:', this.user);
        localStorage.setItem('user', JSON.stringify(this.user));
        localStorage.setItem('olympicsId', olympicsId || null);

        // Update Olympic store
        const olympicStore = useOlympicStore();
        olympicStore.setUserOlympicId(olympicsId);

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
  },
});
