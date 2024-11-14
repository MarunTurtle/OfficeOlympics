package com.olympics.mvc.model.service;

import java.util.List;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsService {
	
	// 챌린지별 댓글 확인
	List<Comments> getComments(int challengeId);
	
	// 사용자가 작성한 댓글이 있는지 확인
	boolean findUserComments(int commentId);
	
	// 댓글 쓰기
	boolean insertComment(Comments comments);
	
	// 댓글 수정
	boolean modifyComment(Comments comments);
	
	// 댓글 삭제
	boolean deleteComment(int commentId, int challengeId);
	
		
}
