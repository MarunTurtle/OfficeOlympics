package com.olympics.mvc.model.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dao.UserDao;
import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.util.FileConfirm;
import com.olympics.mvc.util.HashUtil;

@Service
@Transactional(rollbackFor = {Exception.class})
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	
	public UserServiceImpl(UserDao userDao,  ResourceLoader resourceLoader) {
		this.userDao = userDao;
	}
	
	private final String PREFIX = "http://localhost:8080/uploads/profile/";
	private final String IMG_PATH = "src/main/resources/static/uploads/profile";
	
	/**
	 * 회원가입
	 */
	@Transactional
	@Override
	public boolean insertUser(User user, MultipartFile profileImg) {
		String salt = HashUtil.generateSalt();
		user.setSalt(salt);
		String hashedPassword = HashUtil.hashPassword(user.getPassword(), salt);
		user.setPassword(hashedPassword);
		
		if (profileImg != null && profileImg.getSize() > 0) {
			try {

				if (!FileConfirm.isValidImageFile(profileImg)) {
				    throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
				}
				
				String orgImg = profileImg.getOriginalFilename();
				String uuid = UUID.randomUUID().toString().replace("-", "");
				String uuidImgName = uuid+"_"+orgImg;
				
				File uploadDir = new File(IMG_PATH);
				
				if(!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				
				Path filePath = Paths.get(IMG_PATH, uuidImgName);
				
				try (InputStream inputStream = profileImg.getInputStream()) {
				    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				
				user.setProfileImg(orgImg);
				user.setImgSrc(PREFIX+uuidImgName);
				
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			
			String defaultImg = "default_profile.png";
			File uploadDir = new File(IMG_PATH);
			
			if(!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			
			user.setProfileImg(defaultImg);
			user.setImgSrc(PREFIX+defaultImg);
		}
		
		return userDao.insertUser(user) == 1;
	}
	
	/**
	 * 회원탈퇴
	 */
	@Transactional
	@Override
	public boolean deleteUser(int id) {
		return userDao.deleteUser(id) == 1;
	}

	/**
	 * 로그인
	 */
	@Override
	public User selectUser(String email) {
		return userDao.selectUser(email);
	}
	
	/**
	 * 비밀번호 확인
	 */
	public boolean checkPassword(User user, String inputPassword) {
		// 사용자의 솔트를 가져와서 입력된 비밀번호를 해싱
		String hashedInputPassword = HashUtil.hashPassword(inputPassword, user.getSalt()); // 입력된 비밀번호 해싱
		
		// 해싱된 비밀번호가 저장된 비밀번호와 일치하는지 확인
		return hashedInputPassword.equals(user.getPassword());
	}

	/**
	 * id index로 회원 조회 (마이페이지 조회)
	 */
	@Override
	public User selectById(int id) {
		return userDao.selectById(id);
	}

	/**
	 * 회원정보 수정
	 */
	@Transactional
	@Override
	public boolean modifyUser(User user, MultipartFile profileImg) {
		
		User exUser = userDao.selectById(user.getUserId());
		
		if (profileImg != null && profileImg.getSize() > 0) {
			try {
				
				if (!FileConfirm.isValidImageFile(profileImg)) {
					throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
				}
				
				String orgImg = profileImg.getOriginalFilename();
				String uuid = UUID.randomUUID().toString().replace("-", "");
				String uuidImgName = uuid+"_"+orgImg;
				
				File uploadDir = new File(IMG_PATH);
				
				if(!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				
				Path filePath = Paths.get(IMG_PATH, uuidImgName);
				
				try (InputStream inputStream = profileImg.getInputStream()) {
				    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				
				user.setProfileImg(orgImg);
				user.setImgSrc(PREFIX+uuidImgName);
				
				
			} catch (Exception e){
				e.printStackTrace();
				return false;	
			}
		} else {
			
			user.setProfileImg(exUser.getProfileImg());
			user.setImgSrc(exUser.getImgSrc());
		}
		
		if (user.getName() == null) user.setName(exUser.getName());
		if (user.getEmail() == null) user.setEmail(exUser.getEmail());
		
		return userDao.modifyUser(user) == 1;
	}
		
}
