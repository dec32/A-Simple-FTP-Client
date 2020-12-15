package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/*
 * ������ķ�������������ֵ(����cd)������������Ҫ֪�������Ƿ�˳��ִ��
 * һ���򵥵Ľ�������Ǹ����з�������һ��boolean����ֵ��true����˳��ִ�У�false�����������쳣��ִ��ʧ��
 * ��������̫�ͼ���, ��Ҫ��ǿ�Ͳ����õͼ��ķ���
 * ���Ը�д�İ취�Ǹ����з�������throws��������֪�����߾�����쳣����
 * ��֮����Ҫ��������쳣��������Ϊ��������������GUI��json���Ϲ�ϵ��
 */
public class Client {
	private FTPClient ftpClient;
	List<String> folderList = new ArrayList<String>();
	List<String> fileList = new ArrayList<String>();
	public Client() {
		
	}
	
	public void login(String address, int port, String username, String password) throws SocketException, IOException {

		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8"); // ���ñ���Ϊutf-8
		ftpClient.connect(address, port);// ���ӵ�������
		ftpClient.login(username, password);// ��¼
		int replyCode = ftpClient.getReplyCode(); // �鿴��¼״̬
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			System.out.println("����ʧ��");
			throw new IOException();
		}
		System.out.println("���ӳɹ�");
	}
	
	
	//ת��ָ����Ŀ¼��(ֻ֧�־���·��, ��������֧�����·��)
	public FTPFile[] cd(String ftpPath) throws IOException {
		ftpClient.changeWorkingDirectory(ftpPath);
		FTPFile[] ftpFiles = ftpClient.listFiles();
		//����һ�ݵ�folderList��fileList��
		folderList.clear();
		fileList.clear();
		for(FTPFile f:ftpFiles) {
			if(f.isDirectory()) {
				folderList.add(f.getName());
			}else {
				fileList.add(f.getName());
			}
		}

		
		return ftpFiles;
	}
	
	public void md() {
		
	}
	

	/*
	 * ��ָ���ı����ļ��ϴ�����ǰĿ¼
	 * TODO: ���ز���Ӧ��ʹ�ö��߳���ʵ��
	 */
	public void download(String fileName, String localPath) throws IOException {

		FTPFile[] ftpFiles = ftpClient.listFiles();// ��ȡĿ¼�е������ļ�
		for (FTPFile f : ftpFiles) {// Ѱ��Ŀ���ļ�
			if (fileName.equals(f.getName())) {
				File dst = new File(localPath + "/" + fileName);// destination Ŀ��λ��
				FileOutputStream fos = new FileOutputStream(dst);
				ftpClient.retrieveFile(fileName, fos);
				fos.close();
				System.out.println("���سɹ�");
				return;
			}
		}

	}
	
	/*
	 * ��ָ���ı����ļ��ϴ�����ǰĿ¼
	 * TODO: �ϴ�Ӧ��ʹ�ö��߳���ʵ��
	 */
	public void upload(String localPath){
					
		System.out.println("�ϴ��ɹ�");
		System.out.println("�ϴ�ʧ��");

	}
	
	/*
	 * ɾ����ǰĿ¼��ָ�����ļ����ļ���
	 */
	public void delete(String name) {
		
	}
	
	/*
	 * ����ǰĿ¼��ָ�����ļ����ļ���������
	 */
	public void rename(String name, String newName) {

	}
	
	/*
	 * ����
	 */
	public void logout() {

	}




	
	//getters & setters
	public List<String> getFolderList() {
		return folderList;
	}

	public List<String> getFileList() {
		return fileList;
	}


}
