package org.profit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date utilities
 * 
 * @author Kyia
 */
public class DateUtils {

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	
	private static SimpleDateFormat timeFormat = new SimpleDateFormat(DEFAULT_TIME_FORMAT);

	public static final long MILLISECOND_PER_DAY = 24 * 60 * 60 * 1000;

    public static final long HOUR_MILLISECONDS = 3600000L;

	/**
	 * Constructor
	 */
	public DateUtils() {
		
	}
	
	/**
	 * Format time with default time pattern
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		return timeFormat.format(new Date(time));
	}
	
	/**
	 * Format time with default date pattern
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDate(long time) {
		return dateFormat.format(new Date(time));
	}
	
	/**
	 * Format time with pattern
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formatTime(long time, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date(time));
	}
	
	/**
	 * Parse time
	 * 
	 * @param str
	 * @return
	 */
	public static synchronized long parseTime (String str) {
		long time = 0L;
		try {
			Date date = dateFormat.parse(str);
			time = date.getTime();
		} catch (ParseException e) {
			// e...
		}
		return time;
	}
	
	/**
	 * Parse time
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static long parseTime(String str, String pattern) {
		long time = System.currentTimeMillis();
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			Date date = format.parse(str);
			time = date.getTime();
		} catch (ParseException e) {
			// e...
		}
		return time;
	}


    /**
     * 获取零点
     *
     * @return
     */
    public static long getZeroTime() {
        return getZeroTime(System.currentTimeMillis());
    }

	/**
	 * Get zero time, a day's begin
	 * 
	 * @param time
	 * @return
	 */
	public static long getZeroTime(long time) {
		// Get date string
		String str = dateFormat.format(new Date(time));
		
		long zeroTime = time;
		try {
			Date date = dateFormat.parse(str);
			zeroTime = date.getTime();
		} catch (ParseException e) {
			// e....
		}
		return zeroTime;
	}
	
	/**
	 * Get day interval
	 * end must bigger than start
	 * end > start
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDayInterval(long start, long end) {
		long startZeroTime = getZeroTime(start);
		long endZeroTime = getZeroTime(end);
		
		int day = (int) ((endZeroTime - startZeroTime) / (24 * 60 * 60 * 1000));
		return Math.abs(day);
	}

	/**
	 * 是否是周末
	 *
	 * @return
     */
	public static boolean isWeek(String str) {
		try {
			Date date = dateFormat.parse(str);
			int day = date.getDay();
			if (day == 0 || day == 6) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}

