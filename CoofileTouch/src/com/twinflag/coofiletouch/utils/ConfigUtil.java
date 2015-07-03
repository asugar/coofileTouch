package com.twinflag.coofiletouch.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.ErrorValue;
import com.twinflag.coofiletouch.value.GlobalValue;
import com.twinflag.coofiletouch.value.OptionsBean;

final public class ConfigUtil {

	private static OptionsBean options;
	private static Properties properties;

	public static ConfigUtil self = null;

	private ConfigUtil() {
	}

	public static synchronized ConfigUtil getInstance() {
		if (self == null) {
			return new ConfigUtil();
		}
		return self;
	}

	public OptionsBean getOptions() {
		options = new OptionsBean();
		String basePath = CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_CONFIGFILE_FOLDER;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(basePath
							+ File.separator + Constant.APP_RES_CONFIGFILE)), "utf-8"));
			properties = new Properties();
			properties.load(in);

			options.server_ip = getStringConfig("server_ip");
			options.server_port = getIntegerConfig("server_port");
			options.ftp_ip = getStringConfig("ftp_ip");
			options.ftp_port = getIntegerConfig("ftp_port");
			options.ftp_username = getStringConfig("ftp_username");
			options.ftp_password = getStringConfig("ftp_password");
			options.weatherurl = getStringConfig("weatherurl");
			options.isencrypt = getBooleanConfig("isencrypt");
			checkRequired();
			in.close();
		}
		catch (IOException e) {
			GlobalValue.errorMsg = ErrorValue.ERROR_12;
		}
		catch (Exception e) {
			GlobalValue.errorMsg = ErrorValue.ERROR_13;
		}

		return options;
	}

	private String getStringConfig(String config) {
		return properties.get(config).toString().trim();
	}

	private Integer getIntegerConfig(String config) {
		String tem = getStringConfig(config);
		if (!isEmpty(tem)) {
			return Integer.parseInt(tem);
		}
		return null;

	}

	private Boolean getBooleanConfig(String config) {
		String tem = getStringConfig(config);
		if (!isEmpty(tem)) {
			return Boolean.parseBoolean(tem);
		}
		return null;
	}

	private boolean isEmpty(String config) {
		return TextUtils.isEmpty(config);
	}

	private boolean isIntegerEmpty(Integer tem) {
		if (tem == null) {
			return true;
		}
		return false;
	}

	/**
	 * 校验必填项配置，没有对字符串
	 */
	private void checkRequired() {
		if (isEmpty(options.server_ip)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_01;
		} else if (!checkIP(options.server_ip)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_02;
		} else if (isIntegerEmpty(options.server_port)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_03;
		} else if (isEmpty(options.ftp_ip)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_04;
		} else if (!checkIP(options.ftp_ip)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_05;
		} else if (isIntegerEmpty(options.ftp_port)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_06;
		} else if (isEmpty(options.ftp_username)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_07;
		} else if (isEmpty(options.ftp_password)) {
			GlobalValue.errorMsg = ErrorValue.ERROR_08;
		}
	}

	/**
	 * 校验非必填项配置
	 */
	// private static void checkNonRequired() {
	// }

	/**
	 * 校验是否是合法的IP
	 */
	private boolean checkIP(String config) {
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(config);
		return matcher.matches();
	}

}
