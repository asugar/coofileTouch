package com.twinflag.coofiletouch.ftp;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.os.StatFs;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.ftp.UploadHelper.processedListener;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.socket.SendRequest;
import com.twinflag.coofiletouch.type.LogType;

/**
 * 这个方法主要是下载素材和上传日志
 * */
public class Download {

	private final static long MIN_SIZE = 2 * 1024 * 1024;
	private final static long FTP_DOWNLOAD_MAX_INTERVAL = 1000 * 30 * 2L;

	// 文件下载结果
	public final static int DOWNLOAD_SUCCESS = 0;
	public final static int DOWNLOAD_USER_STOP_DOWNLOAD = 1;
	public final static int DOWNLOAD_ERROR_UNKNOWN = 2;
	public final static int DOWNLOAD_ERROR_NO_SUFFICENT_SPACE = 3;
	public final static int DOWNLOAD_ERROR_FILE_NOT_FOUND = 4;
	public final static int DOWNLOAD_ERROR_IO_EXCEPTION = 5;

	/**
	 * 存放要下载的素材的路径
	 * */
	private List<String> paths = new ArrayList<String>();
	private List<String> downloadPaths;
	private String TaskName;
	private String taskId;
	private UploadHelper ftp;
	private String proccessed = "";
	private List<String> errorPath = new ArrayList<String>();
	private JSONObject json;
	long localToatalSize = 0L;
	long remoteneededSize = 0L;
	long remoteTotalSize = 0L;
	long downloadedSize = 0L;
	long adcategorysize = 0L;
	DecimalFormat df = new DecimalFormat("#.00");

	public Download(List<String> list, String TaskName, String taskId, JSONObject json) {
		this.paths = list;
		this.TaskName = TaskName;
		this.taskId = taskId;
		this.json = json;
	}

	public Download() {

	}

	/**
	 * 停止任务下载
	 * */
	public void stopdownload() {
		ftp.closeConnect();
	}

	/**
	 * 下载元素
	 */
	public int download() {

		long downedsize = filepathsfilter() / 1024;
		if (downloadPaths.size() == 0) {
			SendRequest.taskdownload(taskId, TaskName);
			return DOWNLOAD_SUCCESS;
		} else {
			long lastConnTicks = System.currentTimeMillis();
			long currentTicks = System.currentTimeMillis();
			localToatalSize = getSDFreeSize();
			ftp = UploadHelper.getInstance();
			ftp.openConnect();
			// 非常费时
			remoteTotalSize = ftp.getTaskToatalSize(paths);
			remoteneededSize = ftp.getTaskToatalSize(downloadPaths);
			downloadedSize = remoteTotalSize - remoteneededSize;
			DownloadStatus status;
			if (remoteneededSize - downedsize > localToatalSize) {
				SendRequest.sendProcessedMsg("-1", taskId, TaskName);
				return DOWNLOAD_ERROR_NO_SUFFICENT_SPACE;
				// 空间不足在这里处理
			} else {
				for (int i = 0; i < downloadPaths.size(); i++) {
					String path = downloadPaths.get(i);
					long filesize = ftp.getFileSize(path);
					currentTicks = System.currentTimeMillis();
					if (currentTicks - lastConnTicks > FTP_DOWNLOAD_MAX_INTERVAL) {
						ftp.closeConnect();
						ftp.openConnect();
					}
					try {
						status = ftp.downLoad(path, CoofileTouchApplication.getAppResBasePath(),
										new processedListener() {
											@Override
											public void onProcesseChange(long downloadsize) {
												if (downloadsize > MIN_SIZE) {
													proccessed = df.format((downloadedSize + (downloadsize / 1024))
																	* 100 / remoteTotalSize);
													SendRequest.sendProcessedMsg(proccessed, taskId, TaskName);
												}
											}
										});

						if (status.isSucceed()) {

							downloadedSize += (filesize / 1024);
							// 下载成功
							if ((i == downloadPaths.size() - 1) && errorPath.size() == 0) {
								SendRequest.sendProcessedMsg("100.00", taskId, TaskName);
								return DOWNLOAD_SUCCESS;
							}
						} else {
							if ("fileover".equals(status.getResponse())) {
								return DOWNLOAD_USER_STOP_DOWNLOAD;
							}
							// 下载失败
							errorPath.add(path);
							LogUtil.printApointedLog(Download.class, "status: " + status.getResponse(), LogType.error);
							LogUtil.printApointedLog(Download.class, "FileName : " + path , LogType.error);
						}
					}
					catch (IOException e) {
						SendRequest.sendProcessedMsg("-1", taskId, TaskName);
						e.printStackTrace();
						return DOWNLOAD_ERROR_FILE_NOT_FOUND;
					}

				}
				ftp.closeConnect();
				if (errorPath.size() != 0) {
					SendRequest.sendProcessedMsg("-1", taskId, TaskName);
					try {
						int downloadcount = json.getInt("downloadCount");
						// 为什么是4次
						if (downloadcount < 4) {
							DownloadQueue.addTask(json);
						}
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
					return DOWNLOAD_ERROR_FILE_NOT_FOUND;
				}
			}
		}
		SendRequest.sendProcessedMsg("-1", taskId, TaskName);
		return DOWNLOAD_ERROR_UNKNOWN;
	}

	/**
	 * 筛选出本地不能存在的文件
	 * */
	private long filepathsfilter() {

		if (paths.size() == 0) {
			downloadPaths = new ArrayList<String>();
		}
		downloadPaths = new ArrayList<String>();
		for (int i = 0; i < paths.size(); i++) {
			String path = paths.get(i);
			long length = checkFileLocal(path);
			if (length >= 0) {
				adcategorysize += length;
				downloadPaths.add(path);
			}
		}
		return adcategorysize;
	}

	/**
	 * 检查一个文件在本地是否存在
	 * */
	private long checkFileLocal(String remotepath) {
		String localFileFullName = null;
		File file = new File(remotepath);
		File file2 = new File(CoofileTouchApplication.getAppResBasePath() + File.separator + file.getParent());
		if (!file2.exists()) {
			file2.mkdirs();
		}
		// 检查本地文件是否存在
		try {
			localFileFullName = file2.getCanonicalPath() + File.separator + file.getName();
			File file3 = new File(localFileFullName);
			File file5 = new File(file2.getCanonicalPath() + File.separator + file.getName() + ".codr");
			if (file3.exists() && !file5.exists()) {
				if (localFileFullName.endsWith(".xml")) {
					file3.delete();
				} else {
					return -1;
				}
			}
			if (file3.exists() && file5.exists()) {
				file3.delete();
				file5.delete();
			}
			File file4 = new File(file2.getCanonicalPath() + File.separator + file.getName() + ".ad");
			if (file4.exists()) {
				return file4.length();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算本地空间剩余大小
	 * */
	private boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}

	public long getSDFreeSize() {
		// 取得SD卡文件路径
		if (ExistSDCard()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(path.getPath());
			// 获取单个数据块的大小(Byte)
			long blockSize = sf.getBlockSize();
			// 空闲的数据块的数量
			long freeBlocks = sf.getAvailableBlocks();
			// 返回SD卡空闲大小
			return (freeBlocks * blockSize) / 1024; // 单位KB
		}
		return 1;
	}

	/**
	 * 下载单个文件
	 * 
	 * @param local
	 *            文件本地存储路径
	 * @param remote
	 *            文件远程路径
	 * */
	public DownloadStatus downloadFile(String remotePath, String fileName, String localPath) {
		UploadHelper ftp = UploadHelper.getInstance();
		DownloadStatus ds = null;
		try {
			ftp.openConnect();
			ds = ftp.download(remotePath, fileName, localPath);
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ftp != null) {
				ftp.closeConnect();
			}
		}
		return ds;
	}

}
