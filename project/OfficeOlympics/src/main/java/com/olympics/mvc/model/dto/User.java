package com.olympics.mvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User DTO")
public class User {
	int userId;
	String email; // 로그인 아이디
	String password; // 비밀번호 (SHA-256)
	String salt; // 개별 salt
	String name; // 닉네임
	String nickname;
	String profileImg; // 사용자가 등록한 사진 이름
	String imgSrc; // 서버에 저장한 이름
	String regDate; // 가입일자
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	// User 생성자

	public User(int userId, String email, String password, String salt, String name, String nickname, String profileImg,
			String imgSrc, String regDate) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.name = name;
		this.nickname = nickname;
		this.profileImg = profileImg;
		this.imgSrc = imgSrc;
		this.regDate = regDate;
	}

	// Getters, Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", salt=" + salt + ", name="
				+ name + ", nickname=" + nickname + ", profileImg=" + profileImg + ", imgSrc=" + imgSrc + ", regDate="
				+ regDate + "]";
	}
	
}
