package core;

public class Util {
	
	/*
	 * ����һ������·���������ļ�������
	 */
	public static String getFileName(String path) {
		String[] splited = path.split("/");
		String fileName = splited[splited.length-1];
		return fileName;
	}
	/*
	 * ����һ������·����ȥ���ļ������֣�������ǰ��Ĳ���
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
