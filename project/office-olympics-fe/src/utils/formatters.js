/**
 * @파일명: utils/formatters.js
 * @설명: 데이터 포맷팅을 위한 유틸리티 함수 모음
 */

/**
 * 날짜를 읽기 쉬운 문자열로 변환
 * @param {string | Date} date - 변환할 날짜
 * @returns {string} 포맷된 날짜 문자열
 */
export const formatDate = (date) => {
  if (!date) return '';

  const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  };

  return new Date(date).toLocaleDateString('ko-KR', options);
};

/**
 * 문자열의 첫 글자를 대문자로 변환
 * @param {string} str - 변환할 문자열
 * @returns {string} 첫 글자가 대문자로 변환된 문자열
 * @example
 * capitalize("hello") // returns "Hello"
 * capitalize("") // returns ""
 */
export const capitalize = (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1);
};

/**
 * 숫자에 천 단위 구분 쉼표 추가
 * @param {number} num - 포맷할 숫자
 * @returns {string} 쉼표가 포함된 숫자 문자열
 * @example
 * formatNumber(1000) // returns "1,000"
 * formatNumber(1000000) // returns "1,000,000"
 */
export const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
