/**
 * @파일명: utils/validation.js
 * @설명: 입력값 유효성 검사를 위한 유틸리티 함수 모음
 */

/**
 * 이메일 주소 유효성 검사
 * @param {string} email - 검사할 이메일 주소
 * @returns {boolean} 유효한 이메일이면 true, 아니면 false
 * @example
 * isValidEmail("user@example.com") // returns true
 * isValidEmail("invalid-email") // returns false
 */
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * 비밀번호 유효성 검사
 * - 최소 8자 이상
 * - (주석 처리된 정규식: 대문자, 소문자, 숫자, 특수문자 포함)
 * @param {string} password - 검사할 비밀번호
 * @returns {boolean} 유효한 비밀번호면 true, 아니면 false
 * @example
 * isValidPassword("password123") // returns true
 * isValidPassword("123") // returns false
 */
export const isValidPassword = (password) => {
  // const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  // return passwordRegex.test(password);
  return password && password.length >= 8;
};

/**
 * 문자열이 비어있지 않은지 검사
 * @param {string} str - 검사할 문자열
 * @returns {boolean} 비어있지 않으면 true, 비어있으면 false
 * @example
 * isNotEmpty("hello") // returns true
 * isNotEmpty("   ") // returns false
 */
export const isNotEmpty = (str) => {
  return str && str.trim().length > 0;
};

/**
 * 양의 정수 여부 검사
 * @param {number} num - 검사할 숫자
 * @returns {boolean} 양의 정수면 true, 아니면 false
 * @example
 * isPositiveInteger(5) // returns true
 * isPositiveInteger(-1) // returns false
 * isPositiveInteger(1.5) // returns false
 */
export const isPositiveInteger = (num) => {
  return Number.isInteger(num) && num > 0;
};
