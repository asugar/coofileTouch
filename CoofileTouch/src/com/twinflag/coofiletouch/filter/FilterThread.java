package com.twinflag.coofiletouch.filter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import com.twinflag.coofiletouch.database.MsgData;
import com.twinflag.coofiletouch.database.MyDbHelper;
import com.twinflag.coofiletouch.database.ProgramData;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.ftp.DownloadQueue;
import com.twinflag.coofiletouch.parse.ParseMsg;
import com.twinflag.coofiletouch.parse.ParseProgram;
import com.twinflag.coofiletouch.value.GlobalVariable;
import com.twinflag.coofiletouch.value.MessageValue;

/**
 * @author wanghuayi 过滤线程
 */
public class FilterThread extends Thread {

	private MyDbHelper mDbHelper;
	private boolean isContinue = true;
	private Handler mhandler;
	private ProgramBean program;
	private List<MessageBean> msgs;

	public FilterThread(Handler handler, Context context) {
		mDbHelper = new MyDbHelper(context);
		mhandler = handler;
		initDataBase();
	}

	@Override
	public void run() {

		while (isContinue) {

			// 首先过滤任务
			if (GlobalVariable.globalAllPgs.size() != 0) {

				program = FilterUtil.filterTask();

				// 检查过滤出来的program：
				// null时，停止所有播放；不为空时，检查globalProgram是否为空，为空，赋值开启播放；
				// 不为空：比较两个Program是否一样，不一样，切换任务；一样，比较两个insertTime
				if (program == null) {
					mhandler.sendEmptyMessage(MessageValue.STOP_PROGRAM);
				} else {
					if (GlobalVariable.globalProgram == null) {
						GlobalVariable.globalProgram = program;
						mhandler.sendEmptyMessage(MessageValue.START_PROGRAM);
					} else {
						// 比较两个program是否相同,相同比较insertTime，不同
						if (program.getId() == GlobalVariable.globalProgram.getId()) {

							if (GlobalVariable.globalProgram.getUpdateTime().compareTo(program.getUpdateTime()) < 0) {
								// 处理切换任务操作
								GlobalVariable.globalProgram = program;
								mhandler.sendEmptyMessage(MessageValue.SWITCH_PROGRAM);
							}
						} else {
							// 处理切换任务操作
							GlobalVariable.globalProgram = program;
							mhandler.sendEmptyMessage(MessageValue.SWITCH_PROGRAM);
						}
					}
				}
			} else {
				if (GlobalVariable.globalProgram != null) {
					// 停止播放任务
					GlobalVariable.globalProgram = null;
					mhandler.sendEmptyMessage(MessageValue.STOP_PROGRAM);
				}
			}

			// 过滤消息
			if (GlobalVariable.globalAllMsgs.size() != 0) {

				msgs = FilterUtil.filterMsg();
				if (msgs == null || msgs.size() == 0) {
					// 发送停止命令
					mhandler.sendEmptyMessage(MessageValue.STOP_MESSAGE);
				} else {
					if (GlobalVariable.globalMsgs.size() == 0) {
						GlobalVariable.globalMsgs = msgs;
						// 开启播放
						mhandler.sendEmptyMessage(MessageValue.START_MESSAGE);
					} else {
						// 检查当前播放的消息是否在过滤后的列表中，在，赋值；不在，赋值，切换播放
						// msgs 和 GlobalVariable.globalMsgs

						if (!CompareUtil.compareMessages(GlobalVariable.globalMsgs, msgs)) {
							GlobalVariable.globalMsgs = msgs;
							mhandler.sendEmptyMessage(MessageValue.SWITCH_MESSAGE);//
							// 消息列表变了，重新播放消息列表
						}
					}
				}
			} else {
				if (GlobalVariable.globalMsgs.size() != 0) {
					GlobalVariable.globalMsgs.clear();
					// 发送停止滚动消息列表任务
					mhandler.sendEmptyMessage(MessageValue.STOP_MESSAGE);
				}
			}

			SystemClock.sleep(3000);
		}
	}

	/**
	 * 查找数据库中的任务，如果任务下载完成，加入播放列表，如果未下载完成，放入下载线程，进行下载
	 * */
	public void initDataBase() {
		mDbHelper.open();
		// 获取所有的任务
		List<ProgramData> programs = mDbHelper.getFullTasks();
		for (ProgramData program : programs) {
			if (program.getStopflag().equals("2")) {
//				ProgramBean programbean = ParseProgram.getProgram(program.getPgContent());
//				GlobalVariable.pgAdd(programbean);
			} else if (program.getStopflag().equals("0")) {
				JSONObject json = new JSONObject();
				try {
					json.put("task", program.getPgContent());
					json.put("downloadCount", 0);
					DownloadQueue.queue.add(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		List<MsgData> msgs = mDbHelper.getFullMessage();
		for (int i = 0; i < msgs.size(); i++) {
			String messageXml = msgs.get(i).getMsgContent();
			GlobalVariable.messageAdd(ParseMsg.getMessage(messageXml));
		}

	}

	public void stopFilterThread() {
		isContinue = false;
		mhandler.removeCallbacksAndMessages(null);
		mhandler = null;
	}
}
