package core;

public class Util {
	
	/*
	 * 给定一串完整路径，返回文件的名字
	 */
	public static String getFileName(String path) {
		String[] splited = path.split("/");
		String fileName = splited[splited.length-1];
		return fileName;
	}
	/*
	 * 给定一串完整路径，去掉文件的名字，仅返回前面的部分
	 */
	public static String getParentFolderPath(String path) {
		String[] splited = path.split("/");
		String parentFolderPath = "/";
		for (int i = 1; i < splited.length-1; i++) {
			parentFolderPath+=splited[i];
			parentFolderPath+="/";
		}
		return parentFolderPath;
	}
}
