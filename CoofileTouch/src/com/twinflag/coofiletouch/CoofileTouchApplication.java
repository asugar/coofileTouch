package com.twinflag.coofiletouch;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings.Secure;

import com.twinflag.coofiletouch.value.Constant;

/**
 * @author wanghongbin
 * application类，存放全局变量
 */
public class CoofileTouchApplication extends Application {

	private static int mScreenWidth = 0;
	private static int mScreenHeight = 0;
	private static String mAppVersion = "1.0";
	private static String mAndroidID = "";
	private static String mAppResBasePath = null;

	@Override
	public void onCreate() {
		super.onCreate();

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			mAppResBasePath = Environment.getExternalStorageDirectory().getPath() + File.separator
							+ Constant.APP_RES_PARENT_FOLDER;
		}
		else {
			mAppResBasePath = "/mnt/sdcard" + File.separator + Constant.APP_RES_PARENT_FOLDER;
		}

		File baseFile = new File(mAppResBasePath);
		if (!baseFile.exists()) {
			baseFile.mkdirs();
		}

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		mAppVersion = getSoftVersion(this);
		mAndroidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}

	public static int getScreenWidth() {
		return mScreenWidth;
	}

	public static int getScreenHeight() {
		return mScreenHeight;
	}

	public static void setScreenWidth(int screenWidth) {
		mScreenWidth = screenWidth;
	}

	public static void setScreenHeight(int screenHeight) {
		mScreenHeight = screenHeight;
	}

	public static String getAppResBasePath() {
		return mAppResBasePath;
	}

	public static String getAndroidID() {
		return mAndroidID;
	}

	public static String getAppVersion() {
		return mAppVersion;
	}

	private String getSoftVersion(Context context) {
		String appVersion = "1.0";
		if (context != null) {
			try {
				PackageManager packageManager = context.getPackageManager();
				PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
				appVersion = packInfo.versionName;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return appVersion;
	}
}
