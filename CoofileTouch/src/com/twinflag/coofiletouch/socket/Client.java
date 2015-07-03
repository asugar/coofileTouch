package com.twinflag.coofiletouch.socket;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import com.twinflag.coofiletouch.ftp.DownloadQueue;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalValue;

public class Client extends Thread {

	private Context mContext;
	private NetSocket mNetSocket;// Socket客户端
	private boolean isSocketClose = true;// socket是否关闭
	private boolean isClientClose = true; // client是否在运行
	private DownloadQueue mDownloadQueue;
	private HandlerRequest mHandlerRequest;// 处理接收到的消息
	private ClientStatusThread mClientStatusThread;// 状态发送线程

	public Client(Context Context, Handler handler) {
		this.mContext = Context;
		// 下载相关
		mHandlerRequest = new HandlerRequest(mContext);

		mDownloadQueue = new DownloadQueue(mContext, handler);
		mDownloadQueue.start();

		mClientStatusThread = new ClientStatusThread();
		mClientStatusThread.start();
		
	}

	/**
	 * 接受服务器发送的消息
	 **/
	public void run() {
		while (isClientClose) {
			try {
				if (isConnect()) {
					while (!isSocketClose) {
						byte[] buffer = mNetSocket.receiveData();
						if (buffer == null) {
							close();
						} else if (buffer.length != 1) {
							String msg = new String(buffer, "UTF-8");
							System.out.println("msg = " + msg);
							JSONTokener tokener = new JSONTokener(msg);
							JSONObject json = (JSONObject) tokener.nextValue();
							String command = json.getString("command");
							if (!command.equals(Command.HEARTBEAT) && !TextUtils.isEmpty(command)) {
								mHandlerRequest.execCommand(json);
							}
						}
						SystemClock.sleep(Constant.HEARBEAT_INTERVAL_TIME);
					}
				} else {
					SystemClock.sleep(Constant.SOCKET_INTERRUPT_RECONNECT_INTERVAL_TIME);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 向服务器发送状态的线程
	 * */
	private class ClientStatusThread extends Thread {
		private boolean flag = true;
		@Override
		public void run() {
			while (flag) {
				try {
					Thread.sleep(Constant.UPLOAD_CLIENT_STATUE_INTERVAL);
					SendRequest.clientstatus();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		public void stopThread() {
			flag = false;
		}

	}

	public void sendMessage(String message) {
		if (mNetSocket != null && !isSocketClose && mNetSocket.m_out != null) {
			mNetSocket.SendData(message);
		}
	}

	/**
	 * 连接服务器是否成功
	 * */
	private boolean isConnect() {
		mNetSocket = new NetSocket();
		if (mNetSocket.connect(GlobalValue.options.server_ip, GlobalValue.options.server_port) == 0) {
			this.isSocketClose = true;
			return false;
		} else {
			this.isSocketClose = false;
			LoginCommand();
			return true;
		}
	}

	private void close() {
		if (!this.isSocketClose) {
			this.mNetSocket.Close();
			this.mNetSocket = null;
			this.isSocketClose = true;
		}
	}

	public void stopClient() {
		if (mClientStatusThread != null) {
			mClientStatusThread.stopThread();
			mClientStatusThread = null;
		}
		if (mHandlerRequest != null) {
			mHandlerRequest = null;
		}
		mDownloadQueue.stopThread();
		mDownloadQueue = null;
		this.isClientClose = false;
		close();
	}

	private void LoginCommand() {
		SendLoginCommand(Command.LOGIN);
		SendLoginCommand(Command.HISTORYTASK);
		SendLoginCommand(Command.HISTORYMESSAGE);
		sendLoginSyncTime();
	}

	private void SendLoginCommand(String command) {
		JSONObject json = new JSONObject();
		try {
			json.put("command", command);
			json.put("mac", ClientStatus.getMac());
			sendMessage(json.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void sendLoginSyncTime() {
		try {
			JSONObject json = new JSONObject();
			json.put("command", Command.SYNCTIME);
			json.put("mac", ClientStatus.getMac());
			json.put("local", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
			sendMessage(json.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
