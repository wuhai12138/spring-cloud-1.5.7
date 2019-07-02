package com.sfy.user.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by 金鹏祥 on 2019/3/28.
 */
public class RandomGenerator {

    private static final char[] DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final char[] DEFAULT_INT_CODEC = "1234567890".toCharArray();
    private Random random = new SecureRandom();
    private int length;

    public RandomGenerator() {
        this(6);
    }
    public RandomGenerator(int length) {
        this.length = length;
    }

    public String generate() {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getAuthorizationCodeString(verifierBytes);
    }
    public String generateInt() {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getAuthorizationInt(verifierBytes);
    }

    protected String getAuthorizationCodeString(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = DEFAULT_CODEC[((verifierBytes[i] & 0xFF) % DEFAULT_CODEC.length)];
        }
        return new String(chars);
    }

    protected String getAuthorizationInt(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = DEFAULT_INT_CODEC[((verifierBytes[i] & 0xFF) % DEFAULT_INT_CODEC.length)];
        }
        return new String(chars);
    }
}
