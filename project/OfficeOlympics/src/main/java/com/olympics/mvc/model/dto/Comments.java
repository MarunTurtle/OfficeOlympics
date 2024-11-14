package com.olympics.mvc.model.dto;

public class Comments {
	private int commentId;
	private int userId;
	private int challengeId;
	private String commentText;
	private String regDate;
	private String updateDate;
	
	public Comments() {
		super();
	}

	public Comments(int commentId, int userId, int challengeId, String commentText, String regDate, String updateDate) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.challengeId = challengeId;
		this.commentText = commentText;
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
	
	public Comments(int commentId, int userId, int challengeId, String commentText, String updateDate) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.challengeId = challengeId;
		this.commentText = commentText;
		this.updateDate = updateDate;
	}
	
	// Getter, Setter

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

	@Override
	public String toString() {
		return "Comments [commentId=" + commentId + ", userId=" + userId + ", challengeId=" + challengeId
				+ ", commentText=" + commentText + ", regDate=" + regDate + ", updateDate=" + updateDate + "]";
	}	
	
	
}
