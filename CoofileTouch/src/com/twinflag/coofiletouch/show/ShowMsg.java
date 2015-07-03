package com.twinflag.coofiletouch.show;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.showUtil.GetNextUtil;
import com.twinflag.coofiletouch.type.MsgType;
import com.twinflag.coofiletouch.view.MyScrollMsgView;

public class ShowMsg {

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mParams;

	private Context mContext;
	private MessageBean mCurrentMsg;
	private MyScrollMsgView mMsgView;

	public ShowMsg(Context context) {
		this.mContext = context;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0x123:
					msgChange();
					break;
			}
		};
	};

	private void initVariable() {
		getWindowManager();
		getParams();

		mCurrentMsg = GetNextUtil.getNextMessage(mCurrentMsg);
		mMsgView = new MyScrollMsgView(mContext, mHandler);
		mMsgView.setBackgroundColor(Color.GRAY);
		// mMsgView.setAlpha(0.5f);
		initParams(mCurrentMsg.getPosition());
		mMsgView.initVariable(mCurrentMsg);
		mWindowManager.addView(mMsgView, mParams);

	}

	public void msgChange() {
		mCurrentMsg = GetNextUtil.getNextMessage(mCurrentMsg);
		initParams(mCurrentMsg.getPosition());
		mWindowManager.updateViewLayout(mMsgView, mParams);
		mMsgView.initVariable(mCurrentMsg);
	}

	// 获取windowManager
	private void getWindowManager() {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		}
	}

	// 获取params
	private void getParams() {
		if (mParams == null) {
			mParams = new WindowManager.LayoutParams();
			mParams.gravity = Gravity.LEFT + Gravity.TOP;
			mParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
							| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			mParams.format = PixelFormat.TRANSLUCENT;
			mParams.type = WindowManager.LayoutParams.TYPE_TOAST;// TYPE_PHONE
			mParams.alpha = 0.0f;
		}
	}

	// 根据position初始化params
	private void initParams(MsgType type) {
		switch (type) {
			case 上边:
				mParams.width = CoofileTouchApplication.getScreenWidth();
				mParams.height = 150;
				mParams.x = 0;
				mParams.y = 0;
				break;
			case 下边:
				mParams.width = CoofileTouchApplication.getScreenWidth();
				mParams.height = 150;
				mParams.x = 0;
				mParams.y = CoofileTouchApplication.getScreenHeight() - 150;
				break;
			case 左边:
				mParams.width = 100;
				mParams.height = CoofileTouchApplication.getScreenHeight();
				mParams.x = 0;
				mParams.y = 0;
				break;
			case 右边:
				mParams.width = 100;
				mParams.height = CoofileTouchApplication.getScreenHeight();
				mParams.x = CoofileTouchApplication.getScreenWidth() - 100;
				mParams.y = 0;
				break;
		}
	}

	// 获取当前播放的消息
	public MessageBean getCurrentMsg() {
		return mCurrentMsg;
	}

	// 开启滚动消息
	public void startPlayMsg() {
		initVariable();
	}

	// 暂时停止播放滚动消息;
	public void stopPlayMsg() {
		if (mMsgView != null) {
			mMsgView.pauseMsg();

			if (mWindowManager != null) {
				mWindowManager.removeView(mMsgView);
				mWindowManager = null;
			}

			mMsgView.destroyDrawingCache();
			mMsgView = null;
		}

		if (mParams != null) {
			mParams = null;
		}
	}

	// 继续播放滚动消息
	public void switchMsg() {
		stopPlayMsg();
		startPlayMsg();
	}

	// 彻底销毁滚动消息
	public void destroyMsgView() {
		stopPlayMsg();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}

}
