package com.twinflag.coofiletouch.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

final public class EffectsUtil {

	public static AnimationSet getAnim(int effect) {
		AnimationSet animationSet = new AnimationSet(false);
		Animation translate = null;
		switch (effect) {
		case 101:// 从左淡入
			translate = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
			break;
		case 102:// 从右淡入
			translate = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
			break;
		case 103:// 从上淡入
			translate = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
			break;
		case 104:// 从下淡入
			translate = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
			break;
		case 105:// 从左上角淡入
			translate = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
			break;
		case 106:// 从左下角淡入
			translate = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
			break;
		case 107:// 从右上角淡入
			translate = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
			break;
		case 8:// 从右下角淡入
			translate = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
			break;
		}

		if (translate != null) {
			animationSet.addAnimation(translate);
		}
		animationSet.addAnimation(new AlphaAnimation(0.2f, 1f));
		animationSet.setDuration(4000);
		return animationSet;
	}

}
