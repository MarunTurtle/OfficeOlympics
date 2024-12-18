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

	
	@Override
	public List<Map<String, Object>> getComments(int challengeId) {
		return commentsDao.getComments(challengeId);
	}


	@Override
	public boolean findUserComments(int challengeId) {
		return commentsDao.findUserComments(challengeId) > 0;
	}


	@Override
	public int findWriter(int commentId) {
		return commentsDao.findWriter(commentId);
	}
	

	@Override
	@Transactional
	public boolean insertComment(Comments comments) {
		int isInserted = commentsDao.insertComment(comments);
		
		updateCommentGroup(comments.getCommentId());
		
		return isInserted == 1;
	}

	
	@Override
	@Transactional
	public void updateCommentGroup(int commentId) {
		commentsDao.updateCommentGroup(commentId);
	}
	
	
	@Override
	public boolean checkDeleted(int commentId) {
		return commentsDao.checkDeleted(commentId) == 1;
	}


	@Override
	@Transactional
	public boolean modifyComment(Comments comments) {
		return commentsDao.modifyComment(comments) == 1;
	}

	
	@Override
	@Transactional
	public boolean insertReply(Comments comments) {
		return commentsDao.insertReply(comments) == 1;
	}
	
	
	@Override
	@Transactional
	public boolean modifyReply(Comments comments) {
		return commentsDao.modifyReply(comments) == 1;
	}

	
	@Override
	@Transactional
	public boolean deleteCommentOrReply(int commentId, int userId) {
		Comments comment = commentsDao.findCommentById(commentId);

        if (comment == null) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }

        if (comment.getCommentDepth() == 0) {
            int replyCount = commentsDao.countReplies(commentId);

            if (replyCount > 0) {

            	Map<String, Object> params = new HashMap<>();
            	params.put("commentId", commentId);
            	params.put("commentText", "삭제된 메시지입니다");
            	commentsDao.updateCommentText(params);
            	
            } else {
            	commentsDao.deleteCommentById(commentId);
            }
        } else {
        	
        	commentsDao.deleteCommentById(commentId);

            int repliesExist = commentsDao.countReplies(comment.getCommentGroup());
            
            if (repliesExist == 0) {
                Comments parentComment = commentsDao.findCommentById(comment.getCommentGroup());
                if (parentComment.getIsDeleted() == 1) {
                	commentsDao.deleteCommentById(comment.getCommentGroup());
                }
            }
        }

        return true; 
	}
	
	

}
