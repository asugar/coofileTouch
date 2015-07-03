package com.twinflag.coofiletouch.socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.MainActivity;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.ftp.DownloadQueue;
import com.twinflag.coofiletouch.parse.ParseProgram;

public class SendRequest {

	private static Date startDate = new Date();

	public static void login() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.LOGIN);
			jsonObj.put("mac", ClientStatus.getMac());
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void historyTask() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.HISTORYTASK);
			jsonObj.put("mac", ClientStatus.getMac());
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void historyMessage() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.HISTORYMESSAGE);
			jsonObj.put("mac", ClientStatus.getMac());
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void synctime() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.SYNCTIME);
			jsonObj.put("mac", ClientStatus.getMac());
			jsonObj.put("local", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void requestTask(JSONArray ids) {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.REQUESTTASK);
			jsonObj.put("mac", ClientStatus.getMac());
			jsonObj.put("ids", ids);
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void requestMessage(JSONArray ids) {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("command", Command.REQUESTMESSAGE);
			jsonObj.put("mac", ClientStatus.getMac());
			jsonObj.put("ids", ids);
			MainActivity.client.sendMessage(jsonObj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void update() {
		try {
			JSONObject json = new JSONObject();
			json.put("command", Command.UPDATE);
			json.put("mac", ClientStatus.getMac());
			MainActivity.client.sendMessage(json.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载任务的请求
	 */
	public static void taskdownload(String taskId, String TaskName) {
		try {
			JSONObject taskDownload = new JSONObject();
			taskDownload.put("command", Command.TASKDOWNLOAD);
			taskDownload.put("taskId", Integer.parseInt(taskId));
			taskDownload.put("taskName", TaskName);
			taskDownload.put("taskPercent", "100");
			taskDownload.put("mac", ClientStatus.getMac());
			taskDownload.put("update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			MainActivity.client.sendMessage(taskDownload.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求客户端状态
	 */
	public static void clientstatus() {
		try {
			JSONObject json = new JSONObject();
			json.put("command", Command.CLIENTSTATUS);
			json.put("mac", ClientStatus.getMac());
			json.put("SysVersion", ClientStatus.getSystemVersion());
			json.put("SoftVersion", CoofileTouchApplication.getAppVersion());
			json.put("CPU", ClientStatus.readUsage());
			json.put("memory", ClientStatus.getMemoryUse());
			json.put("disk", ClientStatus.getDiskSpace() + "");
			json.put("runtime", ((new Date()).getTime() - startDate.getTime()) / 1000 / 60 + "");
			json.put("update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			MainActivity.client.sendMessage(json.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向服务器发送进度 当前任务的进度
	 */
	public static void sendProcessedMsg(String processed, String taskId, String TaskName) {
		LinkedBlockingQueue<JSONObject> queues = DownloadQueue.queue;
		JSONArray array = new JSONArray();
		int size = queues.size();
		try {
			JSONObject json1 = new JSONObject();
			json1.put("taskId", Integer.parseInt(taskId));
			json1.put("taskName", TaskName);
			json1.put("taskPercent", processed);
			array.put(json1);
			if (size != 0) {
				JSONObject obj;
				obj = queues.peek();
				String taskXml = obj.getString("task");
				ProgramBean task = null;
//						ParseProgram.getProgram(taskXml);
				String mId = task.getId();
				String mTaskName = task.getName();
				JSONObject json2 = new JSONObject();
				json2.put("taskId", Integer.parseInt(mId));
				json2.put("taskName", mTaskName);
				json2.put("taskPercent", "0");
				array.put(json2);
			}
			JSONObject arrays = new JSONObject();
			arrays.put("command", Command.TASKDOWNLOAD);
			arrays.put("playlist", array);
			arrays.put("mac", ClientStatus.getMac());
			arrays.put("update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			MainActivity.client.sendMessage(arrays.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
