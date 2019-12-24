package com.example.utils;

import java.util.Random;

public class RandomUtils {
    public static final String UpperLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LowerLetter = "abcdefghijklmnopqrstuvwxyz";
    public static final String DigitLetter = "0123456789";
    public static final String UpperAndLower = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String UpperAndDigit = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final String LowerAndDigit = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String AllLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String codeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public RandomUtils() {
    }

    public RandomUtils(String randomCharSet) {
        if (randomCharSet != null && !randomCharSet.equals("")) {
            this.codeChars = randomCharSet;
        }

    }

    public String generate(int len) {
        StringBuilder result = new StringBuilder(len);
        char[] chars = this.codeChars.toCharArray();
        Random rnd = new Random();

        for(int i = 0; i < len; ++i) {
            result.append(String.valueOf(chars[rnd.nextInt(chars.length)]));
        }

        return result.toString();
    }
}
