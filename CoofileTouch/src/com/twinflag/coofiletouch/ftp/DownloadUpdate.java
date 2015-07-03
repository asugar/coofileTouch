package com.twinflag.coofiletouch.ftp;

import java.io.File;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.value.Constant;

public class DownloadUpdate {

	private String loadPath = "";

	public DownloadUpdate() {
		File file = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_UPDATE_APK_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (file.exists()) {
			this.loadPath = CoofileTouchApplication.getAppResBasePath() + File.separator
							+ Constant.APP_RES_UPDATE_APK_FOLDER;
		}
	}

	/**
	 * 下载最新版本软件补丁包
	 * */
	public DownloadStatus downLoadUpdatePak(String remotePath, String fileName) {
		return new Download().downloadFile(remotePath, fileName, loadPath);
	}

}
