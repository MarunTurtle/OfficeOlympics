/**
 * @파일명: services/comment.js
 * @설명: 챌린지 댓글 관련 API 요청 처리 서비스
 * @관련백엔드:
 *   - GET /challenges/{id}/comments (댓글 목록)
 *   - POST /challenges/{id}/comments (댓글 작성)
 *   - PUT /challenges/{id}/comments/{commentId} (댓글 수정)
 *   - DELETE /challenges/{id}/comments/{commentId} (댓글 삭제)
 */

import api from '@/services/api';

/**
 * 챌린지의 댓글 목록 조회
 * @param {number} challengeId - 챌린지 식별자
 * @returns {Promise<Array>} 댓글 목록
 * @property {number} id - 댓글 ID
 * @property {string} content - 댓글 내용
 * @property {string} authorName - 작성자 이름
 * @property {Date} createdAt - 작성일시
 */
export const getChallengeComments = (challengeId) => {
  return api.get(`/challenges/${challengeId}/comments`);
};

/**
 * 챌린지에 새 댓글 작성
 * @param {number} challengeId - 챌린지 식별자
 * @param {Object} commentData - 댓글 데이터
 * @property {string} content - 댓글 내용
 * @returns {Promise<Object>} 생성된 댓글 정보
 */
export const addChallengeComment = (challengeId, commentData) => {
  return api.post(`/challenges/${challengeId}/comments`, commentData);
};

/**
 * 기존 댓글 수정
 * @param {number} challengeId - 챌린지 식별자
 * @param {number} commentId - 댓글 식별자
 * @param {Object} commentData - 수정할 댓글 데이터
 * @property {string} content - 수정할 댓글 내용
 * @returns {Promise<Object>} 수정된 댓글 정보
 */
export const updateChallengeComment = (challengeId, commentId, commentData) => {
  return api.put(`/challenges/${challengeId}/comments/${commentId}`, commentData);
};

/**
 * 댓글 삭제
 * @param {number} challengeId - 챌린지 식별자
 * @param {number} commentId - 댓글 식별자
 * @returns {Promise<void>} 삭제 결과
 */
export const deleteChallengeComment = (challengeId, commentId) => {
  return api.delete(`/challenges/${challengeId}/comments/${commentId}`);
};

/**
 * 댓글에 답글 작성
 * @param {number} challengeId - 챌린지 식별자
 * @param {number} commentId - 댓글 식별자
 * @param {Object} replyData - 답글 데이터
 * @property {string} content - 답글 내용
 * @returns {Promise<Object>} 생성된 답글 정보
 */
export const addCommentReply = (challengeId, commentId, replyData) => {
  return api.post(`/challenges/${challengeId}/comments/${commentId}/replies`, replyData);
};

/**
 * 답글 수정
 * @param {number} challengeId - 챌린지 식별자
 * @param {number} commentId - 댓글 식별자
 * @param {number} replyId - 답글 식별자
 * @param {Object} replyData - 수정할 답글 데이터
 * @property {string} content - 수정할 답글 내용
 * @returns {Promise<Object>} 수정된 답글 정보
 */
export const updateCommentReply = (challengeId, commentId, replyId, replyData) => {
  return api.put(`/challenges/${challengeId}/comments/${commentId}/replies/${replyId}`, replyData);
};
