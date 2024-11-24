/**
 * @파일명: stores/auth.js
 * @설명: 사용자 인증 상태 관리 스토어
 * @관련서비스:
 *   - login (로그인 요청)
 *   - logout (로그아웃 요청)
 *   - register (회원가입 요청)
 */

import { defineStore } from 'pinia';
import { login, logout, register } from '@/services/auth';
import { useOlympicStore } from '@/stores/olympic';

/**
 * 인증 스토어 정의
 * @returns {Store} Pinia 스토어 인스턴스
 */
export const useAuthStore = defineStore('auth', {
  /**
   * 상태 정의
   * @property {Object|null} user - 현재 로그인한 사용자 정보
   * @property {string|null} token - 인증 토큰
   */
  state: () => ({
    user: null,
    token: null,
  }),

  /**
   * 게터 정의
   * @property {boolean} isAuthenticated - 사용자 인증 여부
   */
  getters: {
    isAuthenticated: (state) => !!state.user,
  },

  actions: {
    /**
     * 사용자 로그인 처리
     * @param {Object} credentials - 로그인 정보
     * @throws {Error} 로그인 실패 시 에러
     */
    async loginUser(credentials) {
      try {
        const { user, olympicsId } = await login(credentials);
        console.log('Login Response:', { user, olympicsId });

        // 사용자 정보 저장
        this.user = {
          ...user,
          id: user.id
        };
        console.log('Stored User Data:', this.user);

        // 로컬 스토리지에 사용자 정보 저장
        localStorage.setItem('user', JSON.stringify(this.user));
        localStorage.setItem('olympicsId', olympicsId || null);

        // 올림픽 스토어 업데이트
        const olympicStore = useOlympicStore();
        olympicStore.setUserOlympicId(olympicsId);

      } catch (error) {
        console.error('Login failed:', error);
        throw new Error('이메일 또는 비밀번호가 올바르지 않습니다.');
      }
    },

    /**
     * 회원가입 처리
     * @param {FormData} registerData - 회원가입 정보
     * @throws {Error} 회원가입 실패 시 에러
     */
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

    /**
     * 로그아웃 처리
     * - 서버 로그아웃 요청
     * - 로컬 상태 초기화
     * - 로컬 스토리지 클리어
     */
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

    /**
     * 로컬 스토리지에서 사용자 정보 로드
     * - 페이지 새로고침 시 상태 복구에 사용
     */
    loadUser() {
      const user = localStorage.getItem('user');
      if (user) {
        this.user = JSON.parse(user);
      } else {
        this.user = null; // 로컬 스토리지에 사용자 정보가 없으면 초기화
      }
    },
  },
});
