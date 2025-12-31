package com.concertfinder.concertfinder.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256HashingEncoder {

    public static String encode(String message) {
        try {
            // getInstance로 생생되는 객체는 싱글톤 객체
            MessageDigest messageDigest = MessageDigest.getInstance("sha256");

            byte[] bytes = message.getBytes();

            messageDigest.update(bytes);

            byte[] digest = messageDigest.digest();

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < digest.length; i++) {
                sb.append(Integer.toHexString(digest[i] & 0xff));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
