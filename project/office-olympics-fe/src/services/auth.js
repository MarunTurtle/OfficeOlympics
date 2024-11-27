/**
 * @파일명: services/auth.js
 * @설명: 인증 관련 API 요청 처리 서비스
 * @관련백엔드:
 *   - POST /auth/login (로그인)
 *   - POST /auth/register (회원가입)
 *   - POST /auth/logout (로그아웃)
 */

import api from './api';

/**
 * 사용자 로그인 처리
 * @param {Object} credentials - 로그인 정보
 * @param {string} credentials.email - 사용자 이메일
 * @param {string} credentials.password - 사용자 비밀번호
 * @returns {Promise<Object>} 로그인 응답 데이터
 * @property {Object} user - 사용자 정보
 * @property {number} user.id - 사용자 ID
 * @property {string} user.nickname - 사용자 닉네임
 * @property {number|null} olympicsId - 참여 중인 올림픽 ID
 * @property {string|null} token - 인증 토큰
 */
export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);
  console.log('Raw API Response:', response.data);

  return {
    user: {
      id: response.data.loginUserId,
      nickname: response.data.nickname
    },
    olympicsId: response.data.olympicsId || null,
    token: null,
  };
};

/**
 * 회원가입 처리
 * @param {FormData} data - 회원가입 폼 데이터
 * @param {string} data.email - 이메일
 * @param {string} data.password - 비밀번호
 * @param {string} data.name - 이름
 * @param {string} data.nickname - 닉네임
 * @param {File} [data.profileImg] - 프로필 이미지 (선택)
 * @returns {Promise<Object>} 회원가입 응답 데이터
 */
export const register = async (data) => {
  return api.post('/auth/register', data, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

/**
 * 로그아웃 처리
 * @returns {Promise<void>} 로그아웃 요청 결과
 */
export const logout = async () => {
  return api.post('/auth/logout');
};
