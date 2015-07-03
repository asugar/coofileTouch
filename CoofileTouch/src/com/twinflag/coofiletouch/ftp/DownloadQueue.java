package com.twinflag.coofiletouch.ftp;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.twinflag.coofiletouch.database.MyDbHelper;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.parse.ParseProgram;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.ErrorValue;
import com.twinflag.coofiletouch.value.GlobalVariable;
import com.twinflag.coofiletouch.value.MessageValue;

public class DownloadQueue {

	public static final LinkedBlockingQueue<JSONObject> queue = new LinkedBlockingQueue<JSONObject>();// 这个队列是线程安全的队列
	public static Download download;
	public static String currentTaskid;
	private Context mContext;
	private Handler mHandler;
	private MyDbHelper mTaskData;
	private boolean flag = true;

	public DownloadQueue(Context context, Handler handler) {
		this.mContext = context;
		mHandler = handler;
		mTaskData = new MyDbHelper(mContext);
	}

	// 把下载任务添加到下载队列中
	public static void addTask(JSONObject array) {
		try {
			queue.put(array);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mTaskData.open();
				while (flag) {
					try {
						if (queue.size() == 0) {
							SystemClock.sleep(Constant.DOWNLOADQUEUE_INTERVAL_TIME);
							continue;
						}

						ProgramBean task = null;
						String taskXml = null;
						List<String> pathsList = null;

						JSONObject json = queue.take();
						int downloadCount = json.getInt("downloadCount");
						json.put("downloadCount", downloadCount + 1);
						taskXml = json.getString("task");

//						task = ParseProgram.getProgram(taskXml);
						pathsList = ParseProgram.getDownloadPath(task);
						currentTaskid = task.getId();
						// 同样是多个实例，参数传递
						download = new Download(pathsList, task.getName(), task.getId(), json);
						int downFlag = download.download();
						download = null;

						// 如果当前任务的总数不为0，如果这个任务在当前总任务集合中已经存在，
						// 则移除原来的任务，把更新过的任务添加进去，如果任务不存在，则下载元素，
						// 然后添加到总的任务集合中

						if (downFlag == Download.DOWNLOAD_SUCCESS) {
							mTaskData.updateTask("2", task.getId());
							// GlobalVariable.globalProgram = task;
							GlobalVariable.globalAllPgs.add(task);
						} else if (downFlag != Download.DOWNLOAD_USER_STOP_DOWNLOAD) {
							LogUtil.printLog(DownloadQueue.class, "The Error result: " + downFlag);
							String errorMsg = ErrorValue.ERROR_MSG_ERROR_UNKNOWN;
							if (downFlag == Download.DOWNLOAD_ERROR_NO_SUFFICENT_SPACE) {
								errorMsg = ErrorValue.ERROR_MSG_ERROR_NO_SUFFICENT_SPACE;
							} else if (downFlag == Download.DOWNLOAD_ERROR_FILE_NOT_FOUND) {
								errorMsg = ErrorValue.ERROR_MSG_ERROR_FILE_NOT_FOUND;
							} else if (downFlag == Download.DOWNLOAD_ERROR_IO_EXCEPTION) {
								errorMsg = ErrorValue.ERROR_MSG_ERROR_IO_EXCEPTION;
							}
							Message msg = Message.obtain();
							msg.what = MessageValue.DOWNLOAD_TASK_ERROR;
							msg.obj = errorMsg;
							mHandler.sendMessage(msg);
						}
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
					catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	// 暂停下载线程
	public void stopThread() {
		this.flag = false;
	}

}
