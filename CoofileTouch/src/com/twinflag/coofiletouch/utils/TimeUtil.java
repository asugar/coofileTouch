package com.twinflag.coofiletouch.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeUtil {
	public static String key;
	public static int k;
	static long LoopTime;

	public static String getTranslateHM(long ms){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String hms = formatter.format(ms);
		return hms;
	}
	
	public static long getTimeStringHM2Long(String time) {
		String[] my = time.split(":");
		int hour = Integer.parseInt(my[0]);
		int min = Integer.parseInt(my[1]);
		long totalSec = hour * 3600 + min * 60;
		long totalMilSec = totalSec * 1000;
		return totalMilSec;
	}
	
	public static String getTranslateHMS(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String hms = formatter.format(time);
		return hms;
	}

	//
	public static String getTranslateHMS(long ms) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String hms = formatter.format(ms);
		return hms;
	}
	
	//
	public static String getZeroTimeZoneTranslateHMS(long ms) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(ms);
		return hms;
	}

	//
	public static String showYMDHMS(long ms) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(ms);
		return hms;
	}

	//
	public static long getTimeString2Long(String time) {
		String[] my = time.split(":");
		int hour = Integer.parseInt(my[0]);
		int min = Integer.parseInt(my[1]);
		int sec = Integer.parseInt(my[2]);
		long totalSec = hour * 3600 + min * 60 + sec;
		long totalMilSec = totalSec * 1000;
		return totalMilSec;
	}

	public static String getTheKey(Boolean flag, int k) {
		switch (k) {
			case 0:
				if (flag) {
					key = "sundayON";
				} else {
					key = "sundayOFF";
				}
				break;
			case 1:
				if (flag) {
					key = "mondayON";
				} else {
					key = "mondayOFF";
				}
				break;
			case 2:
				if (flag) {
					key = "tuesdayON";
				} else {
					key = "tuesdayOFF";
				}
				break;
			case 3:
				if (flag) {
					key = "wednesdayON";
				} else {
					key = "wednesdayOFF";
				}
				break;
			case 4:
				if (flag) {
					key = "thursdayON";
				} else {
					key = "thursdayOFF";
				}
				break;
			case 5:
				if (flag) {
					key = "fridayON";
				} else {
					key = "fridayOFF";
				}
				break;
			case 6:
				if (flag) {
					key = "saturdayON";
				} else {
					key = " saturdayOFF";
				}
				break;
		}
		return key;
	}

	//
	public static byte[] longToByteArray1(int flags, long times) {
		byte[] result = new byte[9];

		result[0] = (byte) 0x00;
		result[1] = (byte) 0xaa;
		result[2] = (byte) 0xff;
		result[3] = (byte) 0x55;

		result[4] = (byte) (flags);

		result[5] = (byte) ((times >> 16) & 0xFF);
		result[6] = (byte) ((times >> 8) & 0xFF);
		result[7] = (byte) (times & 0xFF);

		result[8] = (byte) 0x55;

		return result;
	}

	public static Boolean SetTimeEQU(long OpenTime, long ClosedTime) {
		return Math.abs(getTimeString2Long(getZeroTimeZoneTranslateHMS(OpenTime))
						- getTimeString2Long(getZeroTimeZoneTranslateHMS(ClosedTime))) < 60 * 1000 ? true : false;
	}

	//
	public static int getTimeAccuracy(long MathTime) {
		int addTime = 0;
		addTime = (int) (MathTime / (15 * 60)); //
		return addTime;
	}

}
