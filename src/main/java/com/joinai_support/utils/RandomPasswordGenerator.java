package com.joinai_support.utils;

import java.security.SecureRandom;

public class RandomPasswordGenerator {

    // Define the characters allowed in the password
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random password of the specified length.
     * @param length the desired password length (must be > 0)
     * @return a randomly generated password
     * @throws IllegalArgumentException if length <= 0
     */
    public static String generatePassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be greater than zero.");
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
