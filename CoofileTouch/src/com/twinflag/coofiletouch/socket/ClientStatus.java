package com.twinflag.coofiletouch.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

public class ClientStatus {

	/**
	 * 获取系统版本
	 */
	public static String getSystemVersion() {
		return "Android " + Build.VERSION.RELEASE;
	}

	/**
	 * 获取内存使用率
	 */
	public static String getMemoryUse() {
		int NUM = 4;
		long useRatio = 0L;
		try {
			long[] array = new long[4];
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/meminfo")),
							1000);
			String load = null;
			long temp;
			for (int i = 0; i < NUM; i++) {
				load = reader.readLine();
				String[] strs = load.replace(" ", "").split(":");
				temp = Long.parseLong(strs[1].substring(0, strs[1].indexOf("k")));
				array[i] = temp;
			}
			reader.close();
			if (array[0] != 0) {
				useRatio = 100 - (array[1] + array[2] + array[3]) * 100 / array[0];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return useRatio + "";
	}

	/**
	 * 获取CPU使用率
	 */
	public static String readUsage() {
		long useratio = 0L;
		long[] array = new long[2];
		long[] array1 = new long[2];
		array = getArray();
		long total1 = array[0];
		long idle1 = array[1];
		SystemClock.sleep(1000);
		array1 = getArray();
		long total2 = array1[0];
		long idle2 = array1[1];
		long temp = total2 - total1 + idle2 - idle1;
		if (temp != 0) {
			useratio = (total2 - total1) * 100L / temp;
		}
		return useratio + "";
	}

	private static long[] getArray() {
		long[] array = new long[2];
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			String[] tokens = load.split(" ");
			long total = Long.parseLong(tokens[2]) + Long.parseLong(tokens[3]) + Long.parseLong(tokens[4]);
			long idle = Long.parseLong(tokens[5]);
			array[0] = total;
			array[1] = idle;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * 获取磁盘使用空间
	 * 
	 * @return
	 */
	public static int getDiskSpace() {
		int useRatio = 0;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			useRatio = (int) (100 - availCount * 100 / blockCount);
		}
		return useRatio;
	}

	public static String getMac() {
		String result = callCmd("busybox ifconfig", "HWaddr");
		// 如果获得的结果为null，则返回error
		if (result == null) {
			return "error";
		}
		// 将物理地址格式化，例如 00-16-E8-3E-DF-67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			String Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
			if (Mac.length() > 1) {
				Mac = Mac.replaceAll(" ", "");
				result = "";
				String[] tmp = Mac.split(":");
				for (int i = 0; i < tmp.length; ++i) {
					if (i < tmp.length - 1) {
						result += tmp[i] + "-";
					} else {
						result += tmp[i];
					}
				}
			}
		}
		return result;
		// return "66-55-44-33-22-11";// 展厅竖屏mac
		// return "22-11-44-33-66-55";// littleEasy
		// return "33-22-11-66-55-44"; // 展厅85寸竖屏
		// return "11-33-55-22-44-66";// 测试组测试终端：android互动01
	}

	private static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// 读取输入流
			while ((line = br.readLine()) != null && line.contains(filter) == false) {
			}

			result = line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
