// YouTube 관련 유틸리티 함수
export const getYoutubeThumbnail = (url) => {
  if (!url) return '';
  const videoId = url.split('/').pop();
  return `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;
}; 
