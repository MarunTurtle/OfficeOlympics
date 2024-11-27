package com.olympics.mvc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
	
	/**
     * 비밀번호를 SHA-256 해시로 변환하는 메서드
     * 
     * @param password 사용자가 입력한 비밀번호
     * @param salt     비밀번호에 추가할 솔트 값
     * @return 해싱된 비밀번호를 16진수 문자열로 반환
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            digest.update(salt.getBytes());
            
            byte[] hash = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 안전한 솔트를 생성하는 메서드
     * 
     * @return 생성된 솔트를 Base64 문자열로 반환
     */
    public static String generateSalt() {

        byte[] salt = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt); 

        return Base64.getEncoder().encodeToString(salt);
    }
}