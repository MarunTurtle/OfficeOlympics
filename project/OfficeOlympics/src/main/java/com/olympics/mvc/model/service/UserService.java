package com.olympics.mvc.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.User;

public interface UserService {

	boolean insertUser(User user, MultipartFile profileImg);

	boolean deleteUser(int id);

	User selectUser(String email);

	boolean checkPassword(User user, String password);

	User selectById(int id);

	boolean modifyUser(User user, MultipartFile profileImg);
	
}
