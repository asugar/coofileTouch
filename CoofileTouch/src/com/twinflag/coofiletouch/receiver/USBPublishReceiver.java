package com.twinflag.coofiletouch.receiver;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.twinflag.coofiletouch.service.USBPublishService;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.ReceiverAction;

public class USBPublishReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ReceiverAction.USB_MOUNTED_ACTION.equals(action)) {

			Uri uri = intent.getData();

			String path = uri.getPath();
			File usbFile = new File(path);
			if (!usbFile.exists() || !usbFile.canRead()) {
				Toast.makeText(context, "USB不可用：" + path + "路径不存在，或者USB不可读", Toast.LENGTH_LONG).show();
				return;
			}

			final String rootPath = path + Constant.USB_PUBLISH_ROOT_DIRECTORY;
			File rootFolder = new File(rootPath);
			if (!rootFolder.exists() || !rootFolder.canRead()) {
				return;
			}

			Toast.makeText(context, "USB发布已经连接", Toast.LENGTH_LONG).show();
			Intent it = new Intent(context, USBPublishService.class);
			it.putExtra("path", rootPath);
			context.startService(it);

		} else if (ReceiverAction.USB_REMOVED_ACTION.equals(action)) {
			Toast.makeText(context, "USB发布已经移除", Toast.LENGTH_LONG).show();
			context.stopService(intent);
		}

	}

}
