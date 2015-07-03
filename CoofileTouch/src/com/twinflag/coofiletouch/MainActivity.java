package com.twinflag.coofiletouch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.twinflag.coofiletouch.database.MyDbHelper;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.filter.FilterThread;
import com.twinflag.coofiletouch.parse.ParseMsg;
import com.twinflag.coofiletouch.parse.ParseProgram;
import com.twinflag.coofiletouch.playLog.SendLogToFtp;
import com.twinflag.coofiletouch.receiver.USBPublishReceiver;
import com.twinflag.coofiletouch.show.ShowMsg;
import com.twinflag.coofiletouch.show.ShowProgram;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.socket.Client;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.utils.ConfigUtil;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalValue;
import com.twinflag.coofiletouch.value.GlobalVariable;
import com.twinflag.coofiletouch.value.MessageValue;
import com.twinflag.coofiletouch.value.ReceiverAction;

/**
 * @author wanghuayi 主界面
 */
public class MainActivity extends Activity {

	public static Client client;
	private FilterThread filterThread;
	private ShowMsg mShowMsg;
	private SendLogToFtp mSendLogToFtp;
	private USBPublishReceiver mUSBPublishReceiver;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MessageValue.START_PROGRAM:
//					GlobalVariable.globalShowProgram.startPlay();
					break;
				case MessageValue.STOP_PROGRAM:
					GlobalVariable.globalShowProgram.stopPlay();
					break;
				case MessageValue.SWITCH_PROGRAM:
					GlobalVariable.globalShowProgram.swithProgram();
					break;
				case MessageValue.START_MESSAGE:
					GlobalVariable.globalShowMsg.startPlayMsg();
					break;
				case MessageValue.STOP_MESSAGE:
					GlobalVariable.globalShowMsg.stopPlayMsg();
					break;
				case MessageValue.SWITCH_MESSAGE:
					GlobalVariable.globalShowMsg.switchMsg();
					break;
				case MessageValue.DOWNLOAD_TASK_ERROR:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
					break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		generateConfigFolder();
		generateAuthorityFolder();
		generateWeatherFolder();
		GlobalVariable.globalShowProgram = new ShowProgram(this);
		// 正常执行
		setContentView(GlobalVariable.globalShowProgram);

		// 测试photoView
		// PhotoView image = new PhotoView(this);
		// image.setImageBitmap(BitmapFactory.decodeFile("mnt/sdcard/image/img1.jpg"));
		// image.setScaleType(ScaleType.FIT_XY);
		// setContentView(image);

		// 测试pulg
		// FlipPlug plug = new FlipPlug(this);
		// plug.load(TestPlug.getData());
		// setContentView(plug);

		// 测试天气插件
		// testWeather();
		
		// 测试数据
		getProgramData();

		if (Constant.IS_SUPPORT_AUTHORIZED_INSTALLATION) {
			AuthorityUtil authority = AuthorityUtil.getInstance();
			if (authority.isAuthorityFileExist()) {
				if (authority.isAuthorityExpired()) {
					LogUtil.showToastLongTime(getApplicationContext(), "授权已过期，请重新获取授权");
					return;
				}
			} else {
				// 授权文件不存在
				LogUtil.showToastLongTime(getApplicationContext(), "授权文件不存在，请获取授权文件");
				return;
			}
		}
		// ////////// 授权后使用的功能；
		copyWeatherPics();
		initDeviceDisplay();

		// 获取配置文件信息
//		GlobalValue.options = ConfigUtil.getInstance().getOptions();
//		if (!TextUtils.isEmpty(GlobalValue.errorMsg)) {
//			LogUtil.showToastLongTime(getApplicationContext(), GlobalValue.errorMsg);
//			return;
//		}

//		GlobalVariable.globalShowMsg = new ShowMsg(this);

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// try {
		//
		// for (int i = 1; i < 5; i++) {
		// MessageBean msg = getMsgData();
		// msg.setId("msgid" + i);
		// msg.setPosition(i);
		// GlobalVariable.messageAdd(msg);
		// }
		// getProgramData();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();

		client = new Client(getApplicationContext(), handler);
//		client.start();
		filterThread = new FilterThread(handler, this);
//		filterThread.start();

		mSendLogToFtp = new SendLogToFtp();
//		mSendLogToFtp.startSendLogToFtp();

		registerReceiver();
	}

	@Override
	protected void onDestroy() {
		if (mUSBPublishReceiver != null) {
			unregisterReceiver(mUSBPublishReceiver);
		}
		if (client != null) {
			client.stopClient();
			client = null;
		}
		if (filterThread != null) {
			filterThread.stopFilterThread();
			filterThread = null;
		}

		MyDbHelper.close();
		GlobalVariable.globalShowProgram.destroyProgram();
		GlobalVariable.globalShowProgram = null;
		if (mShowMsg != null) {
			mShowMsg.destroyMsgView();
		}
		if (mSendLogToFtp != null) {
			mSendLogToFtp.stopThread();
			mSendLogToFtp = null;
		}
		super.onDestroy();
	}

	@Override
	public void onDetachedFromWindow() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDetachedFromWindow();
	}

	private void getProgramData() {
		try {
			InputStream is = getAssets().open("demo.xml");
			byte[] buffer = new byte[1024];
			int length = 0;
			StringBuffer sb = new StringBuffer();
			while ((length = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, length));
			}
			GlobalVariable.globalTwinflag = ParseProgram.getProgram(sb.toString());
//			GlobalVariable.pgAdd(program);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MessageBean getMsgData() {
		MessageBean msg = null;
		try {
			InputStream is = getAssets().open("testMsg.xml");
			byte[] buffer = new byte[1024];
			int length = 0;
			StringBuffer sb = new StringBuffer();
			while ((length = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, length));
			}
			msg = ParseMsg.getMessage(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private void testWeather() {
		try {
			InputStream is = getAssets().open("testWeather.xml");
			byte[] buffer = new byte[1024];
			int length = 0;
			StringBuffer sb = new StringBuffer();
			while ((length = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, length));
			}
//			ProgramBean program = ParseProgram.getProgram(sb.toString());
//			GlobalVariable.pgAdd(program);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 注册usb发布的receiver
	private void registerReceiver() {
		IntentFilter fiter = new IntentFilter();
		fiter.addAction(ReceiverAction.USB_MOUNTED_ACTION);
		fiter.addAction(ReceiverAction.USB_REMOVED_ACTION);
		fiter.addDataScheme("file");
		mUSBPublishReceiver = new USBPublishReceiver();
		registerReceiver(mUSBPublishReceiver, fiter);
	}

	// 获取设备宽高
	private void initDeviceDisplay() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		CoofileTouchApplication.setScreenHeight(metric.heightPixels);
		CoofileTouchApplication.setScreenWidth(metric.widthPixels);
	}

	// 生成配置文件夹
	private void generateConfigFolder() {
		File configFolder = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_CONFIGFILE_FOLDER);
		if (!configFolder.exists()) {
			configFolder.mkdirs();
		}
	}

	// 生成授权文件夹
	private void generateAuthorityFolder() {
		File authorityFile = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_AUTHORITY_FOLDER);
		if (!authorityFile.exists()) {
			authorityFile.mkdir();
		}
	}

	// 生成天气图片文件夹
	private void generateWeatherFolder() {
		File file = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_WEATHERIMAGE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	// 复制天气图片
	private void copyWeatherPics() {
		new Thread() {
			public void run() {
				try {
					String[] picNames = getResources().getAssets().list(Constant.APP_RES_WEATHERIMAGE_FOLDER);
					for (String picName : picNames) {
						File desFile = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
										+ Constant.APP_RES_WEATHERIMAGE_FOLDER + File.separator + picName);
						if (!desFile.exists()) {
							InputStream in = getAssets().open(
											Constant.APP_RES_WEATHERIMAGE_FOLDER + File.separator + picName);
							OutputStream out = new FileOutputStream(desFile);
							int len = 0;
							byte[] buffer = new byte[1024];
							while ((len = in.read(buffer)) != -1) {
								out.write(buffer, 0, len);
							}
							out.close();
							in.close();
						}
					}
				} catch (Exception e) {
					LogUtil.printApointedLog(MainActivity.class, "天气图片复制失败！", LogType.error);
					e.printStackTrace();
				}
			};
		}.start();
	}

}
