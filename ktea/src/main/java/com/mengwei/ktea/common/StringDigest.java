package com.mengwei.ktea.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create by MengWei at 2019/6/1
 */
public class StringDigest {
    static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    final byte[] data;

    StringDigest(byte[] bytes) {
        data = bytes;
    }

    public static StringDigest of(String str) {
        return new StringDigest(str.getBytes());
    }

    private byte[] digest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm).digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private String hex(byte[] data) {
        char[] result = new char[data.length * 2];
        int c = 0;
        for (byte b : data) {
            result[c++] = HEX_DIGITS[(b >> 4) & 0xf];
            result[c++] = HEX_DIGITS[b & 0xf];
        }
        return new String(result);
    }

    public String md5() {
        return hex(digest("MD5"));
    }

    public String sha1() {
        return hex(digest("SHA-1"));
    }

    public String sha256() {
        return hex(digest("SHA-256"));
    }

    public String sha512() {
        return hex(digest("SHA-512"));
    }


}
