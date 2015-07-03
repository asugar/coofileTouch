package com.twinflag.coofiletouch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.twinflag.coofiletouch.utils.DeviceUtil;

/**
 * @author wanghongbin 暂时没有使用该类，用于复杂的授权检验
 */
public class AuthorityChecking extends Activity {

	private static final String TAG = "AuthorityChecking";
	private final int MSG_GET_LINCENSE_FROM_INTERNET = 120131;
	private final long AUTHORITY_CHECK_PERIOD_IN_MILLIS = 1000 * 60 * 60 * 24 * 7L;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_GET_LINCENSE_FROM_INTERNET) {
				String receivedValue = null;
				String licenseInfo = null;
				receivedValue = (String) msg.obj;
				if (receivedValue != null) {
					try {
						JSONObject json = new JSONObject(receivedValue);
						Log.i(TAG, "handleMessage, receivedValue = " + receivedValue);
						String reslut = json.optString("result", "ok");
						if (reslut.equals("ok")) {
							licenseInfo = json.optString("code", "");
							Log.i(TAG, "handleMessage, licenseInfo = " + licenseInfo);
							if (!licenseInfo.equals("")) {
								AuthorityUtil.getInstance().updateAuthorityFileContent(licenseInfo);
								if (!AuthorityUtil.getInstance().isAuthorityExpired()) {
									// 保存上次更新时间
									long currentMillis = System.currentTimeMillis();
									SharedPreferences mSharedPreferences = getSharedPreferences("checkTime",
													Context.MODE_PRIVATE);
									mSharedPreferences.edit().putLong("lastCheckTime", currentMillis).commit();
									finish();
									Intent intent = new Intent(AuthorityChecking.this, MainActivity.class);
									startActivity(intent);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获取上次检测的时间
		SharedPreferences mSharedPreferences = getSharedPreferences("checkTime", Context.MODE_PRIVATE);
		long lastCheckTimeMillis = mSharedPreferences.getLong("lastCheckTime", 0);

		long currentMillis = System.currentTimeMillis();

		if (needFetchLincenseFromNet(currentMillis, lastCheckTimeMillis)
						|| AuthorityUtil.getInstance().isAuthorityExpired()) {
			fetchLicenseFromInternet();
		} else {
			Log.i(TAG, "license is valid !");
			finish();
			Intent intent = new Intent(AuthorityChecking.this, MainActivity.class);
			startActivity(intent);
		}
	}

	private void fetchLicenseFromInternet() {
		Log.i(TAG, "===fetchLicenseFromInternet !");
		new Thread() {
			public void run() {
				try {
					JSONObject json = new JSONObject();
					try {
						int readLength = 0;
						int sendSize = 0;
						json.put("command", "checkLicense");
						json.put("hardinfo", DeviceUtil.getDeviceInfo());

						Socket socket = new Socket("192.168.13.95", 60000);

						// 获得输出流
						OutputStream os = socket.getOutputStream();
						String jsonStr = json.toString();
						byte[] buffer = jsonStr.getBytes("UTF-8");
						sendSize = buffer.length;
						byte[] array = new byte[4];
						array[3] = (byte) (0xff & sendSize);
						array[2] = (byte) ((0xff00 & sendSize) >> 8);
						array[1] = (byte) ((0xff0000 & sendSize) >> 16);
						array[0] = (byte) (0xff000000 & sendSize >> 24);
						os.write(array);
						os.flush();
						os.write(buffer);
						os.flush();

						// 接收部分;
						InputStream inputStream = socket.getInputStream();
						byte[] length = new byte[4];
						byte[] temp = new byte[4];
						byte oneByte;
						try {
							inputStream.read(length);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						for (int i = 0; i < 4; i++) {
							temp[3 - i] = length[i];
						}
						for (int j = 0; j < 4; j++) {
							oneByte = temp[j];
							readLength += (oneByte & 0xFF) << (8 * j);
						}

						System.out.println(readLength + "字节的大小");

						if (readLength == 0) {
							System.out.println("readLength == 0");
							Message msg = Message.obtain();
							msg.what = MSG_GET_LINCENSE_FROM_INTERNET;
							msg.obj = null;
							mHandler.sendMessageDelayed(msg, 100);
						}
						// 如果信息的自己不等于0
						else {
							System.out.println("readLength == " + readLength);
							buffer = new byte[readLength];
							try {
								String receivedContent = null;
								inputStream.read(buffer, 0, readLength);
								receivedContent = new String(buffer, "UTF-8");

								Message msg = Message.obtain();
								msg.what = MSG_GET_LINCENSE_FROM_INTERNET;
								msg.obj = receivedContent;
								mHandler.sendMessageDelayed(msg, 100);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						socket.close();

					} catch (JSONException exception) {
						exception.printStackTrace();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	private boolean needFetchLincenseFromNet(long currentMillis, long lastCheckMillis) {
		boolean retValue = false;
		if (!AuthorityUtil.getInstance().isAuthorityFileExist()
						|| ((currentMillis - lastCheckMillis) >= AUTHORITY_CHECK_PERIOD_IN_MILLIS)) {
			retValue = true;
		}
		return retValue;
	}

}
