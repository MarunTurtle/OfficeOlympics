package com.olympics.mvc.model.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

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
	
	///////////////////////////////////회원가입///////////////////////////////////
	
	// 회원가입 로직
	@Transactional
	@Override
	public boolean insertUser(User user, MultipartFile profileImg) {
		// 비밀번호 암호화
		String salt = HashUtil.generateSalt();
		user.setSalt(salt);
		String hashedPassword = HashUtil.hashPassword(user.getPassword(), salt);
		user.setPassword(hashedPassword);
		
		// 파일이 있으면 저장
		if (profileImg != null && profileImg.getSize() > 0) {
			try {
				// 확장자 검증
				if (!FileConfirm.isValidImageFile(profileImg)) {
				    throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
				}
				
				String orgImg = profileImg.getOriginalFilename();
				String img = LocalDate.now()+"_"+orgImg;
				
				String staticPath = "src/main/resources/static/uploads/profile";
				File uploadDir = new File(staticPath);
				
				if(!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				
				Path filePath = Paths.get(staticPath, img);
				
				try (InputStream inputStream = profileImg.getInputStream()) {
				    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				
				String prefix = "http://localhost:8080/uploads/profile/";
				user.setProfileImg(prefix+img);
				user.setImgSrc(orgImg);
				
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			user.setProfileImg(null);
			user.setImgSrc(null);
		}
		
		int res = userDao.insertUser(user);
		return res == 1;
	}
	
	// 회원 탈퇴
	@Transactional
	@Override
	public boolean deleteUser(int id) {
		int res = userDao.deleteUser(id);
		return res == 1;
	}
	
	
	
	///////////////////////////////////로그인///////////////////////////////////
	
	// 로그인 로직
	@Override
	public User selectUser(String email) {
		return userDao.selectUser(email);
	}
	
	// 비밀번호 확인 메서드
	public boolean checkPassword(User user, String inputPassword) {
		// 사용자의 솔트를 가져와서 입력된 비밀번호를 해싱
		String hashedInputPassword = HashUtil.hashPassword(inputPassword, user.getSalt()); // 입력된 비밀번호 해싱
		
		// 해싱된 비밀번호가 저장된 비밀번호와 일치하는지 확인
		return hashedInputPassword.equals(user.getPassword());
	}

	
	///////////////////////////////////마이페이지///////////////////////////////////
	
	
	// id index로 회원 조회 (마이페이지 조회)
	@Override
	public User selectById(int id) {
		return userDao.selectById(id);
	}

	// 회원 정보 수정
	@Transactional
	@Override
	public boolean modifyUser(User user, MultipartFile profileImg) {
		// 기존 사용자 정보 조회
		User exUser = userDao.selectById(user.getUserId());
		System.out.println(exUser);
		// 파일이 있으면 저장
		if (profileImg != null && profileImg.getSize() > 0) {
			try {
				// 확장자 검증
				if (!FileConfirm.isValidImageFile(profileImg)) {
					throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
				}
				
				String orgImg = profileImg.getOriginalFilename();
				String img = LocalDate.now()+"_"+orgImg;
				
				String staticPath = "src/main/resources/static/uploads/profile";
				File uploadDir = new File(staticPath);
				
				if(!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				
				Path filePath = Paths.get(staticPath, img);
				
				try (InputStream inputStream = profileImg.getInputStream()) {
				    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				
				String prefix = "http://localhost:8080/uploads/profile/";
				user.setProfileImg(prefix+img);
				user.setImgSrc(orgImg);
				
				
			} catch (Exception e){
				e.printStackTrace();
				return false;	
			}
		} else {
			// 이미지 파일이 없으면 기존 이미지 데이터 유지
			user.setProfileImg(exUser.getProfileImg());
			user.setImgSrc(exUser.getImgSrc());
		}
		
		// 기타 정보 업데이트 (빈 값이 덮어쓰지 않도록 처리)
		if (user.getName() == null) user.setName(exUser.getName());
		if (user.getEmail() == null) user.setEmail(exUser.getEmail());
		
		// 사용자 정보 업데이트
		int res = userDao.modifyUser(user);
		return res == 1;
	}
	
	
	// 전체 계정 정보 조회
	@Override
	public List<User> selectAccounts() {
		return userDao.selectAccounts();
	}
	
}
