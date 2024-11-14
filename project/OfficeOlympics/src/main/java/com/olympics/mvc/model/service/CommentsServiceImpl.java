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
	public List<Comments> getComments(int challengeId) {
		return commentsDao.getComments(challengeId);
	}

	// 댓글 쓰기
	@Override
	public boolean insertComment(Comments comments) {
		int isInserted = commentsDao.insertComment(comments);
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
	public boolean deleteComment(int commentId, int challengeId) {
		
		Map<String, Object> comment = new HashMap<>();
		comment.put("commentId", commentId);
		comment.put("challengeId", challengeId);
		
		Integer isDeleted = commentsDao.deleteComment(comment);

		if(isDeleted == null || isDeleted == 0) {
			isDeleted = 0;
		}
		return isDeleted == 1;
	}

	@Override
	public boolean findUserComments(int challengeId) {
		int isExist = commentsDao.findUserComments(challengeId);
		return isExist > 0;
	}


}
