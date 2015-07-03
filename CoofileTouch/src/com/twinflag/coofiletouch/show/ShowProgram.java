package com.twinflag.coofiletouch.show;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.R;
import com.twinflag.coofiletouch.entity.ButtonBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.entity.RegionBean;
import com.twinflag.coofiletouch.entity.SceneBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.ButtonType;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalVariable;
import com.twinflag.coofiletouch.value.MessageValue;

public class ShowProgram extends FrameLayout {

	private SceneBean currentScene;// 当前的场景
	private SceneBean mainScene;// 主场景
	private SceneBean lastScene;// 将要销毁的上一个场景

	private Stack<String> scenePath = new Stack<String>(); // 记录场景跳转的路径，用于返回上一个场景使用

	private Map<String, SceneBean> sceneMap = new HashMap<String, SceneBean>();// 记录所有场景的引用

	private FrameLayout fl_home;// 定义场景背景
	// private ProgressBar mProgressBar;

	private Boolean isLoading = false;// 标记是否正在加载
	private LoadSceneBgTask mLoadSceneBgTask;// 场景背景加载

	public ShowProgram(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowProgram(Context context) {
		super(context);
		this.setBackgroundColor(getResources().getColor(android.R.color.white));
		fl_home = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.home, null);
		// mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		this.addView(fl_home, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	// 初始化场景列表
	private void initVariable() {
		if (GlobalVariable.globalProgram == null) {
			LogUtil.printApointedLog(ShowProgram.class, "initVariable时，全局变量globalProgram为空", LogType.error);
			return;
		}
		List<SceneBean> list = GlobalVariable.globalProgram.getScenes();
		if (list.size() == 0) {
			LogUtil.printApointedLog(ShowProgram.class, "initVariable时，getScenes为空", LogType.error);
			return;
		}
		for (SceneBean scene : list) {
			sceneMap.put(scene.getId(), scene);
		}
	}

	// 开启播放
	public void startPlay() {
		// mProgressBar.setVisibility(View.VISIBLE);
		initVariable();
		mainScene = currentScene = getMainScene();
		if (currentScene == null) {
			LogUtil.showToastShortTime(getContext(), "请检查节目单主场景是否设置！！！！！！");
			LogUtil.printApointedLog(ShowProgram.class, "startPlay时，未设置主场景！！！！！", LogType.error);
			return;
		}
		if (!isLoading) {
			loadScene();
		}

	}

	// 停止播放
	public void stopPlay() {
		isLoading = false;
		handler.removeCallbacksAndMessages(null);
		if (mLoadSceneBgTask != null) {
			mLoadSceneBgTask.cancel(true);
			mLoadSceneBgTask = null;
		}
		if (currentScene != null) {
			destroyScene(currentScene);
		}
		if (lastScene != null) {
			destroyScene(lastScene);
		}
		mainScene = currentScene = lastScene = null;
		scenePath.clear();
		sceneMap.clear();
		fl_home.setVisibility(View.VISIBLE);
	}

	// 切换任务播放
	public void swithProgram() {
		stopPlay();
		startPlay();
	}

	// 销毁播放，在mainActivity中调用的方法
	public void destroyProgram() {
		stopPlay();
		handler.removeCallbacksAndMessages(null);
		handler = null;
	}

	// 切换场景的方法
	public synchronized Boolean switchScene(String id) {
		Boolean flag = false;
		if (currentScene == null) {
			LogUtil.printSameLog("switchScene 中 currentScene为空！！");
			return false;
		}
		// 如果要切换的场景就是他自己，重置播放，重新开始播放
		if (currentScene.getId().equals(id)) {
			// 重新播放
			resetPlay();
			flag = false;
			// 如果是主场景，普通场景
		}
		// 如果要切换到别的场景，首先销毁目前的播放，将进度条界面显示，等全部加载完毕再一块显示
		else {
			currentScene = sceneMap.get(id);
			// 如果获取的场景为空，跳转到主场景并返回
			if (currentScene == null) {
				flag = false;
				LogUtil.showToastShortTime(getContext(), "switchScene时，当前播放场景为空！！！！！！");

				switchMainScene();
				return flag;
			}
			// 如果获取的场景不为空，存储当前场景路径
			if (!isLoading) {
				scenePath.push(currentScene.getId());
				loadScene();
				flag = true;
			}
		}
		return flag;
	}

	// 切换主场景的方法
	public boolean switchMainScene() {
		// currentScene为空
		Boolean flag = false;
		if (currentScene != null) {
			// 如果当前场景就是主场景，不用切换了F
			if (currentScene.getIsmain() || currentScene == mainScene) {
				// 重置播放(没有)
				resetPlay();
				// 如果不是主场景
			} else {
				// scenePath.clear();
				currentScene = mainScene;
				if (!isLoading) {
					scenePath.push(currentScene.getId());
					flag = true;
					loadScene();
				}
			}
			return flag;
		} else {
			LogUtil.printApointedLog(ShowProgram.class, "switchMainScene中，currentScene为空", LogType.error);
			return flag;
		}

	}

	// 切换上一个场景的方法
	public Boolean switchLastScene() {
		Boolean flag = false;
		// 如果当前场景就是主场景，就不在切换
		if (scenePath.size() == 0) {
			flag = false;
		} else {
			try {
				scenePath.pop();
				String sceneId = scenePath.peek();
				currentScene = sceneMap.get(sceneId);
				loadScene();
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.printApointedLog(ShowProgram.class, "switchLastScene时，抛异常了", LogType.info);
				flag = false;
				switchMainScene();
			}
		}
		return flag;
	}

	// 获取主场景
	private SceneBean getMainScene() {
		try {
			SceneBean scene = null;
			for (String sceneId : sceneMap.keySet()) {
				scene = sceneMap.get(sceneId);
				if (scene.getIsmain()) {
					return scene;
				}
			}
			return GlobalVariable.globalProgram.getScenes().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.showToastShortTime(getContext(), "getMainScene中，获取主场景出错了！！！");
			return null;
		}
	}

	// 重置播放
	private void resetPlay() {
		// 如何将播放重置，销毁目前的播放，重新加载播放
	}

	// 处理模板消息
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MessageValue.DESTROY_SCENE:
					destroyScene(lastScene);
					lastScene = currentScene;
					break;
				case MessageValue.SHOW_SCENE:
					// SceneBean scene = (SceneBean)msg.obj;
					// if(scene != null){
					// addView(scene.getView());
					// }
					showScene();
					break;
				case 111:
					switchMainScene();
					break;
			}
		};
	};

	// 模板加载
	private void loadScene() {
		isLoading = true;
		if (currentScene == null) {
			isLoading = false;
			LogUtil.printApointedLog(ShowProgram.class, "currentScene is null", LogType.error);
			return;
		}
		final FrameLayout sceneLayout = new FrameLayout(getContext());
		FrameLayout.LayoutParams fl = new LayoutParams(GlobalVariable.globalProgram.getWidth(),
						GlobalVariable.globalProgram.getHeight());
		fl.gravity = Gravity.CENTER_HORIZONTAL;
		sceneLayout.setLayoutParams(fl);
		// 设置场景背景图片
		final String backimg = currentScene.getBackimg();
		if (backimg != null) {
			if (mLoadSceneBgTask != null) {
				mLoadSceneBgTask.cancel(true);
				mLoadSceneBgTask = null;
			}
			mLoadSceneBgTask = new LoadSceneBgTask();
			mLoadSceneBgTask.execute(backimg);
		}
		List<RegionBean> regions = currentScene.getRegions();
		loadRegions(regions, sceneLayout);
		List<PlugBean> plugs = currentScene.getPlugs();
		loadPlugs(plugs, sceneLayout);
		List<ButtonBean> buttons = currentScene.getButtons();
		loadButtons(buttons, sceneLayout);
		currentScene.setView(sceneLayout);

		// 加载250ms再进行显示
		handler.sendEmptyMessageDelayed(MessageValue.SHOW_SCENE, Constant.SCENE_PRLOAD_TIME);
		// showScene();
	}

	private class LoadSceneBgTask extends AsyncTask<String, Void, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable background = Drawable.createFromPath(CoofileTouchApplication.getAppResBasePath() + params[0]);
			return background;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			FrameLayout sceneLayout = (FrameLayout) currentScene.getView();
			if (sceneLayout != null) {
				sceneLayout.setBackground(result);
			} else {
				// 执行了
				LogUtil.printApointedLog(ShowProgram.class, "LoadSceneBgTask currentScene's sceneLayout is null",
								LogType.error);
			}
		}

	}

	// 加载plug插件
	private void loadPlugs(List<PlugBean> plugs, FrameLayout sceneLayout) {
		for (int i = 0; i < plugs.size(); i++) {
			PlugBean plug = plugs.get(i);
			ShowPlug showPlug = new ShowPlug(getContext(), plug);
			plug.setShowPlug(showPlug);
			LayoutParams params = new LayoutParams(plug.getWidth(), plug.getHeight());
			params.leftMargin = plug.getLeft() < 0 ? 0 : plug.getLeft();
			params.topMargin = plug.getTop() < 0 ? 0 : plug.getTop();
			sceneLayout.addView(showPlug, params);
		}
	}

	// 加载buttons
	private void loadButtons(List<ButtonBean> buttons, FrameLayout sceneLayout) {
		Boolean isClearScenePath = true;
		for (int i = 0; i < buttons.size(); i++) {
			ButtonBean button = buttons.get(i);
			if (button.getType().equals(ButtonType.返回)) {
				isClearScenePath = false;
			}
			ShowButton showButton = new ShowButton(getContext(), button);
			button.setShowButton(showButton);
			LayoutParams params = new LayoutParams(button.getWidth(), button.getHeight());
			params.leftMargin = button.getLeft() < 0 ? 0 : button.getLeft();
			params.topMargin = button.getTop() < 0 ? 0 : button.getTop();
			sceneLayout.addView(showButton, params);
		}
		if (isClearScenePath) {
			clearScenePath();
		}

	}

	private void clearScenePath() {
		if (scenePath != null && scenePath.size() > 0) {
			String seek = scenePath.peek();
			scenePath.clear();
			scenePath.push(seek);
		}
	}

	// 加载区域列表
	private void loadRegions(List<RegionBean> regions, FrameLayout sceneLayout) {
		ShowBase showRegion;
		for (int i = 0; i < regions.size(); i++) {
			RegionBean region = regions.get(i);
			// switch (region.getType()) {
			// case 图片:
			// showRegion = new ShowRegionForImage(getContext(), region);
			// break;
			// default:
			// showRegion = new ShowRegion(getContext(), region);
			// break;
			// }
			showRegion = new ShowRegion(getContext(), region);
			region.setShowRegion(showRegion);
			LayoutParams params = new LayoutParams(region.getWidth(), region.getHeight());
			params.leftMargin = region.getLeft() < 0 ? 0 : region.getLeft();
			params.topMargin = region.getTop() < 0 ? 0 : region.getTop();
			sceneLayout.addView(showRegion, params);
		}
	}

	// 场景播放，分成三个方法
	private void showScene() {
		View view = currentScene.getView();
		if (view == null) {
			isLoading = false;
			return;
		}
		addView(view);

		List<RegionBean> regions = currentScene.getRegions();
		showRegions(regions);

		List<PlugBean> plugs = currentScene.getPlugs();
		showPlugs(plugs);

		List<ButtonBean> buttons = currentScene.getButtons();
		showButtons(buttons);

		// 处理销毁的问题 马上销毁上一个场景
		// destroyScene(lastScene);
		handler.sendEmptyMessageDelayed(MessageValue.DESTROY_SCENE, Constant.SCENE_DELAY_DESTROY_TIME);

		// 将主页界面隐藏
		fl_home.setVisibility(View.GONE);

		// 处理一段时间内跳转会主场景
		handler.sendEmptyMessageDelayed(111, 3 * 60 * 1000);

	}

	// 显示区域
	private void showRegions(List<RegionBean> regions) {
		for (int i = 0; i < regions.size(); i++) {
			regions.get(i).getShowRegion().startShow();
		}
	}

	// 显示插件
	private void showPlugs(List<PlugBean> plugs) {
		for (int i = 0; i < plugs.size(); i++) {
			PlugBean plug = plugs.get(i);
			plug.getShowPlug().startShow();
		}
	}

	// 显示按钮
	private void showButtons(List<ButtonBean> buttons) {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).getShowButton().startShow();
		}
	}

	// 场景销毁，分成三个销毁的方法
	private synchronized void destroyScene(SceneBean scene) {
		if (scene == null || scene.getView() == null) {
			isLoading = false;
			return;
		}
		// 移除上一个，这是一处整个view，移除后整个界面就不显示了
		FrameLayout layout = (FrameLayout) scene.getView();
		// 先移除layout，虽然耗时厉害，但是会直接停止所有的播放
		this.removeView(layout);

		// 销毁场景背景,费时操作bitmap为空
		if (scene.getBackimg() != null) {
			BitmapDrawable drawable = (BitmapDrawable) layout.getBackground();
			DestroyUtil.destroyDrawable(drawable);
		}

		layout.setBackground(null);

		// 对区域的销毁，
		List<RegionBean> regions = scene.getRegions();
		destroyRegions(regions, layout);

		// 对plug的销毁
		List<PlugBean> plugs = scene.getPlugs();
		destroyPlugs(plugs, layout);

		// 对按钮的销毁
		List<ButtonBean> buttons = scene.getButtons();
		destroyButtons(buttons, layout);

		layout = null;
		scene.setView(null);
		isLoading = false;
	}

	// 对按钮的销毁
	private void destroyButtons(List<ButtonBean> buttons, FrameLayout layout) {
		for (ButtonBean button : buttons) {
			ShowButton _ShowButton = button.getShowButton();
			layout.removeView(_ShowButton);
			// 调用button的销毁方法，把销毁放到对应的类中处理
			_ShowButton.destroyShow();
			_ShowButton = null;
			button.setShowButton(null);
		}
	}

	// 对plug的销毁
	private void destroyPlugs(List<PlugBean> plugs, FrameLayout layout) {
		for (PlugBean plug : plugs) {
			ShowPlug _ShowPlug = plug.getShowPlug();
			layout.removeView(_ShowPlug);
			_ShowPlug.stopShow();
			_ShowPlug.destroyShow();
			_ShowPlug = null;
			plug.setShowPlug(null);
		}
	}

	// 对区域的销毁，这是一个一个的移除和销毁
	private void destroyRegions(List<RegionBean> regions, FrameLayout layout) {
		for (RegionBean region : regions) {
			ShowBase _ShowRegion = region.getShowRegion();
			// 先移除区域的FrameLayout
			layout.removeView(_ShowRegion);
			// 移除handler中的消息，并制空。
			_ShowRegion.stopShow();
			// 循环他的所有孩子,依次销毁view所占内存,并移除所有孩子
			_ShowRegion.destroyShow();
			// 制空
			_ShowRegion = null;
			region.setShowRegion(null);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isLoading) {
			return true;
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (handler != null && handler.obtainMessage(1) != null) {
					handler.removeMessages(111);
					handler.sendEmptyMessageDelayed(111, 3 * 60 * 1000);
				}
				break;
		}
		return false;
	}

	public SceneBean getCurrentScene() {
		return currentScene;
	}
}
