package com.olympics.mvc.model.dao;

import com.olympics.mvc.model.dto.User;

public interface UserDao {

	int insertUser(User user);

	int deleteUser(int userId);

	User selectUser(String email);

	User selectById(int userId);

	int modifyUser(User user);
	
}
