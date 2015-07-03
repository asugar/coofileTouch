package com.twinflag.coofiletouch.service;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

public class VolumService extends Service {

	AudioManager am;
	int maxVolume;

	@Override
	public void onCreate() {
		super.onCreate();
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		SystemClock.setCurrentTimeMillis((new Date()).getTime());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    if(intent != null)
	    {
	        Bundle b = intent.getExtras();
		    int value = ((Integer.parseInt(b.getString("value"))) * maxVolume) / 100;
		    if (value > maxVolume) {
			    value = maxVolume;
		    }
		    am.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
	    }
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
