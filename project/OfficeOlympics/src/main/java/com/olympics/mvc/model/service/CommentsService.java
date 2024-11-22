package com.olympics.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsService {
	
	// 챌린지별 댓글 확인
	List<Map<String, Object>> getComments(int challengeId);
	
	// 사용자가 작성한 댓글이 있는지 확인
	boolean findUserComments(int commentId);
	
	// 댓글 작성자 확인
	int findWriter(int commentId);
	
	// 댓글 작성
	boolean insertComment(Comments comments);
	void updateCommentGroup (int commentId);
	
	// 댓글 수정
	boolean checkDeleted(Comments comments);
	boolean modifyComment(Comments comments);
	
	// 댓글 삭제
	public boolean deleteCommentOrReply(int commentId, int userId);
	
	// 대댓글 작성
	boolean insertReply(Comments comments);
	
	// 대댓글 수정
	boolean modifyReply(Comments comments);
}
