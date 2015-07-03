package com.twinflag.coofiletouch.playLog;

import java.io.File;
import java.io.IOException;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.ftp.UploadHelper;
import com.twinflag.coofiletouch.value.Constant;

public class Upload {

	public boolean uploadLog(String path) {
		boolean uploadResult = false;
		UploadHelper ftp = UploadHelper.newInstance();
		try {
			ftp.openConnect();
			String basepath = CoofileTouchApplication.getAppResBasePath() + File.separator
							+ Constant.APP_RES_LOGFILE_FOLDER + File.separator + path;
			// 日志路径需要核实
			uploadResult = ftp.uploadLogs(basepath, Constant.LOG_FOLDER_IN_FTP + File.separator + path);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ftp != null) {
				ftp.closeConnect();
			}
		}
		return uploadResult;
	}

}
