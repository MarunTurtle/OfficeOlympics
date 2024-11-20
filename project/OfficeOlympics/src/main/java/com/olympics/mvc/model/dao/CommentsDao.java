package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsDao {
	
	// 챌린지별 댓글 확인
	List<Map<String, Object>> getComments(int challengeId);
	
	// 사용자가 작성한 댓글이 있는지 확인
	int findUserComments(int challengeId);

	// 댓글 작성
	int insertComment(Comments comments);
	int updateCommentGroup (int commentId);
	
	// 댓글 수정
	int modifyComment(Comments comments);
	
	// 대댓글 확인
	boolean hasReplies(int commentId);
	
	// 소프트 삭제
	int softDeleteComment(Map<String, Object> params);
	
	// 댓글 삭제
	int deleteComment(Map<String, Object> params);

	// 대댓글 작성
	int insertReply(Comments comments);
	
	// 대댓글 수정
	int modifyReply(Comments comments);

}
