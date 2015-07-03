package com.twinflag.coofiletouch.show;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.entity.ButtonBean;
import com.twinflag.coofiletouch.playLog.ButtonLogBean;
import com.twinflag.coofiletouch.playLog.WriteLogToFile;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.ButtonType;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.value.GlobalVariable;

/**
 * 用于场景切换的button
 * 
 * @author wanghuayi
 */
public class ShowButton extends ShowBase {

	private ButtonBean mButtonBean;
	LoadButtonBgTask mLoadButtonBgTask;

	public ShowButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowButton(Context context, ButtonBean button) {
		super(context);
		this.mButtonBean = button;
		runBackGround();
	}

	private void runBackGround() {
		Button buttonView = new Button(getContext());
		mButtonBean.setView(buttonView);
		buttonView.setAlpha(0.0f);
		try {
			String url = mButtonBean.getBackImg();
			if (url != null) {
				buttonView.setAlpha(1.0f);
				mLoadButtonBgTask = new LoadButtonBgTask();
				mLoadButtonBgTask.execute(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.printApointedLog(ShowButton.class, "按钮背景不可用", LogType.error);
		}

	}

	private class LoadButtonBgTask extends AsyncTask<String, Void, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable background = Drawable.createFromPath(CoofileTouchApplication.getAppResBasePath() + params[0]);
			return background;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			Button buttonView = (Button) mButtonBean.getView();
			if (buttonView != null) {
				buttonView.setBackground(result);
			} else {
				LogUtil.printApointedLog(ShowButton.class, "LoadButtonBgTask mButtonBean's buttonView is null",
								LogType.error);
			}
		}

	}

	// 添加到界面的方法
	@Override
	public void startShow() {

		View view = mButtonBean.getView();
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		addView(mButtonBean.getView());
		initListener();
		// 在这添加日志

	}

	@Override
	public void stopShow() {
		View view = mButtonBean.getView();
		if (view != null) {
			// 在这添加日志
			view.setEnabled(false);
		}
	}

	@Override
	public void destroyShow() {
		if (mLoadButtonBgTask != null) {
			mLoadButtonBgTask.cancel(true);
			mLoadButtonBgTask = null;
		}
		View view = mButtonBean.getView();
		Boolean isHaveBackImg = false;
		if (view != null) {
			this.removeView(view);
			if (mButtonBean.getBackImg() != null) {
				isHaveBackImg = true;
			}
			DestroyUtil.destroyButton(view, isHaveBackImg);
			view = null;
		}
		mButtonBean.setView(null);
	}

	// 初始化按钮监听事件
	private void initListener() {
		final Button buttonView = (Button) mButtonBean.getView();
		if (buttonView == null) {
			return;
		}
		ButtonType buttonType = mButtonBean.getType();
		switch (buttonType) {
			case 切换:
				buttonView.setOnTouchListener(new SwitchSceneListener());
				break;
			case 返回:
				buttonView.setOnTouchListener(new SwitchLastSceneListener());
				break;
			case 主场景:
				buttonView.setOnTouchListener(new SwitchMainSceneListener());
				break;
			case 未定义:
				break;
		}

	}

	// type=1 场景切换
	private class SwitchSceneListener implements OnTouchListener {
		long time1 = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_UP:

					if (event.getEventTime() - time1 < 50) {
						Log.i("yi", "this is my want");
					}
					time1 = event.getEventTime();

					String _ButtonID = mButtonBean.getSceneid();
					if (_ButtonID == null || _ButtonID == "") {
						LogUtil.printApointedLog(ShowButton.class, "ShowButton中_ButtonID不可用！！！", LogType.error);
						return false;
					}
					DestroyUtil.setAllButtonUnusable(v);
					Log.i("yi", "setAllButtonUnusable " + _ButtonID);
					if (!GlobalVariable.globalShowProgram.switchScene(_ButtonID)) {
						DestroyUtil.setAllButtonUsable(v);
						Log.i("yi", "setAllButtonUsable");
					} else {
						WriteLogToFile.writeButtonLog(new ButtonLogBean(GlobalVariable.globalProgram,
										GlobalVariable.globalShowProgram.getCurrentScene(), mButtonBean));
					}
					break;
			}
			return false;
		}

	}

	// type=2 上一个场景
	private class SwitchLastSceneListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					DestroyUtil.setAllButtonUnusable(v);
					if (!GlobalVariable.globalShowProgram.switchLastScene()) {
						DestroyUtil.setAllButtonUsable(v);
					} else {
						WriteLogToFile.writeButtonLog(new ButtonLogBean(GlobalVariable.globalProgram,
										GlobalVariable.globalShowProgram.getCurrentScene(), mButtonBean));
					}
					break;
			}
			return true;
		}

	}

	// type=3 主场景
	private class SwitchMainSceneListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					DestroyUtil.setAllButtonUnusable(v);
					if (!GlobalVariable.globalShowProgram.switchMainScene()) {
						DestroyUtil.setAllButtonUsable(v);
					} else {
						WriteLogToFile.writeButtonLog(new ButtonLogBean(GlobalVariable.globalProgram,
										GlobalVariable.globalShowProgram.getCurrentScene(), mButtonBean));
					}
					break;
			}
			return true;
		}
	}

}
