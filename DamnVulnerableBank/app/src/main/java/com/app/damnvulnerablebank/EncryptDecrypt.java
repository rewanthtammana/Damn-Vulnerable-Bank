package com.app.damnvulnerablebank;

import android.util.Base64;

public class EncryptDecrypt {
    static public String secret = "amazing";
    static public int secretLength = secret.length();

    public static String operate(String input) {
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int xorVal = (int) input.charAt(i) ^ (int) secret.charAt(i % secretLength);
            char xorChar =  (char) xorVal;

            result += xorChar;
        }

        return result;
    }

    public static String encrypt(String input) {
        String encVal = operate(input);
        String val = Base64.encodeToString(encVal.getBytes(),0);

        return val;
    }

    public static String decrypt(String input) {
        byte[] decodeByte = Base64.decode(input,0);
        String decodeString = new String(decodeByte);
        String decryptString = operate(decodeString);

        return decryptString;
    }
}
