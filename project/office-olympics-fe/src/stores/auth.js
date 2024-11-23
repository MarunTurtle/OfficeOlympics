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
          id: user.id
        };
        console.log('Stored User Data:', this.user);
        localStorage.setItem('user', JSON.stringify(this.user));
        localStorage.setItem('olympicsId', olympicsId || null);

        // Update Olympic store
        const olympicStore = useOlympicStore();
        olympicStore.setUserOlympicId(olympicsId);

      } catch (error) {
        console.error('Login failed:', error);
        throw new Error('이메일 또는 비밀번호가 올바르지 않습니다.');
      }
    },

    async registerUser(registerData) {
      try {
        const response = await register(registerData);
        console.log('Registration successful:', response.data);
        alert('회원가입이 완료되었습니다! 로그인해 주세요.');
      } catch (error) {
        console.error('Registration failed:', error);
        throw new Error('회원가입 중 오류가 발생했습니다.');
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
