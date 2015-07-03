package com.twinflag.coofiletouch.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.util.Log;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.utils.BitmapUtil;
import com.twinflag.coofiletouch.utils.Coder;
import com.twinflag.coofiletouch.value.GlobalValue;
import com.twinflag.coofiletouch.value.GlobalVariable;

public class UploadHelper {

	private boolean connected = false;
	private List<FTPFile> list;// FTP远程文件集合
	private FTPClient ftpClient;// FTPClient的客户端
	private Coder coder;// 解码对象
	private boolean isover = false;// 结束下载任务
	private static UploadHelper uploadHelper = null;// 主机构造器
	private static UploadHelper ftpHelper2 = null;

	private UploadHelper() {
		ftpClient = new FTPClient();
		coder = new Coder();
		list = new ArrayList<FTPFile>();
	}

	/**
	 * FTP服务器获得一个单例
	 */
	public static UploadHelper getInstance() {
		if (uploadHelper == null) {
			uploadHelper = new UploadHelper();
		}
		return uploadHelper;
	}

	public static UploadHelper newInstance() {
		if (ftpHelper2 == null) {
			ftpHelper2 = new UploadHelper();
		}
		return ftpHelper2;
	}

	/**
	 * 连接到FTP服务器
	 */
	public DownloadStatus openConnect() {
		isover = false;
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");// 设置编码方式
		int reply;// 访问远程返回码
		try {
			ftpClient.connect(GlobalValue.options.ftp_ip, GlobalValue.options.ftp_port);// 连接到远程服务器
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		reply = ftpClient.getReplyCode();// 获得链接的返回码
		if (!FTPReply.isPositiveCompletion(reply)) {// 如果返回码不正确
			try {
				ftpClient.disconnect();// 断开服务器链接
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return new DownloadStatus("服务器连接失败");
		}

		boolean success = false;// 登录FTP服务器
		try {
			success = ftpClient.login(GlobalValue.options.ftp_username, GlobalValue.options.ftp_password);
			if (success) {
				this.connected = true;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {// FTP服务器登录失败
			try {
				ftpClient.disconnect();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return new DownloadStatus("服务器登录失败");
		}
		else {
			try {
				FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType());
				config.setServerLanguageCode("zh");
				ftpClient.configure(config);
				// 设置接受模式为被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置文件类型兼容字节
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			}
			catch (IOException e) {
				e.printStackTrace();
				return new DownloadStatus("服务器登录失败");
			}
			return new DownloadStatus("登录服务器成功");
		}
	}

	/**
	 * 检验FTP服务器是否链接
	 * */
	public boolean isConnected() {
		return ftpClient.isConnected();
	}

	public boolean isconnected() {
		return this.connected;
	}

	/**
	 * 下载远程文件到本地
	 * 
	 * @param remotePath
	 *            远程文件的路径
	 * @param localPath
	 *            本地文件的路径
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * */

	public DownloadStatus download(String remotePath, String localPath) throws IOException {
		File file = new File(remotePath);// 创建本地目录文件夹
		File file2 = new File(localPath + "/" + file.getParent());// 为了让本地文件夹和FTP服务器文件目录一致
		if (!file2.exists()) {
			file2.mkdirs();
		}
		String localFileFullName = file2.getCanonicalPath() + "/" + file.getName();// 检查本地文件是否存在
		File file3 = new File(localFileFullName);
		File file5 = new File(file2.getCanonicalPath() + "/" + file.getName() + ".codr");
		if (file3.exists() && !file5.exists()) {
			return new DownloadStatus(true, "本地文件已经存在");
		}
		if (file3.exists() && file5.exists()) {
			file3.delete();
			file5.delete();
		}
		FTPFile[] files = ftpClient.listFiles(remotePath);
		if (files.length == 0) {
			return new DownloadStatus(false, "远程文件不存在");
		}
		long remoteSize = files[0].getSize();// 获取远程文件的大小
		File file4 = new File(file2.getCanonicalPath() + "/" + files[0].getName() + ".ad");// 检查本地未下载完成的文件
		if (file4.exists()) {
			long localSize = file4.length();
			FileOutputStream fos = new FileOutputStream(file4, true);// 下面是进行断点续传
			ftpClient.setRestartOffset(localSize);
			InputStream is = ftpClient.retrieveFileStream(remotePath);
			byte[] buffer = new byte[1024];
			int len = 0;
			long step = remoteSize / 100;
			long process = localSize / step;
			long length = 0;
			while ((len = is.read(buffer)) >= 0) {
				fos.write(buffer, 0, len);
				Log.i("UploadHelper", "length = " + length);
				localSize += len;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
				}
			}
			is.close();
			fos.close();
			boolean isDone = ftpClient.completePendingCommand();
			if (isDone) {
				file4.renameTo(file3);
				if (GlobalVariable.isencrypt && !file3.getName().endsWith(".apk")) {
					coder.decode(file3.getCanonicalPath());
				}
				BitmapUtil.compressBitmap(file3.getCanonicalPath(), CoofileTouchApplication.getScreenWidth(), CoofileTouchApplication.getScreenHeight());
				return new DownloadStatus(true, "下载完成");
			}
			return new DownloadStatus(false, "断点续传失败");
		}
		else {
			OutputStream out = new FileOutputStream(file4, true);
			InputStream is = ftpClient.retrieveFileStream(remotePath);
			byte[] bytes = new byte[1024];
			long step = remoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				out.write(bytes, 0, len);
				localSize += len;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
				}
			}
			is.close();
			out.close();
			boolean newDone = ftpClient.completePendingCommand();
			if (newDone) {
				file4.renameTo(file3);
				if (GlobalVariable.isencrypt && !file3.getName().endsWith(".apk")) {
					coder.decode(file3.getCanonicalPath());
				}
				BitmapUtil.compressBitmap(file3.getCanonicalPath(), CoofileTouchApplication.getScreenWidth(), CoofileTouchApplication.getScreenHeight());
				return new DownloadStatus(true, "新文件下载成功");
			}
			return new DownloadStatus(false, "新文件下载失败");
		}

	}

	public DownloadStatus downLoad(String remotePath, String localPath, processedListener listener) throws IOException {
		File file = new File(remotePath);// 为了让本地文件夹和FTP服务器文件目录一致 创建本地目录文件夹
		File file2 = new File(localPath + file.getParent());
		if (!file2.exists()) {
			file2.mkdirs();
		}
		String localFileFullName = file2.getCanonicalPath() + File.separator + file.getName();
		File file3 = new File(localFileFullName);
		FTPFile[] files = ftpClient.listFiles(remotePath);
		if (files.length == 0) {
			return new DownloadStatus(false, "远程文件不存在");
		}
		long remoteSize = files[0].getSize();// 获取远程文件的大小
		File file4 = new File(file2.getCanonicalPath() + File.separator + files[0].getName() + ".ad");// 检查本地未下载完成的文件
		if (file4.exists()) {
			long localSize = file4.length();
			FileOutputStream fos = new FileOutputStream(file4, true);// 下面是进行断点续传
			ftpClient.setRestartOffset(localSize);
			InputStream is = ftpClient.retrieveFileStream(remotePath);
			byte[] buffer = new byte[1024 * 10];
			int len = 0;
			long step = remoteSize / 100;
			long process = localSize / step;
			while ((len = is.read(buffer)) >= 0) {
				fos.write(buffer, 0, len);
				localSize += len;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					listener.onProcesseChange(localSize);
				}
				if (isover) {
					is.close();
					fos.close();
					return new DownloadStatus(false, "fileover");
				}
			}
			is.close();
			fos.close();
			boolean isDone = ftpClient.completePendingCommand();
			if (isDone) {
				file4.renameTo(file3);
				if (GlobalVariable.isencrypt && !file3.getName().endsWith(".apk")) {
					coder.decode(file3.getCanonicalPath());
				}
				BitmapUtil.compressBitmap(localFileFullName, CoofileTouchApplication.getScreenWidth(), CoofileTouchApplication.getScreenHeight());
				return new DownloadStatus(true, "下载完成");
			}

			return new DownloadStatus(false, "断点续传失败");
		}
		else {
			OutputStream out = new FileOutputStream(file4, true);
			InputStream is = ftpClient.retrieveFileStream(remotePath);
			byte[] bytes = new byte[1024 * 10];
			long step = remoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				out.write(bytes, 0, len);
				localSize += len;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					listener.onProcesseChange(localSize);
					if (isover) {
						is.close();
						out.close();
						return new DownloadStatus(false, "fileover");
					}
				}
			}
			is.close();
			out.close();
			boolean newDone = ftpClient.completePendingCommand();
			if (newDone) {
				file4.renameTo(file3);
				if (GlobalVariable.isencrypt && !file3.getName().endsWith(".apk")) {
					coder.decode(file3.getCanonicalPath());
				}
				BitmapUtil.compressBitmap(localFileFullName, CoofileTouchApplication.getScreenWidth(), CoofileTouchApplication.getScreenHeight());
				return new DownloadStatus(true, "新文件下载成功");
			}
			return new DownloadStatus(false, "新文件下载失败");
		}

	}

	public interface processedListener {
		public void onProcesseChange(long downloadsize);
	}

	/**
	 * 下载更新文件
	 * */
	public DownloadStatus download(String remotePath, String fileName, String localPath) throws IOException {
		DownloadStatus result = null;
		File fileDirectory = new File(localPath);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
		ftpClient.changeWorkingDirectory(remotePath);// 更改FTP目录
		FTPFile[] ftpFiles = ftpClient.listFiles();// 得到FTP当前目录下所有文件
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.getName().equals(fileName)) {// 找到需要下载的文件
				File file = new File(localPath + "/" + fileName);// 创建本地目录
				if (file.exists()) {
					file.delete();
				}
				OutputStream outputStream = new FileOutputStream(file);// 下载当个文件
				ftpClient.retrieveFile(file.getName(), outputStream);// 下载单个文件
				outputStream.close();// 关闭文件流
				result = new DownloadStatus(true, "下载更新文件成功");// 下载完时间 返回值
			}
		}
		return result;
	}

	/**
	 * 获得下载的文件的大小 单位KB
	 */
	public long getTaskToatalSize(List<String> paths) {
		long totalSize = 0L;
		for (int i = 0; i < paths.size(); i++) {
			String path = paths.get(i);
			try {
				FTPFile[] files = ftpClient.listFiles(path);
				if (files.length == 1) {
					FTPFile ff = files[0];
					totalSize += ff.getSize();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return totalSize / 1024;
	}

	public long getFileSize(String path) {
		long totalsize = 0L;
		FTPFile[] files;
		try {
			files = ftpClient.listFiles(path);
			if (files.length == 1) {
				FTPFile ff = files[0];
				totalsize = ff.getSize();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return totalsize;
	}

	/**
	 * 上传本地文件到服务器
	 */
	public void upload(String local, String remote) throws IOException {
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			if (!CreateDirectory(remote)) {
				return;
			}
		}
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("UTF-8"), "iso-8859-1"));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if (remoteSize == localSize) {// 本地文件在远程文件中已经存在
				return;
			}
			else if (remoteSize > localSize) {
				return;
			}
			uploadFile(remoteFileName, f, remoteSize);
		}
		else {
			File f = new File(local);
			uploadFile(remoteFileName, f, 0);
		}

	}

	/**
	 * 上传本地日志文件到远程服务器
	 */
	public boolean uploadLogs(String local, String remote) throws IOException {
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("Unicode");// 上传文件设置编码格式
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			if (!CreateDirectory(remote)) {
				return false;
			}
		}
		File f = new File(local);
		return uploadFile(remoteFileName, f);
	}

	/**
	 * 上传本地文件到远程的续传
	 */
	public void uploadFile(String remoteFileName, File localFile, long remoteSize) throws IOException {
		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		OutputStream out = ftpClient.appendFileStream(remoteFileName);
		if (out == null) {
			String message = ftpClient.getReplyString();
			raf.close();
			throw new RuntimeException(message);
		}

		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			// System.out.println("%" + process);
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = raf.read(buffer)) != -1) {
			out.write(buffer, 0, len);
			localreadbytes += len;
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				// System.out.println(localFile.getName() + "下载进程:" + process);
			}
		}
		out.flush();
		raf.close();
		out.close();
		ftpClient.completePendingCommand();
	}

	/**
	 * 直接上传文件的方法
	 * 
	 * @param remoteFile
	 *            远程文件
	 * @param localFile
	 *            本地文件
	 * @throws IOException
	 * */
	public boolean uploadFile(String remoteFile, File localFile) throws IOException {
		InputStream in = new FileInputStream(localFile);
		boolean flag = ftpClient.storeFile(remoteFile, in);
		in.close();
		return flag;
	}

	/**
	 * 上传文件夹到服务器
	 * 
	 * @param local
	 *            本地文件夹
	 * @param remote
	 *            远程文件夹路径
	 * @throws IOException
	 * */
	public boolean uploadList(String local, String remote) throws IOException {
		boolean success = false;
		File file = new File(local);
		if (!file.exists()) {
			return success;
		}
		if (!file.isDirectory()) {
			return success;
		}
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.exists()) {
				if (f.isDirectory()) {
					this.uploadList(f.getAbsolutePath().toString(), remote);
				}
				else {
					String localpath = f.getCanonicalPath().replaceAll("\\\\", "/");
					String remotepath = remote + localpath.substring(localpath.indexOf("/") + 1);
					upload(localpath, remotepath);
					ftpClient.changeWorkingDirectory("/");
				}
			}
		}
		return true;
	}

	/**
	 * 创建不存在的文件
	 * 
	 * @param remote
	 *            远程文件路径
	 * */
	public boolean CreateDirectory(String remote) throws IOException {
		boolean success = true;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
						&& !ftpClient.changeWorkingDirectory(new String(directory.getBytes("UTF-8"), "iso-8859-1"))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			}
			else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("UTF-8"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					}
					else {
						success = false;
					}
				}
				start = end + 1;
				end = directory.indexOf("/", start);
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * 关闭FTP服务器
	 * 
	 * @throws IOException
	 * */
	public DownloadStatus closeConnect() {
		this.isover = true;
		if (ftpClient != null) {

			try {
				// 登出服务器
				ftpClient.logout();
				ftpClient.disconnect();
				this.connected = false;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new DownloadStatus("服务器断开");
	}

	/**
	 * 列出远程文件
	 * 
	 * @param remotePath
	 *            远程文件路径
	 * @return FTPFile 远程文件夹或者文件的集合
	 * @throws IOException
	 */
	public List<FTPFile> listFiles(String remotePath) throws IOException {
		// 远程目录中的所有的文件
		FTPFile[] files = ftpClient.listFiles(remotePath);
		// 遍历文件
		for (FTPFile file : files) {
			list.add(file);
		}
		return list;
	}
}
