package org.profit.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
	public static String getExtension(File file) {
		return (file != null) ? getExtension(file.getName()) : "";
	}
	
	public static String getExtension(String fileName) {
		return getExtension(fileName, "");
	}
	
	public static String getExtension(String fileName, String ext) {
		if (fileName != null && fileName.length() > 0) {
			int i = fileName.lastIndexOf(".");
			if (i > -1 && i < (fileName.length() - 1)) {
				return fileName.substring(i + 1);
			}
		}
		return ext;
	}
	
	public static String trimExtension(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			int i = fileName.lastIndexOf(".");
			if (i > -1 && i < fileName.length()) {
				return fileName.substring(0, i);
			}
		}
		return fileName;
	}
	
	/**
	 * Make file directory
	 * 
	 * @param dir
	 * @return
	 */
	public static String mkdir(String dir) {
		boolean created = false;
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (dirFile.mkdirs()) {
				created = true;
			} else {
				LOG.warn("Make dir failed. Location: {}", dir);
			}
		} else {
			created = true;
		}
		
		if (created) {
			return dir;
		} else {
			return "";
		}
	}
	
	/**
	 * Write file
	 * 
	 * @param filePath
	 * @param content
	 * @param append
	 */
	public static void writeFile(String filePath, String content, boolean append) {
		try {
			StringBuffer sb = new StringBuffer();
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, append));
			
			sb.append(content);
//			sb.append("\r\n");
			
			bw.write(sb.toString());
			bw.flush();
			bw.close();
		} catch (IOException ioe) {
			LOG.error("File write failed.", ioe);
		}
	}
	
	/**
	 * Write bytes
	 * 
	 * @param filePath
	 * @param bytes
	 */
	public static void writeFile(String filePath, byte[] bytes) {
		try {
		    File file = new File(filePath); 
		    FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException ioe) { 
			LOG.error("File write failed.", ioe);
		}
	}
	
	/**
	 * Delete all files
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteAllFile(String path) {
		boolean success = true;

		File file = new File(path);
		if (!file.exists()) {
			success = false;
		}
		if (!file.isDirectory()) {
			success = false;
		}

		if (success) {
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					success &= temp.delete();
				}
			}
		}
		return success;
	}
}
