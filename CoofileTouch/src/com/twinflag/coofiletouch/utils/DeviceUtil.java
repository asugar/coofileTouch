package com.twinflag.coofiletouch.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.twinflag.coofiletouch.CoofileTouchApplication;

public class DeviceUtil {

	private static String getCpuInfo() {
		String cpuInfo = "00000000000000000000000000000000";
		Process process = null;
		InputStream inputStream = null;
		BufferedReader readBuffer = null;
		String cupSeriral = null;
		String readLine = "";
		try {
			process = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			if (process != null) {
				inputStream = process.getInputStream();
				if (inputStream != null) {
					readBuffer = new BufferedReader(new InputStreamReader(inputStream));
					readLine = readBuffer.readLine();
					while (readLine != null) {
						if (readLine.indexOf("Serial") > -1) {
							cupSeriral = readLine.substring(readLine.indexOf(":") + 1, readLine.length());
							cpuInfo = cupSeriral.trim();
							break;
						} else {
							readLine = readBuffer.readLine();
						}
					}
					readBuffer.close();
					inputStream.close();
				}
			}
		} catch (IOException ioException) {
		}
		return cpuInfo;
	}

	public static String getDeviceInfo() {
		String md5Value = null;
		String deviceInfo = null;
		String cpuInfo = null;
		String androidID = "";
		cpuInfo = getCpuInfo();
		androidID = CoofileTouchApplication.getAndroidID();
		
		if (cpuInfo != null) {
			md5Value = MD5Util.getMD5String(cpuInfo + androidID);
			if (md5Value != null) {
				deviceInfo = md5Value.substring(8, 12) + "-" + md5Value.substring(12, 16) + "-" + md5Value.substring(16, 20) + "-" + md5Value.substring(20, 28);
			}
		}
		return deviceInfo;
	}

	public static boolean isSdCardExist() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

}
