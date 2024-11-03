package com.olympics.mvc.model.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dao.UserDao;
import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.util.FileConfirm;
import com.olympics.mvc.util.HashUtil;

@Service
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	private final ResourceLoader resourceLoader;
	
	public UserServiceImpl(UserDao userDao, ResourceLoader resourceLoader) {
		this.userDao = userDao;
		this.resourceLoader = resourceLoader;
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
				
				// 파일명과 확장자 설정
				String fileName = profileImg.getOriginalFilename();
				String fileSrc = UUID.randomUUID().toString();
				String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // "." 포함 확장자
				
				user.setProfile_img(fileName);
				user.setImg_src(fileSrc+extension);
				
				// 저장 경로 설정
				Resource resource = resourceLoader.getResource("classpath:/static/upload/profile");
				File uploadDir = resource.getFile(); // Resource를 File로 변환
				if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 파일 저장
				profileImg.transferTo(new File(uploadDir, fileSrc));
                
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			user.setProfile_img(null);
			user.setImg_src(null);
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
	
	// 회원 정보 수정
	@Transactional
	@Override
	public boolean modifyUser(User user, MultipartFile profileImg) {
		// 기존 사용자 정보 조회
		User exUser = userDao.selectById(user.getUser_id()); 
		// 파일이 있으면 저장
		if (profileImg != null && profileImg.getSize() > 0) {
			try {
				// 확장자 검증
				if (!FileConfirm.isValidImageFile(profileImg)) {
				    throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
				}
				
				// 파일명과 확장자 설정
				String fileName = profileImg.getOriginalFilename();
				String fileId = UUID.randomUUID().toString();
				String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // "." 포함 확장자
				
				user.setProfile_img(fileName);
				user.setImg_src(fileId+extension);
				
				// 저장 경로 설정
				Resource resource = resourceLoader.getResource("classpath:/static/upload/profile");
				File uploadDir = resource.getFile(); // Resource를 File로 변환
				if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
				
                // 파일 저장
				profileImg.transferTo(new File(uploadDir, fileId));
				
				} catch (Exception e){
					e.printStackTrace();
					return false;	
				}
			} else {
				// 이미지 파일이 없으면 기존 이미지 데이터 유지
	            user.setProfile_img(exUser.getProfile_img());
	            user.setImg_src(exUser.getImg_src());
			}
		
		// 기타 정보 업데이트 (빈 값이 덮어쓰지 않도록 처리)
        if (user.getName() == null) user.setName(exUser.getName());
        if (user.getEmail() == null) user.setEmail(exUser.getEmail());
        
        // 사용자 정보 업데이트
		int res = userDao.modifyUser(user);
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

	// id index로 회원 조회
	@Override
	public User selectById(int id) {
		return userDao.selectById(id);
	}

	// 전체 계정 정보 조회
	@Override
	public List<User> selectAccounts() {
		return userDao.selectAccounts();
	}
	
	
	
}
