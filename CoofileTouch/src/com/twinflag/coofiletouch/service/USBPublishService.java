package com.twinflag.coofiletouch.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.twinflag.coofiletouch.utils.FileUtil;

public class USBPublishService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 空指针异常
		if (intent != null) {
			Toast.makeText(getApplicationContext(), "USB发布开始执行！！！", Toast.LENGTH_LONG).show();
			final String rootPath = intent.getStringExtra("path");
			new Thread() {
				public void run() {
					FileUtil fileUtils = new FileUtil(USBPublishService.this);
					fileUtils.copyFiles(rootPath);
				};
			}.start();
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		stopSelf();
		super.onDestroy();
	}

}
