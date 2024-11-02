package com.olympics.mvc.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class FileConfirm {
	
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
