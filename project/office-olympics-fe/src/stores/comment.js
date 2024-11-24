/**
 * @파일명: stores/comment.js
 * @설명: 챌린지 댓글 상태 관리 스토어
 * @관련서비스:
 *   - getChallengeComments (댓글 목록 조회)
 *   - addChallengeComment (댓글 작성)
 *   - updateChallengeComment (댓글 수정)
 *   - deleteChallengeComment (댓글 삭제)
 */

import { defineStore } from 'pinia';
import {
  getChallengeComments,
  addChallengeComment,
  updateChallengeComment,
  deleteChallengeComment,
  addCommentReply,
  updateCommentReply
} from '@/services/comment';
import { useAuthStore } from '@/stores/auth';

/**
 * 댓글 스토어 정의
 */
export const useCommentStore = defineStore('comment', {
  /**
   * 상태 정의
   * @property {Array} comments - 댓글 목록
   * @property {boolean} loading - 로딩 상태
   * @property {string|null} error - 에러 메시지
   */
  state: () => ({
    comments: [],
    loading: false,
    error: null
  }),

  actions: {
    /**
     * 에러 메시지 설정 및 자동 제거
     * @param {string} message - 에러 메시지
     */
    setError(message) {
      this.error = message;
      setTimeout(() => {
        this.error = null;
      }, 3000);
    },

    /**
     * 챌린지의 댓글 목록 조회
     * @param {number} challengeId - 챌린지 식별자
     * @throws {Error} 조회 실패 시 에러
     */
    async fetchComments(challengeId) {
      try {
        this.loading = true;
        const response = await getChallengeComments(challengeId);
        this.comments = response.data.map(comment => ({
          ...comment,
          commentId: comment.commentId,
          commentGroup: comment.commentGroup,
          commentDepth: comment.commentDepth,
          commentText: comment.commentText,
          regDate: comment.regDate,
          updateDate: comment.updateDate,
          nickname: comment.nickname,
          userId: comment.userId,
          imgSrc: comment.imgSrc
        }));
        console.log('Mapped comments:', this.comments);
      } catch (error) {
        this.setError(error.response?.data || '댓글을 불러오는데 실패했습니다');
        throw error;
      } finally {
        this.loading = false;
      }
    },

    /**
     * 새 댓글 작성
     * @param {number} challengeId - 챌린지 식별자
     * @param {Object} commentData - 댓글 데이터
     * @returns {Promise<Object>} 생성된 댓글 정보
     */
    async addComment(challengeId, commentData) {
      try {
        const response = await addChallengeComment(challengeId, commentData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        this.setError(error.response?.data || '댓글 추가에 실패했습니다');
        throw error;
      }
    },

    /**
     * 기존 댓글 수정
     * @param {number} challengeId - 챌린지 식별자
     * @param {number} commentId - 댓글 식별자
     * @param {Object} commentData - 수정할 댓글 데이터
     * @returns {Promise<Object>} 수정된 댓글 정보
     */
    async updateComment(challengeId, commentId, commentData) {
      try {
        const response = await updateChallengeComment(challengeId, commentId, commentData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        const message = handleApiError(error);
        this.setError(message);
        throw error;
      }
    },

    /**
     * 댓글 삭제
     * @param {number} challengeId - 챌린지 식별자
     * @param {number} commentId - 댓글 식별자
     * @returns {Promise<Object>} 삭제 결과
     */
    async deleteComment(challengeId, commentId) {
      try {
        const response = await deleteChallengeComment(challengeId, commentId);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        const message = handleApiError(error);
        this.setError(message);
        throw error;
      }
    },

    /**
     * 댓글에 답글 작성
     * @param {number} challengeId - 챌린지 식별자
     * @param {number} commentId - 댓글 식별자
     * @param {Object} replyData - 답글 데이터
     * @returns {Promise<Object>} 생성된 답글 정보
     */
    async addReply(challengeId, commentId, replyData) {
      try {
        const response = await addCommentReply(challengeId, commentId, replyData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        const message = handleApiError(error);
        this.setError(message);
        throw error;
      }
    },

    /**
     * 답글 수정
     * @param {number} challengeId - 챌린지 식별자
     * @param {number} commentId - 댓글 식별자
     * @param {number} replyId - 답글 식별자
     * @param {Object} replyData - 수정할 답글 데이터
     * @returns {Promise<Object>} 수정된 답글 정보
     */
    async updateReply(challengeId, commentId, replyId, replyData) {
      try {
        const response = await updateCommentReply(challengeId, commentId, replyId, replyData);
        await this.fetchComments(challengeId);
        return response.data;
      } catch (error) {
        const message = handleApiError(error);
        this.setError(message);
        throw error;
      }
    }
  }
});

/**
 * API 에러 처리 유틸리티 함수
 * @param {Error} error - API 에러 객체
 * @returns {string} 사용자 친화적인 에러 메시지
 */
const handleApiError = (error) => {
  if (!error.response) return '네트워크 오류가 발생했습니다.';

  switch (error.response.status) {
    case 400:
      return error.response.data || '잘못된 요청입니다.';
    case 401:
      if (error.response.data === '세션이 만료되었습니다. 다시 로그인해주세요.') {
        const authStore = useAuthStore();
        authStore.logoutUser();
        return '세션이 만료되었습니다. 다시 로그인해주세요.';
      }
      return error.response.data || '권한이 없습니다.';
    case 403:
      return error.response.data || '접근이 거부되었습니다.';
    default:
      return '알 수 없는 오류가 발생했습니다.';
  }
};
