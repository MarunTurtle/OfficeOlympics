package com.olympics.mvc.model.dto;

public class User {
	int user_id;
	String email; // 로그인 아이디
	String password; // 비밀번호
	String name; // 닉네임
	String profile_img; // 사용자가 등록한 사진 이름
	String img_src; // 서버에 저장한 이름
	String reg_date; // 가입일자
	
	// User 생성자
	public User(int user_id, String email, String password, String name, String profile_img, String img_src,
			String reg_date) {
		this.user_id = user_id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.profile_img = profile_img;
		this.img_src = img_src;
		this.reg_date = reg_date;
	}
	
	// Getters, Setters
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getImg_src() {
		return img_src;
	}

	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@Override
	public String toString() {
		return "UserDto [user_id=" + user_id + ", email=" + email + ", password=" + password + ", name=" + name
				+ ", profile_img=" + profile_img + ", img_src=" + img_src + ", reg_date=" + reg_date + "]";
	}
	

	
}
