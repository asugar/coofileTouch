package com.twinflag.coofiletouch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

import com.twinflag.coofiletouch.utils.DES;
import com.twinflag.coofiletouch.utils.DeviceUtil;
import com.twinflag.coofiletouch.value.Constant;

/**
 * @author wanghongbin 简单的授权检验
 */
public class AuthorityUtil {

	private static final int LICENSE_DECODE_KEY_LEN = 8;
	private static final int DEVICE_INFO_CONTENT_LEN = 24;
	private static AuthorityUtil self = null;

	private AuthorityUtil() {
	}

	public static synchronized AuthorityUtil getInstance() {
		if (self == null) {
			self = new AuthorityUtil();
		}
		return self;
	}

	public boolean isAuthorityExpired() {
		boolean retValue = true;
		if (DeviceUtil.isSdCardExist()) {
			String deviceInfo = null;
			String licenseFileContent = null;
			String decodedContent = null;
			String validContent = null;
			String dateInfo = null;
			String deviceInfoTmp = null;
			String decodeKey = null;
			String lincenseFileFullName = null;
			String lincenseFileName = null;
			deviceInfo = DeviceUtil.getDeviceInfo();
			lincenseFileName = deviceInfo + ".license";
			// deviceInfo = "6523-8769-2234-85746873";
			// lincenseFileName = "6523-8769-2234-85746873.license";
			lincenseFileFullName = CoofileTouchApplication.getAppResBasePath() + File.separator
							+ Constant.APP_RES_AUTHORITY_FOLDER + File.separator + lincenseFileName;
			deviceInfoTmp = deviceInfo.replace("-", "");
			if (deviceInfoTmp.length() >= LICENSE_DECODE_KEY_LEN)
				decodeKey = deviceInfoTmp.substring(deviceInfoTmp.length() - LICENSE_DECODE_KEY_LEN,
								deviceInfoTmp.length());
			File authFile = new File(lincenseFileFullName);
			if (authFile.exists()) {
				try {
					FileInputStream fin = new FileInputStream(authFile);
					int length = fin.available();
					byte[] buffer = new byte[length];
					fin.read(buffer);
					licenseFileContent = EncodingUtils.getString(buffer, "UTF-8");
					fin.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (licenseFileContent != null) {
					try {
						decodedContent = DES.decryptDES(licenseFileContent, decodeKey);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (decodedContent.length() >= DEVICE_INFO_CONTENT_LEN) {
						validContent = decodedContent.substring(0, 10)
										+ decodedContent.substring(17, decodedContent.length());
						dateInfo = decodedContent.substring(10, 16);
						if (validContent.equalsIgnoreCase(deviceInfo) && isLaterThanToday(dateInfo)) {
							retValue = false;
						}
					}
				}
			}

		}
		return retValue;
	}

	public boolean isAuthorityFileExist() {
		boolean retValue = false;
		if (DeviceUtil.isSdCardExist()) {
			File storageDir = null;
			File authorityFile = null;
			String authorityFilePath = null;
			String lincenseFileFullName = null;
			String lincenseFileName = null;
			storageDir = Environment.getExternalStorageDirectory();
			lincenseFileName = DeviceUtil.getDeviceInfo() + ".license";
			// lincenseFileName = "6523-8769-2234-85746873.license";
			lincenseFileFullName = storageDir.getPath() + File.separator + "twinflag" + File.separator
							+ lincenseFileName;
			authorityFilePath = storageDir.getPath() + File.separator + "twinflag";
			authorityFile = new File(authorityFilePath);
			if (!authorityFile.exists()) {
				authorityFile.mkdir();
			}
			File authFile = new File(lincenseFileFullName);
			if (authFile.exists()) {
				retValue = true;
			}
		}
		return retValue;
	}

	public void updateAuthorityFileContent(String content) {
		if (DeviceUtil.isSdCardExist() && content != null) {
			boolean createAuthFileSuccesss = false;
			File storageDir = null;
			File authorityFile = null;
			String authorityFilePath = null;
			String lincenseFileFullName = null;
			String lincenseFileName = null;
			storageDir = Environment.getExternalStorageDirectory();
			lincenseFileName = DeviceUtil.getDeviceInfo() + ".license";
			// lincenseFileName = "6523-8769-2234-85746873.license";
			lincenseFileFullName = storageDir.getPath() + File.separator + "twinflag" + File.separator
							+ lincenseFileName;
			authorityFilePath = storageDir.getPath() + File.separator + "twinflag";
			authorityFile = new File(authorityFilePath);
			if (!authorityFile.exists()) {
				authorityFile.mkdir();
			}
			File authFile = new File(lincenseFileFullName);
			if (authFile.exists()) {
				authFile.delete();
			}
			authFile = new File(lincenseFileFullName);
			if (!authFile.exists()) {
				try {
					authFile.createNewFile();
					createAuthFileSuccesss = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				createAuthFileSuccesss = true;
			}
			if (createAuthFileSuccesss) {
				try {
					byte[] bytes = content.getBytes();
					FileOutputStream fileOutputStream = new FileOutputStream(authFile);
					fileOutputStream.write(bytes);
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isLaterThanToday(String date) {
		boolean retValue = false;
		if (date != null) {
			int dayDif = 0;
			int year = 0;
			int month = 0;
			int day = 0;
			String monthStr = null;
			String dayStr = null;
			Date toCompareDate = null;
			Date currentDate = null;
			Calendar calendar = null;
			String toCompareDateString = null;
			String currentDateString = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			toCompareDateString = "20" + date;
			try {
				toCompareDate = simpleDateFormat.parse(toCompareDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 生成当前时间
			calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			if (month < 10) {
				monthStr = "0" + month;
			} else {
				monthStr = "" + month;
			}
			day = calendar.get(Calendar.DAY_OF_MONTH);
			if (day < 10) {
				dayStr = "0" + day;
			} else {
				dayStr = "" + day;
			}
			currentDateString = "" + year + monthStr + dayStr;
			try {
				currentDate = simpleDateFormat.parse(currentDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GregorianCalendar cal1 = new GregorianCalendar();
			GregorianCalendar cal2 = new GregorianCalendar();
			cal1.setTime(toCompareDate);
			cal2.setTime(currentDate);
			dayDif = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24));
			if (dayDif <= 0) {
				retValue = true;
			}
		}
		return retValue;
	}

}
