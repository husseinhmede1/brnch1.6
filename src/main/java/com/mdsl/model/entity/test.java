package com.mdsl.model.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mdsl.utils.PasswordGenerator;

public class test {

	
	public static void main(String a[]) {
		
		String clearPassword = PasswordGenerator.generatePassword(10);
		System.out.println(clearPassword);
		BCryptPasswordEncoder endor = new BCryptPasswordEncoder();
		String encryptedPassword = endor.encode(clearPassword);
		System.out.println(encryptedPassword);
		
	}
	
}
