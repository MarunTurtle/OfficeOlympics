package com.olympics.mvc.model.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.olympics.mvc.model.dao.UserDao;
import com.olympics.mvc.model.dto.User;

@Service
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	// 회원가입 로직
	@Override
	public boolean insertUser(User user) {
		int res = userDao.insertUser(user);
		return res == 1;
	}
	
	// 로그인 로직
	@Override
	public User selectUser(User user) {
		User foundUser = userDao.selectUser(user.getEmail());
		// 로그인 시 비밀번호 검증
		if (foundUser != null && user.getPassword().equals(foundUser.getPassword())) {
			return foundUser;
		}
		return null;
	}
	
	// 회원 탈퇴
	@Override
	public boolean deleteUser(int id) {
		int res = userDao.deleteUser(id);
		return res == 1;
	}
	
	// 회원 정보 수정
	@Override
	public boolean modifyUser(User user) {
		int res = userDao.modifyUser(user);
		return res == 1;
	}

	@Override
	public List<User> selectAccounts() {
		return userDao.selectAccounts();
	}
	
}
