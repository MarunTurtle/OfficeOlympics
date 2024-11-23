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
	@Override
	public boolean checkDeleted(Comments comments) {
		Map<String, Object> params = new HashMap<>();
		params.put("commentId", comments.getCommentId());
		params.put("userId", comments.getUserId());
		
		int isDeleted = commentsDao.checkDeleted(params);
		return isDeleted == 1;
	}

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
		Comments comment = commentsDao.findCommentById(commentId);

        if (comment == null) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }

     // 원댓글 처리
        if (comment.getCommentDepth() == 0) {
            int replyCount = commentsDao.countReplies(commentId);

            if (replyCount > 0) {
                // 대댓글이 있으면 텍스트 변경
            	Map<String, Object> params = new HashMap<>();
            	params.put("commentId", commentId);
            	params.put("commentText", "삭제된 메시지입니다");
            	commentsDao.updateCommentText(params);
            } else {
                // 대댓글이 없으면 삭제
            	commentsDao.deleteCommentById(commentId);
            }
        } else {
            // 대댓글 처리
        	commentsDao.deleteCommentById(commentId);

            // 해당 댓글 그룹의 남아있는 대댓글 수 확인
            int remainingReplies = commentsDao.countReplies(comment.getCommentGroup());
            if (remainingReplies == 0) {
                // 원댓글 상태 확인 후 삭제
                Comments parentComment = commentsDao.findCommentById(comment.getCommentGroup());
                if ("삭제된 메시지입니다".equals(parentComment.getCommentText())) {
                	commentsDao.deleteCommentById(comment.getCommentGroup());
                }
            }
        }

        return true; // 삭제 성공 시 true 반환
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
