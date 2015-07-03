package com.twinflag.coofiletouch.playLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.socket.ClientStatus;
import com.twinflag.coofiletouch.value.Constant;

public class SendLogToFtp {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private boolean isStop = false;
	private Upload mUpload;

	public SendLogToFtp() {
		mUpload = new Upload();
	}

	public void startSendLogToFtp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					try {
						File logFileFolder = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
										+ Constant.APP_RES_LOGFILE_FOLDER);
						if (!logFileFolder.exists()) {
							logFileFolder.mkdirs();
							Thread.sleep(Constant.UPLOAD_PLAY_LOG_PERIOD);
							continue;
						}
						File[] logFiles = logFileFolder.listFiles();
						if (logFiles.length == 0) {
							Thread.sleep(Constant.UPLOAD_PLAY_LOG_PERIOD);
							continue;
						}
						String todayDate = sdf.format(new Date());
						for (File logFile : logFiles) {
							String logName = logFile.getName();
							if (!logName.contains(todayDate)) {
								boolean isSend = mUpload.uploadLog(logFile.getName());
								if (isSend) {
									delFile(logFile);
								} else {
									Thread.sleep(Constant.UPLOAD_PLAY_LOG_PERIOD);
								}
							}
						}
						Thread.sleep(Constant.UPLOAD_PLAY_LOG_PERIOD);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void stopThread() {
		this.isStop = true;
		if (mUpload != null) {
			mUpload = null;
		}
	}

	/**
	 * 删除本地的日志文件
	 */
	private void delFile(File logFile) {
		if (logFile != null && logFile.exists()) {
			logFile.delete();
		}
	}

	/**
	 * 获得今天的日期
	 */
	private String getDat() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = calendar.getTime();
		String data1 = sdf.format(date);
		String mac = ClientStatus.getMac();
		return data1 + "@" + mac + ".txt";
	}

}
