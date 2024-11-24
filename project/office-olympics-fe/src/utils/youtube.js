/**
 * @파일명: utils/youtube.js
 * @설명: YouTube 동영상 관련 유틸리티 함수 모음
 */

/**
 * YouTube 동영상 URL에서 썸네일 이미지 URL 추출
 * @param {string} url - YouTube 동영상 URL
 * @returns {string} 고화질 썸네일 이미지 URL
 */
export const getYoutubeThumbnail = (url) => {
  if (!url) return '';
  const videoId = url.split('/').pop();
  return `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;
};
