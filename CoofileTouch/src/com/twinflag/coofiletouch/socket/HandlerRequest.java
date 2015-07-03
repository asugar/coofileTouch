package com.twinflag.coofiletouch.socket;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.database.MsgData;
import com.twinflag.coofiletouch.database.MyDbHelper;
import com.twinflag.coofiletouch.database.ProgramData;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.ftp.DownloadQueue;
import com.twinflag.coofiletouch.ftp.DownloadStatus;
import com.twinflag.coofiletouch.ftp.DownloadUpdate;
import com.twinflag.coofiletouch.parse.ParseMsg;
import com.twinflag.coofiletouch.parse.ParseProgram;
import com.twinflag.coofiletouch.service.VolumService;
import com.twinflag.coofiletouch.utils.ShellUtils;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalVariable;

public class HandlerRequest {

	private static final String TAG = "HandlerRequest";
	private Context context;
	private MyDbHelper myDbHelper;

	public HandlerRequest(Context context) {
		this.context = context;
		myDbHelper = new MyDbHelper(context);
		myDbHelper.open();
	}

	/**
	 * 更新消息执行对应的命令
	 */
	public void execCommand(JSONObject jsonObj) {
		long startTime = System.currentTimeMillis();
		try {
			Log.i(TAG, "命令耗时：" + jsonObj.toString());
			String command = jsonObj.getString("command");
			if (command.equals(Command.HISTORYTASK)) {
				historyTask(jsonObj);
			} else if (command.equals(Command.HISTORYMESSAGE)) {
				historyMessage(jsonObj);
			} else if (command.equals(Command.PUBLISHTASK)) {
				publishTask(jsonObj);
			} else if (command.equals(Command.PUBLISHMESSAGE)) {
				publishMessage(jsonObj);
			} else if (command.equals(Command.STOPTASK) || command.equals(Command.DELETETASK)) {
				deleteTask(jsonObj);
			} else if (command.equals(Command.STOPMESSAGE) || command.equals(Command.DELETEMESSAGE)) {
				deleteMessage(jsonObj);
			} else if (command.equals(Command.PARTUPDATE)) {
				partupdate(jsonObj);
			} else if (command.equals(Command.SETVOLUME)) {
				setvolume(jsonObj);
			} else if (command.equals(Command.UPDATEAPP)) {
				updateAPP(jsonObj);
			} else if (command.equals(Command.SHUTDOWN)) {
				shutdownBox(jsonObj);
			} else if (command.equals(Command.REBOOT)) {
				rebootBox(jsonObj);
			} else if (command.equals(Command.TIMEDPOWER)) {
				timedpower(jsonObj);
			}
			Log.i(TAG, "命令耗时：" + (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 处理任务历史任务
	private void historyTask(JSONObject jsonObj) throws Exception {
		if ("false".equals(jsonObj.getString("empty"))) {
			JSONArray ids = jsonObj.getJSONArray("ids");
			List<ProgramData> dbTasks = myDbHelper.getUsefulTasks();
			JSONArray results = new JSONArray();
			for (int i = 0; i < ids.length(); i++) {
				boolean flag = false;// 默认数据库中没有
				String[] result = ids.getString(i).replace('T', ' ').split("\\|");
				for (ProgramData dbTask : dbTasks) {
					if (dbTask.getPgId().equals(result[0]) && dbTask.getUpdateDate().equals(result[1])) {
						flag = true;
						break;
					}
				}
				if (!flag) {// 数据库中没有该任务则下载
					results.put(result[0]);
				}
			}
			for (ProgramData dbTask : dbTasks) {
				boolean flag = false;// 默认服务器中没有
				String dbTaskId = dbTask.getPgId();
				for (int i = 0; i < ids.length(); i++) {
					String[] result = ids.getString(i).replace('T', ' ').split("\\|");
					if (dbTaskId.equals(result[0]) && dbTask.getUpdateDate().equals(result[1])) {
						flag = true;
						break;
					}
				}
				if (!flag) {// 服务器中有该任务则不删除
					myDbHelper.deleteTask(dbTaskId);
					GlobalVariable.pgRemove(dbTaskId);
				}
			}
			if (results.length() > 0) {
				SendRequest.requestTask(results);
			}
		} else {
			myDbHelper.deleteAllTask();
			GlobalVariable.pgClear();
		}
	}

	// 处理滚动消息历史任务
	private void historyMessage(JSONObject jsonObj) throws Exception {
		if ("false".equals(jsonObj.getString("empty"))) {
			JSONArray ids = jsonObj.getJSONArray("ids");
			List<MsgData> dbMsgs = myDbHelper.getFullMessage();
			JSONArray results = new JSONArray();
			for (int i = 0; i < ids.length(); i++) {
				boolean flag = false;// 默认数据库中没有
				String[] result = ids.getString(i).replace('T', ' ').split("\\|");
				for (MsgData dbMsg : dbMsgs) {
					if (dbMsg.getMsgId().equals(result[0]) && dbMsg.getUpdateDate().equals(result[1])) {
						flag = true;
						break;
					}
				}
				if (!flag) {// 数据库中没有该消息则下载
					results.put(result[0]);
				}
			}
			for (MsgData dbMsg : dbMsgs) {
				boolean flag = false;// 默认服务器中没有
				String dbMsgId = dbMsg.getMsgId();
				for (int i = 0; i < ids.length(); i++) {
					String[] result = ids.getString(i).replace('T', ' ').split("\\|");
					if (dbMsgId.equals(result[0]) && dbMsg.getUpdateDate().equals(result[1])) {
						flag = true;
						break;
					}
				}
				if (!flag) {// 服务器中有该消息则不删除
					myDbHelper.deleteMessage(dbMsgId);
					GlobalVariable.messageRemove(dbMsgId);
				}
			}
			if (results.length() > 0) {
				SendRequest.requestMessage(results);
			}
		} else {
			myDbHelper.deleteAllMessage();
			GlobalVariable.messageClear();
		}
	}

	// 发布任务
	private void publishTask(JSONObject jsonObj) throws Exception {
		String content = jsonObj.getString("task");
		// need fix
		ProgramBean task = null;
		ParseProgram.getProgram(content);// 非常费时
		String id = task.getId();
		String updateDate = task.getUpdateTime();
		boolean isExist = false;// 默认任务不存在
		boolean isUpdate = false;// 默认任务没被修改过
		List<ProgramData> dbTasks = myDbHelper.getFullTasks();
		for (ProgramData dbTask : dbTasks) {
			String dbId = dbTask.getPgId();
			if (id.equals(dbId)) {
				isExist = true;
				String dbUpdateDate = dbTask.getUpdateDate();
				if (!updateDate.equals(dbUpdateDate)) {
					isUpdate = true;
					myDbHelper.deleteTask(dbId);
					GlobalVariable.pgRemove(dbId);
				}
				break;
			}
		}
		if (!isExist || isUpdate) {
			// 任务不存在或任务被修改过则重新下载 2014-10-10 00:00:00
			// String date = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			myDbHelper.insertTask(id, task.getName(), content, "0", updateDate, null);
			jsonObj.put("downloadCount", 0);
			// 在此添加下载列表
			DownloadQueue.addTask(jsonObj);
		}
	}

	// 发布滚动消息
	private void publishMessage(JSONObject jsonObj) throws Exception {
		String msg = jsonObj.getString("msg");
		MessageBean message = ParseMsg.getMessage(msg);
		String id = message.getId();
		MsgData dbMessage = myDbHelper.getMessageById(id);
		if (dbMessage != null) {
			myDbHelper.deleteMessage(id);
			GlobalVariable.messageRemove(id);
		}
		myDbHelper.insertMessage(id, msg, message.getUpdateTime());
		GlobalVariable.messageAdd(message);
	}

	// 停止任务
	private void deleteTask(JSONObject jsonObj) throws Exception {
		String taskId = jsonObj.getString("taskid");
		myDbHelper.deleteTask(taskId);
		if (GlobalVariable.pgIsContain(taskId)) {
			GlobalVariable.pgRemove(taskId);
		} else {
			if (DownloadQueue.download != null) {
				// 判断正在下载的任务是不是将要停止的任务时使用了currentTaskid
				if (DownloadQueue.currentTaskid.equals(taskId)) {
					DownloadQueue.download.stopdownload();
				} else {
					Iterator<JSONObject> iter = DownloadQueue.queue.iterator();
					while (iter.hasNext()) {
						JSONObject obj = iter.next();
						ProgramBean task = null;
//								ParseProgram.getProgram(obj.getString("task"));
						if (taskId.equals(task.getId())) {
							DownloadQueue.queue.remove(obj);
						}
					}
				}
			}
		}
	}

	// -------------------------------------------------------
	// 停止滚动消息
	private void deleteMessage(JSONObject jsonObj) throws Exception {
		String msgId = jsonObj.getString("msgid");
		myDbHelper.deleteMessage(msgId);
		GlobalVariable.messageRemove(msgId);
	}

	// 处理局部更新
	private void partupdate(JSONObject jsonObj) throws Exception {
		jsonObj.put("downloadCount", 0);
		jsonObj.put("updateRegion", true);
		DownloadQueue.addTask(jsonObj);
	}

	// 设置音量
	private void setvolume(JSONObject jsonObj) throws Exception {
		String value = jsonObj.getString("value");
		Intent intent = new Intent(context, VolumService.class);
		intent.putExtra("value", value);
		context.startService(intent);
	}

	// 定时关机

	// 重启
	private void rebootBox(JSONObject jsonObj) {
		ShellUtils.execCommand("reboot", true, true);
	}

	// 关机
	private void shutdownBox(JSONObject jsonObj) {
		ShellUtils.execCommand("reboot -p", true, true);
	}

	// 更新app
	private void updateAPP(JSONObject jsonObj) throws Exception {
		String path = jsonObj.getString("androidpath").replace("\\", "/");
		if (path == null || path == "") {
			return;
		}
		String filePath = path.substring(0, path.indexOf('/'));
		String fileName = path.substring(path.indexOf('/') + 1);
		String version = jsonObj.getString("androidversion");

		if (version.compareTo(CoofileTouchApplication.getAppVersion()) > 0) {
			DownloadUpdate down = new DownloadUpdate();
			DownloadStatus ds = down.downLoadUpdatePak(filePath, fileName);
			if (ds.isSucceed()) {
				String savefolder = CoofileTouchApplication.getAppResBasePath() + File.separator
								+ Constant.APP_RES_UPDATEFILE_FOLDER;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(new File(savefolder, fileName)),
								"application/vnd.android.package-archive");
				context.startActivity(intent);
			}
		}
	}

	// 处理定时开关机
	/**
	 * 设置时间：{"command":"autopower","xml":"00:00"}
	 * 取消设置：{"command":"autopower","xml":""}
	 * 
	 * @param jsonObj
	 * @throws Exception
	 */
	private void timedpower(JSONObject jsonObj) throws Exception {
		String power_on_time = null;
		String power_off_time = jsonObj.getString("xml");
		if (power_off_time.equals("")) {
			// 取消设置
			SharedPreferences sp = context.getSharedPreferences("power_time", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			// 取消定时器
			Intent intent_delayOff = new Intent("wits.com.simahuan.shutdown");
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent_delayOff,
							PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(pi);

		} else {
			// 设置开关机时间
			SharedPreferences sp = context.getSharedPreferences("power_time", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("power_off_time", power_off_time);
			editor.commit();
			Intent i = new Intent("com.twinflag.receiver.TIMEPOWER");
			context.sendBroadcast(i);
		}
	}
}
