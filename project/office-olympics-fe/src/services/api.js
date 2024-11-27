/**
 * @파일명: services/api.js
 * @설명: API 통신을 위한 Axios 인스턴스 설정
 */

import axios from 'axios';

/**
 * Axios 인스턴스 생성 및 설정
 * @constant {AxiosInstance}
 *
 * 설정 내용:
 * - baseURL: 환경변수에서 API 기본 URL 설정 (기본값: localhost:8080)
 * - headers: JSON 컨텐츠 타입 설정
 * - withCredentials: 인증 쿠키 전송 허용
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // 쿠키 포함 설정
});

/**
 * 이미지 URL 생성 함수
 * @param {string} path - 이미지 경로
 * @returns {string} 완성된 이미지 URL
 *
 * @example
 * const imageUrl = getImageUrl('profiles/user1.jpg');
 * // => 'https://api.example.com/images/profiles/user1.jpg'
 */
export const getImageUrl = (path) =>
  `${import.meta.env.VITE_IMAGE_BASE_URL}/${path}`;

export default api;
