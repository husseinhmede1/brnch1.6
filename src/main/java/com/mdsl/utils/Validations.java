package com.mdsl.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.exceptionHandling.ValidationException;

public class Validations {

	public static void validate(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}
	}

	public static void isEmpty (List<?> list) {
		if (list.isEmpty()) {
			throw new BusinessException(ResponseCode.CFG_NO_DATA_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	public static void isValidDateRange (Timestamp fromDate, Timestamp toDate, int dateRange) {
		long difference_in_time = TimeUnit.MILLISECONDS.toMinutes(toDate.getTime() - fromDate.getTime());
		if(difference_in_time > dateRange) {
			throw new BusinessException(ResponseCode.VAL_INVALID_DATE_RANGE, HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Validates from/to dates 
	 * From date cannot be greater that to date 
	 * From date and to date cannot be greater than system date
	 */
	public static void dateValidations (Date fromDate, Date toDate) {
		long now = System.currentTimeMillis();
        Date systemDate = new Date(now);
		
		if(fromDate.compareTo(toDate) > 0) {
			throw new BusinessException(ResponseCode.VAL_FROM_DATE_GREATER_TO_DATE, HttpStatus.BAD_REQUEST);
		} 
		
		if(fromDate.compareTo(systemDate) > 0) {
			throw new BusinessException(ResponseCode.VAL_FROM_DATE_GREATER_SYSDATE, HttpStatus.BAD_REQUEST);
		} 
		
		if(toDate.compareTo(systemDate) > 0) {
			throw new BusinessException(ResponseCode.VAL_TO_DATE_GREATER_SYSDATE, HttpStatus.BAD_REQUEST);
		} 
	}
	
	/*
	 * Pads the string to the left with the padChar
	 */
	public static String leftPad(String s, String padChar, int totalLength) {
		if (s == null)
			s = "";
		StringBuffer newS = new StringBuffer(s);
		while (newS.length() < totalLength)
			newS.insert(0, padChar);
		return newS.toString();
	}

	/*
	 * Pads the string to the right with the padChar
	 */
	public static String rightPad(String s, String padChar, int totalLength) {
	    StringBuffer newS=new StringBuffer(s);
	    while (newS.length()<totalLength)
	    	newS.append(padChar);
	    return newS.toString();
	}

	/*
	 * public static void isValidPassword (PasswordPolicy passwordPolicy, String
	 * password, List<PasswordHistory> passwordHistory, PasswordEncoder
	 * passwordEncoder) { if(password.trim().length() >
	 * passwordPolicy.getPasswordLength()) { throw new BusinessException
	 * (ResponseCode.CFG_INVALID_PASSWORD_LENGTH, HttpStatus.BAD_REQUEST); }
	 *
	 * passwordHistory.stream().forEach((passHist)->{ if
	 * (passwordEncoder.matches(password.trim(), passHist.getPassword())){ throw new
	 * BusinessException(ResponseCode.CFG_INVALID_PASSWORD_ALREADY_USED,
	 * HttpStatus.BAD_REQUEST); } }); }
	 */
	public static void isValidDateRange (Date fromDate, Date toDate, int dateRange) {
		long difference_in_time = TimeUnit.DAYS.convert((toDate.getTime() - fromDate.getTime()), TimeUnit.MILLISECONDS);
		if(difference_in_time > dateRange) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE_RANGE, HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * Checks if all values inside a list are unique
	 */
	public static <T> boolean areAllUnique(List<T> list){
	    Set<T> set = new HashSet<>();
	    return list.stream().allMatch(t -> set.add(t));
	}
	
	/*
	 * Check if all numeric values from 1 to list size exist
	 */
	public static Boolean allPositionsExist (List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != (i+1)) {
				return false;
			}
		}
		return true;
	}
	/*
	 * Validates date format
	 * Month cannot be less than 1 or greater than 12
	 * Year cannot be less than 1900 or greater than 2100
	 * If Day equals 0, Day is disregarded
	 * If Day not equals 0, Day cannot be less than 1 or greater than 31
	 * If Month equals February and Year is leap, Day cannot be greater than 29
	 * If Month equals February and Year is not leap, Day cannot be greater than 28
	 * If Month equals April, June, September or November, Day cannot be greater than 30
	 */
	public static void dateFormatValidation (int day, int month, int year) {
		if(month<1 || month>12) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
		if(year<1900 || year>2100) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
		if(day<0 || day>31) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
			
		if (month == 2){
	        if (isLeap(year) && day!=0 && day>29)
				throw new BusinessException(ResponseCode.CFG_INVALID_MONTH_DAY, HttpStatus.BAD_REQUEST);

	        else if(!isLeap(year) && day!=0 && day>28)
				throw new BusinessException(ResponseCode.CFG_INVALID_MONTH_DAY, HttpStatus.BAD_REQUEST);
	    }
		
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			if(day <0 || day>30)
				throw new BusinessException(ResponseCode.CFG_INVALID_MONTH_DAY, HttpStatus.BAD_REQUEST);

		}
	}
	
	public static void weekFormatValidation(int week, int year) {
		
		if(week<1 || week>52) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
		if(year<1900 || year>2100) {
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Check if Year is Leap
	 */
	public static boolean isLeap(int year) {
	return (((year % 4 == 0) &&
	         (year % 100 != 0)) ||
	         (year % 400 == 0));
	}
	
	/*
	 * Validates from/to dates 
	 * From date cannot be greater that to date 
	 * From date and to date cannot be greater than system date
	 */
	public static void dateValidationsScheduled (Date fromDate, Date toDate) {
		if(fromDate.compareTo(toDate) > 0) {
			throw new BusinessException(ResponseCode.VAL_FROM_DATE_GREATER_TO_DATE, HttpStatus.BAD_REQUEST);
		} 
	}
	
	/*
	 * Validates if from date is less than today
	 */
	public static void fromDateSysdateValidation (String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate fromDate = LocalDate.parse(date, formatter);
        LocalDate systemDate = LocalDate.now();    
        
        if(fromDate.isBefore(systemDate)) {
        	throw new BusinessException(ResponseCode.VAL_FROM_DATE_LESS_SYSDATE, HttpStatus.BAD_REQUEST);
        }
	}

	public static String getRequestURL(String requestURL) {
		if (requestURL.contains("springfox-swagger") || requestURL.contains("swagger-resources")) {
			return "/swagger-ui.html";
		}
		int slashCount = (int) requestURL.chars().filter(ch -> ch == '/').count();
		if (slashCount == 2) {
			requestURL = requestURL.substring(requestURL.lastIndexOf("/"));
		} else if (slashCount == 3) {
			String[] list = requestURL.split("/");
			requestURL = "/" + list[3];
		} else if (slashCount > 3) {
			String[] list = requestURL.split("/");
			requestURL = "/" + list[3] + "/" + list[4];
		}
		return requestURL;
	}
}