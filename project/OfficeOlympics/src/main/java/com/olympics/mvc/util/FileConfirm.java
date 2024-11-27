package com.olympics.mvc.util;

import org.springframework.web.multipart.MultipartFile;

public class FileConfirm {
	
	/**
	 * 파일 확장자 확인
	 * @param file 사용자가 등록한 file
	 * @return 파일 형식 확인 후 jpg/ png가 아닐 경우 false 반환
	 */
	public static boolean isValidImageFile(MultipartFile file) {
		
		// 1. 파일 확장자 확인
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png"))) {
            return false;
        }
        
        // 2. MIME 타입 확인
		String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            return false;
        }
        
        return true;
	}

}
