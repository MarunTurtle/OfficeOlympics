package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsDao {
	
	List<Map<String, Object>> getComments(int challengeId);
	
	int findUserComments(int challengeId);

	int findWriter(int commentId);
	
	int insertComment(Comments comments);
	
	int updateCommentGroup (int commentId);
	
	int checkDeleted(int commentId);
	
	int modifyComment(Comments comments);

	int insertReply(Comments comments);
	
	int modifyReply(Comments comments);

	Comments findCommentById(int commentId);

	int countReplies(int commentId);

	void updateCommentText(Map<String, Object> params);

	void deleteCommentById(int commentId);

}
