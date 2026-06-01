package com.mdsl.utils;

public class CronJob {
	public static String toCron(final String seconds, final String mins, final String hrs, final String dayOfMonth, final String month) {
        return String.format("%s %s %s %s %s ?", seconds, mins, hrs, dayOfMonth, month);
    }
}