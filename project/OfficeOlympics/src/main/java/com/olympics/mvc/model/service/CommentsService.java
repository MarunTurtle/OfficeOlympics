package com.olympics.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Comments;

public interface CommentsService {
	
	List<Map<String, Object>> getComments(int challengeId);
	
	boolean findUserComments(int commentId);
	
	int findWriter(int commentId);
	
	boolean insertComment(Comments comments);
	
	void updateCommentGroup (int commentId);

	boolean checkDeleted(int commentId);
	
	boolean modifyComment(Comments comments);

	public boolean deleteCommentOrReply(int commentId, int userId);
	
	boolean insertReply(Comments comments);
	
	boolean modifyReply(Comments comments);
}
