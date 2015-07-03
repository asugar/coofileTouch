package com.twinflag.coofiletouch.playLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.socket.ClientStatus;
import com.twinflag.coofiletouch.value.Constant;

public class WriteLogToFile {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/**
	 * 记录区域元素播放日志
	 * 
	 * @param element
	 * @param scene
	 * @param program
	 */
	public static void writeRegionLog(final RegionLogBean regionLog) {
		new Thread() {
			public void run() {
				boolean needWriteHead = false;
				String date = sdf.format(new Date());
				String[] current = date.split(" ");
				String firstName = current[0];
				String mac = ClientStatus.getMac();

				File file = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
								+ Constant.APP_RES_LOGFILE_FOLDER);
				if (!file.exists()) {
					file.mkdirs();
				}
//				File logFile = new File(file, firstName + "@" + midName + "@" + "region" + ".txt");
				File logFile = new File(file, firstName + "@" + "nomal" + "@" + mac + ".txt");
				if (!logFile.exists()) {
					try {
						logFile.createNewFile();
						needWriteHead = true;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				StringBuffer sb = new StringBuffer();
				sb.append(regionLog.toString()).append("\r\n");
				try {
					FileOutputStream fos = new FileOutputStream(logFile, true);
					byte[] contentBytes = sb.toString().getBytes("Unicode");
					if (needWriteHead) {
						fos.write(contentBytes, 0, contentBytes.length);
					} else {
						fos.write(contentBytes, 2, contentBytes.length - 2);
					}
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 记录插件播放日志
	 * 
	 * @param element
	 * @param scene
	 * @param program
	 */
	public static void writePlugLog(final PlugLogBean plugLog) {
		new Thread() {
			public void run() {
				boolean needWriteHead = false;
				String date = sdf.format(new Date());
				String[] current = date.split(" ");
				String firstName = current[0];
				String mac = ClientStatus.getMac();
				File file = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
								+ Constant.APP_RES_LOGFILE_FOLDER);
				if (!file.exists()) {
					file.mkdirs();
				}
				File logFile = new File(file, firstName + "@" + mac + "@" + "plug" + ".txt");
				if (!logFile.exists()) {
					try {
						logFile.createNewFile();
						needWriteHead = true;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				StringBuffer sb = new StringBuffer();
				// 任务
				sb.append(plugLog.toString());
				try {
					FileOutputStream fos = new FileOutputStream(logFile, true);
					byte[] contentBytes = sb.toString().getBytes("Unicode");
					if (needWriteHead) {
						fos.write(contentBytes, 0, contentBytes.length);
					} else {
						fos.write(contentBytes, 2, contentBytes.length - 2);
					}
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * button点击日志
	 * 
	 * @param button
	 * @param scene
	 * @param program
	 */
	public static void writeButtonLog(final ButtonLogBean buttonLog) {
		new Thread() {
			public void run() {
				boolean needWriteHead = false;
				String date = sdf.format(new Date());
				String[] current = date.split(" ");
				String name = current[0];
				String mac = ClientStatus.getMac();
				File file = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
								+ Constant.APP_RES_LOGFILE_FOLDER);
				if (!file.exists()) {
					file.mkdirs();
				}
				File logFile = new File(file, name + "@" + "interaction" + "@" + mac + ".txt");
				if (!logFile.exists()) {
					try {
						logFile.createNewFile();
						needWriteHead = true;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				StringBuffer sb = new StringBuffer();
				// 任务
				sb.append(buttonLog.toString());
				try {
					FileOutputStream fos = new FileOutputStream(logFile, true);
					byte[] contentBytes = sb.toString().getBytes("Unicode");
					if (needWriteHead) {
						fos.write(contentBytes, 0, contentBytes.length);
					} else {
						fos.write(contentBytes, 2, contentBytes.length - 2);
					}
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 记录错误的log
	 * 
	 * @param context
	 * @param name
	 * @param type
	 */
	public static void writeErrorLog(final String context) {
		new Thread() {
			public void run() {
				boolean needWriteHead = false;
				String date = mSimpleDateFormat.format(new Date());
				File logFolder = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
								+ Constant.APP_RES_ERROR_LOGFILE_FOLDER);
				if (!logFolder.exists()) {
					logFolder.mkdirs();
				}

				File logFile = new File(logFolder, date + ".txt");
				if (!logFile.exists()) {
					try {
						logFile.createNewFile();
						needWriteHead = true;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				StringBuffer sb = new StringBuffer();
				sb.append("time: ").append(date).append(" ").append(context).append("\r\n");
				try {
					FileOutputStream fos = new FileOutputStream(logFile, true);
					byte[] contentBytes = sb.toString().getBytes("utf-8");
					if (needWriteHead) {
						fos.write(contentBytes, 0, contentBytes.length);
					} else {
						fos.write(contentBytes, 2, contentBytes.length - 2);
					}
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
