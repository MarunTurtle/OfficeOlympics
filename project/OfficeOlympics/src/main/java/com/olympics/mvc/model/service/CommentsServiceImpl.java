package com.olympics.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olympics.mvc.model.dao.CommentsDao;
import com.olympics.mvc.model.dto.Comments;

@Service
@Transactional(rollbackFor = {Exception.class})
public class CommentsServiceImpl implements CommentsService{
	
	private final CommentsDao commentsDao;
	
	public CommentsServiceImpl(CommentsDao commentsDao) {
		super();
		this.commentsDao = commentsDao;
	}

	// 챌린지별 댓글 확인
	@Override
	public List<Map<String, Object>> getComments(int challengeId) {
		return commentsDao.getComments(challengeId);
	}

	// 사용자가 작성한 댓글이 있는지 확인
	@Override
	public boolean findUserComments(int challengeId) {
		int isExist = commentsDao.findUserComments(challengeId);
		return isExist > 0;
	}

	// 댓글 작성자 확인
	@Override
	public int findWriter(int commentId) {
		return commentsDao.findWriter(commentId);
	}
	
	// 댓글 쓰기
	@Transactional
	@Override
	public boolean insertComment(Comments comments) {
		int isInserted = commentsDao.insertComment(comments);
		
		updateCommentGroup(comments.getCommentId());
		
		return isInserted == 1;
	}

	// 댓글 수정
	@Transactional
	@Override
	public boolean modifyComment(Comments comments) {
		int isModified = commentsDao.modifyComment(comments);
		return isModified == 1;
	}

	// 댓글 삭제
	@Transactional
	@Override
	public boolean deleteCommentOrReply(int commentId, int userId) {
	    // 댓글에 대댓글이 있는지 확인
	    boolean hasReplies = commentsDao.hasReplies(commentId);
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("commentId", commentId);
	    params.put("userId", userId);

	    if (hasReplies) {
	        return commentsDao.softDeleteComment(params) > 0;
	    } else {
	    		return commentsDao.deleteComment(params) > 0;
	    }
	}

	
	// 대댓글 작성
	@Transactional
	@Override
	public boolean insertReply(Comments comments) {
		int isInserted = commentsDao.insertReply(comments);
		return isInserted == 1;
	}

	@Transactional
	@Override
	public void updateCommentGroup(int commentId) {
		commentsDao.updateCommentGroup(commentId);
	}

	
	// 대댓글 수정
	@Transactional
	@Override
	public boolean modifyReply(Comments comments) {
		int isModified = commentsDao.modifyReply(comments);
		return isModified == 1;
	}



}
