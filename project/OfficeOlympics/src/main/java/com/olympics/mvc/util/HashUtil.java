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
            // SHA-256 해싱 알고리즘을 사용하기 위해 MessageDigest 인스턴스 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // 솔트를 해싱 데이터에 추가하여 보안을 강화
            digest.update(salt.getBytes());
            
            // 비밀번호를 바이트 배열로 변환한 후 SHA-256으로 해싱
            byte[] hash = digest.digest(password.getBytes());
            
            // 해싱된 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                // 각 바이트를 16진수 문자열로 변환
                String hex = Integer.toHexString(0xff & b);
                // 한 자릿수의 경우 앞에 0을 붙여 두 자리로 만듦
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // 최종적으로 완성된 16진수 문자열을 반환
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 알고리즘을 찾지 못한 경우 예외를 발생시킴
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 안전한 솔트를 생성하는 메서드
     * 
     * @return 생성된 솔트를 Base64 문자열로 반환
     */
    public static String generateSalt() {
        // 16바이트 길이의 솔트 배열 생성
        byte[] salt = new byte[16];
        
        // 보안 랜덤 객체를 이용해 솔트를 안전하게 생성
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt); // 솔트 바이트 배열을 랜덤으로 채움
        
        // 생성된 솔트를 Base64 인코딩하여 문자열로 반환
        return Base64.getEncoder().encodeToString(salt);
    }
}