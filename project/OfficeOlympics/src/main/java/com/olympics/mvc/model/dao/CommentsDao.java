package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsDao {
	
	// 챌린지별 댓글 확인
	List<Comments> getComments(int challengeId);
	
	// 사용자가 작성한 댓글이 있는지 확인
	int findUserComments(int challengeId);

	// 댓글 쓰기
	int insertComment(Comments comments);
	
	// 댓글 수정
	int modifyComment(Comments comments);
	
	// 댓글 삭제
	int deleteComment(Map<String, Object> comment);
	
}
