package com.mdsl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {

	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "$@";
	
	public static String generatePassword(int passLength) {
		String pass = "";
		try {
	        StringBuilder password = new StringBuilder(passLength);
	        Random random = new Random(System.nanoTime());

	        // Collect the categories to use.
	        List<String> charCategories = new ArrayList<String>(4);
	        charCategories.add(LOWER);
	        charCategories.add(UPPER);
	        charCategories.add(DIGITS);
	        charCategories.add(PUNCTUATION);
	        
	        // Build the password.
	        for (int i = 0; i < passLength; i++) {
	            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
	            int position = random.nextInt(charCategory.length());
	            password.append(charCategory.charAt(position));
	        }
	        pass = password.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return pass;
	}
}