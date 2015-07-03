package com.twinflag.coofiletouch.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.database.MyDbHelper;
import com.twinflag.coofiletouch.database.ProgramData;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.parse.ParseProgram;
import com.twinflag.coofiletouch.playLog.WriteLogToFile;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalValue;
import com.twinflag.coofiletouch.value.GlobalVariable;

public class FileUtil {

	private MyDbHelper mTaskDataBase;

	public FileUtil(Context context) {
		mTaskDataBase = new MyDbHelper(context);
	}

	public static boolean createFile(File file) {
		if (file.exists()) {
			return true;
		}
		return file.mkdirs();
	}

	public void copyFiles(String path) {
		// rootFolder就是iis目录
		File rootFolder = new File(path);

		// 每个播放单目录
		File[] programFolders = rootFolder.listFiles();
		if (programFolders.length == 0) {
			WriteLogToFile.writeErrorLog("USB导入时：主文件夹iis为空 ");
			return;
		}
		mTaskDataBase.open();
		Coder coder = new Coder();

		// 目标文件夹
		File desFolder = new File(CoofileTouchApplication.getAppResBasePath() + File.separator
						+ Constant.APP_RES_MATERIAL_FOLDER);
		if (!isFolderExiste(desFolder)) {
			createFile(desFolder);
		}

		// 遍历每一个播放单文件夹
		for (File programFolder : programFolders) {
			// 素材部分
			// 素材文件夹：\iis\酒店播放单1\ftpFile\processed
			File processedFolder = new File(programFolder.getPath() + File.separator + Constant.USB_PUBLISH_PROCESSED);
			if (!isFolderExiste(processedFolder)) {
				// 导出文件不完整
				WriteLogToFile.writeErrorLog("USB导入时： " + processedFolder.getPath() + " 不存在");
				continue;
			}
			// 素材文件夹中的所有素材source file destination
			File[] elementFiles = processedFolder.listFiles();
			for (File sourceFile : elementFiles) {
				
				if (sourceFile.isDirectory()) {
					File desOfficeFolder = new File(desFolder + File.separator + sourceFile.getName());
					if (!isFolderExiste(desOfficeFolder)) {
						desOfficeFolder.mkdirs();
					}
					File[] officeFiles = sourceFile.listFiles();
					for (File sourceOfficeFile : officeFiles) {
						copyElement(sourceOfficeFile, desOfficeFolder, coder);
						// File desOfficeFile = new File(desOfficeFolder,
						// sourceOfficeFile.getName());
						// copyFile(sourceOfficeFile, desOfficeFile);
						// BitmapUtil.compressBitmap(desOfficeFile.getCanonicalPath(),
						// CoofileTouchApplication.getScreenWidth(),
						// CoofileTouchApplication.getScreenHeight());
						// if (GlobalValue.options.isencrypt) {
						// coder.decode(desOfficeFile.getCanonicalPath());
						// }

					}
				} else {
					copyElement(sourceFile, desFolder, coder);
					// File desFile = new File(desFolder.getPath() +
					// File.separator + sourceFile.getName());
					// if (!desFile.exists()) {
					// copyFile(sourceFile, desFile);
					// BitmapUtil.compressBitmap(desFile.getCanonicalPath(),
					// CoofileTouchApplication.getScreenWidth(),
					// CoofileTouchApplication.getScreenHeight());
					// if (GlobalValue.options.isencrypt) {
					// coder.decode(desFile.getCanonicalPath());
					// }
					// }
				}
			}

			// 解析xml的部分
			// xml文件夹：\iis\酒店播放单1\ftpFile\programXmlFile
			File programXmlFolder = new File(programFolder.getPath() + Constant.USB_PUBLISH_XMLFOLDER);
			if (!isFolderExiste(programXmlFolder)) {
				// 播放单不完整
				continue;
			}
			File[] xmlFiles = programXmlFolder.listFiles();
			if (xmlFiles.length == 0) {
				continue;
			}
			for (File xmlFile : xmlFiles) {
				StringBuffer XmlString = new StringBuffer();
				File xml = new File(xmlFile.getPath());
				BufferedReader br = null;
				try {
					String line = null;
					br = new BufferedReader(new InputStreamReader(new FileInputStream(xml)));
					while ((line = br.readLine()) != null) {
						XmlString.append(line);
					}
					LogUtil.printLog(FileUtil.class, XmlString.toString());
					br.close();

					ProgramBean task = null;
//							ParseProgram.getProgram(XmlString.toString());
					LogUtil.printLog(FileUtil.class, task.getName());
					String taskName = task.getName();
					String taskId = task.getId();
					String updateDate = task.getUpdateTime();
					List<ProgramData> taskList = new ArrayList<ProgramData>();
					taskList = mTaskDataBase.getFullTasks();
					if (taskList.size() != 0) {
						boolean flag = false;
						for (ProgramData d : taskList) {

							if (taskId.equals(d.getPgId())) {
								flag = true;
								if (!updateDate.equals(d.getUpdateDate())) {
									mTaskDataBase.deleteTask(taskId);
									mTaskDataBase.insertTask(taskId, taskName, XmlString.toString(), "2", updateDate,
													null);
									for (ProgramBean t : GlobalVariable.globalAllPgs) {
										if (t.getId().equals(taskId)) {
											GlobalVariable.globalAllPgs.remove(t);
											break;
										}
									}
									GlobalVariable.pgAdd(task);
									break;
								}

							}
						}
						if (!flag) {

							mTaskDataBase.insertTask(taskId, taskName, XmlString.toString(), "2", updateDate, null);
							GlobalVariable.pgAdd(task);

						}
					} else {
						mTaskDataBase.insertTask(taskId, taskName, XmlString.toString(), "2", updateDate, null);
						GlobalVariable.pgAdd(task);
					}
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// 复制元素的播放
	private void copyElement(File sourceFile, File desFolder, Coder coder) {
		try {
			File desFile = new File(desFolder.getPath() + File.separator + sourceFile.getName());
			if (!desFile.exists()) {
				copyFile(sourceFile, desFile);
				BitmapUtil.compressBitmap(desFile.getCanonicalPath(), CoofileTouchApplication.getScreenWidth(),
								CoofileTouchApplication.getScreenHeight());
				if (GlobalValue.options.isencrypt) {
					coder.decode(desFile.getCanonicalPath());
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void copyFile(File sourceFile, File targetFile) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] buffer = new byte[1024 * 2];
			int len;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			// 刷新此缓冲流
			bos.flush();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return 存在：true;不存在：falsg
	 */
	private Boolean isFolderExiste(File fileName) {
		if (fileName.exists()) {
			return true;
		}
		return false;
	}

}
