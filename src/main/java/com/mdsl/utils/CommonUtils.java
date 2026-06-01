package com.mdsl.utils;

import java.util.Collection;
import java.util.Objects;

public class CommonUtils {

	public static String replaceEmptyStringIfNull(String replaceValue) {
		return isBlank(replaceValue) ? "" : replaceValue.trim();
	}
	
	public static boolean isBlank(String input) {
		return Objects.isNull(input) || input.isEmpty();
	}	
	
	public static boolean isNotNull(final Object obj) {
		return obj != null;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(final Object obj) {
		if (isNotNull(obj)) {
			if (obj instanceof Collection) {
				return !((Collection) obj).isEmpty();
			} else if (obj instanceof String) {
				return ((String) obj).length() > 0;
			} else if (obj.getClass().isArray()) {
				final Object[] oArr = (Object[]) obj;
				return oArr.length > 0;
			}
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(final Object obj) {
		return !isNotEmpty(obj);
	}
}