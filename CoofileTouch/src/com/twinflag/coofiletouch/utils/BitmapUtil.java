package com.twinflag.coofiletouch.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.twinflag.coofiletouch.CoofileTouchApplication;

final public class BitmapUtil {

	private static final String TAG = "BitmapUtil";
	private static final boolean SHOW_LOG_FLAG = true;
	private static final int ONE_KB = 1024;
	private static final int IMAGE_FILE_MAX_SIZE = 300 * ONE_KB;
	private static final boolean DELETE_ORIGINAL_FILE = true;

	public static String path;

	public static void compressBitmap(String filePath, int desWidth, int dstHeight) {

		int compressOptions = 100;
		boolean isJPGFile = true;
		File originalFile = null;
		String fileExtensionName = null;
		File compressedFile = null;
		File originalSavedFile = null;

		fileExtensionName = getFileExtension(filePath);
		if (fileExtensionName != null
						&& (fileExtensionName.equalsIgnoreCase("jpg") || fileExtensionName.equalsIgnoreCase("jpeg") || fileExtensionName
										.equalsIgnoreCase("png"))) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			options.inSampleSize = calcInSampleSize(options, desWidth, dstHeight);
			options.inJustDecodeBounds = false;

			if (options.inSampleSize < 1) {
				if (SHOW_LOG_FLAG) {
					Log.i(TAG, "====>>>options.inSampleSize = " + options.inSampleSize);
				}
				return;
			}

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			if (bitmap == null) {
				if (SHOW_LOG_FLAG) {
					Log.i(TAG, "====>>>decode bitmap failed!!");
				}
				return;
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if (fileExtensionName.equalsIgnoreCase("png")) {
				isJPGFile = false;
			} else {
				isJPGFile = true;
			}

			while (baos.toByteArray().length > IMAGE_FILE_MAX_SIZE && compressOptions > 30) {
				baos.reset();
				if (isJPGFile) {
					bitmap.compress(Bitmap.CompressFormat.JPEG, compressOptions, baos);
				} else {
					bitmap.compress(Bitmap.CompressFormat.PNG, compressOptions, baos);
				}
				compressOptions -= 10;
			}

			compressedFile = new File(filePath + ".tmp");
			try {
				FileOutputStream out = new FileOutputStream(compressedFile);
				out.write(baos.toByteArray());
				out.flush();
				out.close();
				if (SHOW_LOG_FLAG) {
					Log.i(TAG, "====>>>Compress success!!!");
				}
				originalFile = new File(filePath);
				if (DELETE_ORIGINAL_FILE) {
					originalFile.delete();
				} else {
					originalSavedFile = new File(filePath + "." + fileExtensionName);
					originalFile.renameTo(originalSavedFile);
					originalFile = new File(filePath);
				}
				compressedFile.renameTo(originalFile);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (SHOW_LOG_FLAG) {
				Log.i(TAG, "====>>>cannot compress files with extension: " + fileExtensionName);
			}
		}
	}

	private static int calcInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	private static String getFileExtension(String filename) {
		String extensionName = null;
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				extensionName = filename.substring(dot + 1);
			}
		}
		return extensionName;
	}

	public static Bitmap getBitmap(String src, int width, int height) {
		try {
			path = CoofileTouchApplication.getAppResBasePath() + src;
			// File file = new File(path);
			// if (!file.exists()) {
			// path = "/" + src;
			// }
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			options.inJustDecodeBounds = false;
			int w = options.outWidth;
			int h = options.outHeight;
			int inSampleSize = 1;
			if (width != 0 && w > h && w > width) {
				inSampleSize = (int) (options.outWidth / width);
			} else if (height != 0 && h > w && h > height) {
				inSampleSize = (int) (options.outHeight / height);
			}
			if (inSampleSize <= 0) {
				inSampleSize = 1;
			}
			options.inSampleSize = inSampleSize;
			return BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			return null;
		}
	}

}
