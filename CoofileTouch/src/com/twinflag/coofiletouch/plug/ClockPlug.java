package com.twinflag.coofiletouch.plug;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextClock;

import com.twinflag.coofiletouch.entity.ClockBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.entity.TimepieceBean;

public class ClockPlug extends TextClock implements BasePlug {

	public ClockPlug(Context context) {
		super(context);
	}

	@Override
	public void load(PlugBean plug) {
		TimepieceBean timepiece = plug.getTimepiece();
		ClockBean clock = timepiece.getClock();
		
		this.setFormat24Hour(clock.getFormat() == null ? "yyyy-MM-dd k:mm:ss" : clock.getFormat().replace("HH", "k"));
		this.setTextColor(Color.parseColor(clock.getFgcolor() == null ? "0xFF000000" : clock.getFgcolor()));
		if(clock.getBgcolor() != null){
			this.setBackgroundColor(Color.parseColor(clock.getBgcolor()));
		}
		this.setTextSize(clock.getSize() == 0 ? 15 : clock.getSize());
		// font
		
	}

	@Override
	public void destroy() {
		this.destroyDrawingCache();
	}

}
