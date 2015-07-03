package com.twinflag.coofiletouch.showUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.twinflag.coofiletouch.type.LogType;

public class LogUtil {

	private static final int DEVELOP = 0;// 开发阶段
	private static final int DEBUG = 1;// 内部测试阶段
	private static final int BATE = 2;// 公开测试
	private static final int RELEASE = 3;// 正式版
	private static final String SAME_TAG = "yi";
	private static int currentStage = DEVELOP;// 当前阶段
	private static int TOAST_LONG_TIME = 5000;// 吐司长时间
	private static int TOAST_SHORT_TIME = 500;// 吐司短时间

	@SuppressWarnings("rawtypes")
	public static void printLog(Class clazz, String msg) {
		if (clazz != null && !TextUtils.isEmpty(msg)) {
			switch (currentStage) {
				case DEVELOP:
					Log.i(clazz.getSimpleName(), msg);
					break;
				case DEBUG:
					break;
				case BATE:
					break;
				case RELEASE:
					break;
			}
		}
	}

	public static void printSameLog(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			Log.i(SAME_TAG, msg);
		}
	}

	public static void printApointedLog(Class<?> clazz, String msg, LogType type) {
		if (clazz != null && !TextUtils.isEmpty(msg)) {
			switch (type) {
				case info:
					Log.i(clazz.getSimpleName(), msg);
					break;
				case error:
					Log.e(clazz.getSimpleName(), msg);
					break;
			}
		}
	}

	/**
	 * 长时间显示吐司
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastShortTime(Context context, String msg) {
		Toast.makeText(context, msg, TOAST_LONG_TIME).show();
	}

	/**
	 * 短时间显示吐司
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastLongTime(Context context, String msg) {
		Toast.makeText(context, msg, TOAST_SHORT_TIME).show();
	}

}
