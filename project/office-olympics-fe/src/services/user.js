/**
 * @파일명: services/user.js
 * @설명: 사용자 계정 관련 API 요청 처리 서비스
 * @관련백엔드:
 *   - GET /accounts/{userId} (사용자 프로필 조회)
 *   - PUT /accounts/{userId} (프로필 수정)
 *   - DELETE /accounts/{userId} (계정 삭제)
 *   - GET /accounts (전체 사용자 목록 조회)
 */

import api from '@/services/api';

/**
 * 현재 사용자의 프로필 정보 조회
 * @param {number} userId - 사용자 식별자
 * @returns {Promise<Object>} 사용자 프로필 정보
 * @property {string} name - 사용자 이름
 * @property {string} email - 이메일
 * @property {string} nickname - 닉네임
 * @throws {Error} userId가 제공되지 않은 경우
 */
export const fetchUserProfile = async (userId) => {
  if (!userId) {
    throw new Error('User ID is required');
  }
  return api.get(`/accounts/${userId}`);
};

/**
 * 사용자 프로필 정보 수정
 * @param {number} userId - 사용자 식별자
 * @param {FormData} formData - 수정할 프로필 데이터
 * @property {string} [name] - 이름
 * @property {string} [nickname] - 닉네임
 * @property {File} [profileImage] - 프로필 이미지
 * @returns {Promise<Object>} 수정된 사용자 정보
 */
export const updateUserProfile = (userId, formData) => {
  return api.put(`/accounts/${userId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};

/**
 * 사용자 계정 삭제
 * @param {number} userId - 사용자 식별자
 * @returns {Promise<void>} 삭제 결과
 */
export const deleteUserAccount = (userId) => {
  return api.delete(`/accounts/${userId}`);
};

/**
 * 전체 사용자 목록 조회 (관리자 기능)
 * @returns {Promise<Array>} 사용자 목록
 * @property {number} id - 사용자 ID
 * @property {string} name - 이름
 * @property {string} email - 이메일
 * @property {string} role - 권한
 */
export const fetchAllUsers = () => {
  return api.get('/accounts');
};
