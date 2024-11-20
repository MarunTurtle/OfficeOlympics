package com.olympics.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.olympics.mvc.model.dao.CommentsDao;
import com.olympics.mvc.model.dto.Comments;

@Service
public class CommentsServiceImpl implements CommentsService{
	
	private final CommentsDao commentsDao;
	
	public CommentsServiceImpl(CommentsDao commentsDao) {
		super();
		this.commentsDao = commentsDao;
	}

	// 챌린지별 댓글 확인
	@Override
	public List<Map<String, Object>> getComments(int challengeId, int offset, int limit) {
		Map<String, Object> params = new HashMap<>();
	    params.put("challengeId", challengeId);
	    params.put("offset", offset);
	    params.put("limit", limit);
		return commentsDao.getComments(params);
	}

	// 사용자가 작성한 댓글이 있는지 확인
	@Override
	public boolean findUserComments(int challengeId) {
		int isExist = commentsDao.findUserComments(challengeId);
		return isExist > 0;
	}

	// 댓글 쓰기
	@Override
	public boolean insertComment(Comments comments) {
		int isInserted = commentsDao.insertComment(comments);
		
		updateCommentGroup(comments.getCommentId());
		
		return isInserted == 1;
	}

	// 댓글 수정
	@Override
	public boolean modifyComment(Comments comments) {
		int isModified = commentsDao.modifyComment(comments);
		return isModified == 1;
	}

	// 댓글 삭제
	@Override
	public boolean deleteCommentOrReply(int commentId, int userId) {
	    // 댓글에 대댓글이 있는지 확인
	    boolean hasReplies = commentsDao.hasReplies(commentId);
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("commentId", commentId);
	    params.put("userId", userId);

	    if (hasReplies) {
	        // 대댓글이 있는 경우: 소프트 삭제
	        return commentsDao.softDeleteComment(params) > 0;
	    } else {
	        // 대댓글이 없는 경우: 진짜 삭제
	        return commentsDao.deleteComment(params) > 0;
	    }
	}



	// 대댓글 작성
	@Override
	public boolean insertReply(Comments comments) {
		int isInserted = commentsDao.insertReply(comments);
		return isInserted == 1;
	}

	// 대댓글 수정
	@Override
	public boolean modifyReply(Comments comments) {
		int isModified = commentsDao.modifyReply(comments);
		return isModified == 1;
	}

	@Override
	public void updateCommentGroup(int commentId) {
		commentsDao.updateCommentGroup(commentId);
	}


}
