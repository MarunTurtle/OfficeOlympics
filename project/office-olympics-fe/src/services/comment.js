import api from '@/services/api';

export const getChallengeComments = (challengeId) => {
  return api.get(`/challenges/${challengeId}/comments`);
};

export const addChallengeComment = (challengeId, commentData) => {
  return api.post(`/challenges/${challengeId}/comments`, commentData);
};

export const updateChallengeComment = (challengeId, commentId, commentData) => {
  return api.put(`/challenges/${challengeId}/comments/${commentId}`, commentData);
};

export const deleteChallengeComment = (challengeId, commentId) => {
  return api.delete(`/challenges/${challengeId}/comments/${commentId}`);
};

export const addCommentReply = (challengeId, commentId, replyData) => {
  return api.post(`/challenges/${challengeId}/comments/${commentId}/replies`, replyData);
};

export const updateCommentReply = (challengeId, commentId, replyId, replyData) => {
  return api.put(`/challenges/${challengeId}/comments/${commentId}/replies/${replyId}`, replyData);
};
