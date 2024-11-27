package com.olympics.mvc.model.dto;

public class Comments {
    private int commentId; // 댓글 ID
    private int userId; // 작성자 ID
    private int challengeId; // 챌린지 ID
    private int commentDepth; // 0 이면 댓글, 1이면 대댓글
    private int commentGroup; // 댓글 그룹 (원댓글의 ID)
    private String commentText; // 댓글내용
    private String regDate; // 작성일자
    private String updateDate; // 수정일자
    private int isDeleted; // 1이면 삭제된 댓글

    public Comments() {}

	public Comments(int commentId, int userId, int challengeId, int commentDepth, int commentGroup, String commentText,
			String regDate, String updateDate, int isDeleted) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.challengeId = challengeId;
		this.commentDepth = commentDepth;
		this.commentGroup = commentGroup;
		this.commentText = commentText;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.isDeleted = isDeleted;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public int getCommentDepth() {
		return commentDepth;
	}

	public void setCommentDepth(int commentDepth) {
		this.commentDepth = commentDepth;
	}

	public int getCommentGroup() {
		return commentGroup;
	}

	public void setCommentGroup(int commentGroup) {
		this.commentGroup = commentGroup;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	
}