package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsDao {
	
	// 챌린지별 댓글 확인
	List<Map<String, Object>> getComments(int challengeId);
	
	// 사용자가 작성한 댓글이 있는지 확인
	int findUserComments(int challengeId);

	// 댓글 작성자 확인
	int findWriter(int commentId);
	
	// 댓글 작성
	int insertComment(Comments comments);
	
	// 댓글 작성 완료 후 그룹 ID 설정
	int updateCommentGroup (int commentId);
	
	// 댓글이 삭제 상태인지 확인
	int checkDeleted(Map<String, Object> params);
	// 댓글 수정
	int modifyComment(Comments comments);

	// 대댓글 작성
	int insertReply(Comments comments);
	
	// 대댓글 수정
	int modifyReply(Comments comments);

	// 댓글 ID를 통해 특정 댓글 조회
	Comments findCommentById(int commentId);

	// 특정 댓글에 달린 대댓글 개수를 조회
	int countReplies(int commentId);

	// 댓글 텍스트를 업데이트
	void updateCommentText(Map<String, Object> params);

	// 댓글 텍스트를 업데이트
	void deleteCommentById(int commentId);

}
