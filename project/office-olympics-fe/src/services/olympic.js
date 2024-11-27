/**
 * @파일명: services/olympic.js
 * @설명: 올림픽 이벤트 관련 API 요청 처리 서비스
 * @관련백엔드:
 *   - POST /olympics (올림픽 생성)
 *   - GET /olympics/{id} (올림픽 상세 조회)
 *   - DELETE /olympics/{id} (올림픽 삭제)
 *   - GET /olympics/{id}/players (참가자 목록 조회)
 */

import api from '@/services/api';

/**
 * 새로운 올림픽 이벤트 생성
 * @param {object} olympicData - 올림픽 이벤트 데이터
 * @property {string} name - 올림픽 이름
 * @property {Array} players - 참가자 목록
 * @returns {Promise<Object>} 생성된 올림픽 정보
 * @throws {Error} 서버 응답 형식이 잘못되었거나 요청이 실패한 경우
 */
export const createOlympic = async (olympicData) => {
  try {
    const response = await api.post('/olympics', olympicData);

    // 응답 구조 유효성 검사
    if (!response.data || !response.data.olympicsId) {
      throw new Error('Invalid response format from server');
    }

    return response;
  } catch (error) {
    if (error.response?.status === 400) {
      throw new Error('Invalid Olympic data provided');
    }
    throw error;
  }
};

/**
 * 올림픽 이벤트 상세 정보 조회
 * @param {number} olympicId - 올림픽 식별자
 * @returns {Promise<Object>} 올림픽 상세 정보
 * @property {string} name - 올림픽 이름
 * @property {string} status - 올림픽 상태
 * @property {Date} startDate - 시작일
 */
export const getOlympicDetails = (olympicId) => {
  return api.get(`/olympics/${olympicId}`);
};

/**
 * 올림픽 이벤트 삭제
 * @param {number} olympicId - 올림픽 식별자
 * @returns {Promise<void>} 삭제 결과
 */
export const deleteOlympic = (olympicId) => {
  return api.delete(`/olympics/${olympicId}`);
};

/**
 * 올림픽 참가자 목록 조회
 * @param {number} olympicId - 올림픽 식별자
 * @returns {Promise<Array>} 참가자 목록
 * @property {number} id - 참가자 ID
 * @property {string} nickname - 참가자 닉네임
 * @property {number} score - 현재 점수
 */
export const getOlympicPlayers = (olympicId) => {
  return api.get(`/olympics/${olympicId}/players`);
};
