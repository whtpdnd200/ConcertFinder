package com.concertfinder.concertfinder.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256HashingEncoder {

    private static final int SALT_SIZE = 16;

    // byte 16진수 변환 메서드
    public static String byteToString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for(byte b : bytes) {

            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    // salt 생성 메서드
    public static String getSalt() {

        SecureRandom secureRandom = new SecureRandom();

        byte[] temp = new byte[SALT_SIZE];

        secureRandom.nextBytes(temp);

        return byteToString(temp);
    }



    // 비밀번호 암호화 메서드
    public static String encode(String message, String salt) {
        try {
            // getInstance로 생생되는 객체는 싱글톤 객체
            MessageDigest messageDigest = MessageDigest.getInstance("sha256");

            message = message + salt;
            byte[] bytes = message.getBytes();

            messageDigest.update(bytes);

            byte[] digest = messageDigest.digest();


            return byteToString(digest);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
