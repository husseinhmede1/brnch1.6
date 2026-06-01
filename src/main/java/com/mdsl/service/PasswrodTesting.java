package com.mdsl.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mdsl.utils.PasswordGenerator;

public class PasswrodTesting {
	
	
	
	public static void main(String a[]) {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String clearPassword = PasswordGenerator.generatePassword(10);
		System.out.println(clearPassword);
		String encryptedPassword = bCryptPasswordEncoder.encode(clearPassword);
		System.out.println(encryptedPassword);

	}

}
