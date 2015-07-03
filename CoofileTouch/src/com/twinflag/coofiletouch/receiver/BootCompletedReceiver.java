package com.twinflag.coofiletouch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twinflag.coofiletouch.MainActivity;
import com.twinflag.coofiletouch.value.ReceiverAction;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ReceiverAction.BOOT_COMPLETED_ACTION)) {
			Intent StartIntent = new Intent(context, MainActivity.class);
			StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(StartIntent);
		}
	}

}
