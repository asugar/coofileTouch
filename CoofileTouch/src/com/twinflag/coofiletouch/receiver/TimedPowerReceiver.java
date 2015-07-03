package com.twinflag.coofiletouch.receiver;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.twinflag.coofiletouch.utils.TimeUtil;

public class TimedPowerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("power_time", Context.MODE_PRIVATE);
		String power_off_time = sp.getString("power_off_time", null);
		if (power_off_time == null) {
			return;
		}
		String nowTime = TimeUtil.getTranslateHM(Calendar.getInstance().getTimeInMillis());
		if (power_off_time.compareTo(nowTime) > 0) {
			long timed = TimeUtil.getTimeStringHM2Long(power_off_time) - TimeUtil.getTimeStringHM2Long(nowTime);
			executePowerOff(context, timed);

		} else {
			// 定时 + 1天
			long timed = TimeUtil.getTimeStringHM2Long(power_off_time) + 24 * 60 * 60 * 1000
							- TimeUtil.getTimeStringHM2Long(nowTime);
			executePowerOff(context, timed);
		}
	}

	private void executePowerOff(Context context, long time) {
		Intent intent_delayOff = new Intent("wits.com.simahuan.shutdown");
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent_delayOff, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, pi);
	}

}
