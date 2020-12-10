package core;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Client {
	private FTPClient ftpClient;
	
	public Client() {
		
	}
	
	public void start() {
		login("localhost", 21, "10641", "2131415");
		download("/", "saito asuka.jpg", "D:/ftp download");
	}
	

	public void login(String ip, int port, String username, String password) {
		
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8"); //���ñ���Ϊutf-8
		try {
			ftpClient.connect(ip, port);//���ӵ�������
			ftpClient.login(username, password);//��¼
			int replyCode = ftpClient.getReplyCode(); //�鿴��¼״̬
	        if(!FTPReply.isPositiveCompletion(replyCode)){ 
	        	System.out.println("����ʧ��");
	        	return;
	        } 
	        System.out.println("���ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	/*
	 * todo: ���ز���Ӧ��ʹ�ö��߳���ʵ��
	 */
	public void download(String ftpPath, String fileName, String localPath) {
		if(ftpClient==null) {
			return;//δ��¼���ֹ����
		}
		try {
			ftpClient.changeWorkingDirectory(ftpPath);//ת��Ŀ���ļ����ڵ�Ŀ¼
			FTPFile[] ftpFiles = ftpClient.listFiles();//��ȡĿ¼�е������ļ�
			for(FTPFile f:ftpFiles) {//Ѱ��Ŀ���ļ�
				if(fileName.equals(f.getName())) {
					File dst = new File(localPath+"/"+fileName);//destination Ŀ��λ��
					FileOutputStream fos = new FileOutputStream(dst);
					ftpClient.retrieveFile(fileName, fos);
					fos.close();
					break;
				}
			}
			System.out.println("���سɹ�");
		} catch (Exception e) {
			System.out.println("����ʧ��");
			e.printStackTrace();
		}
	}
	

	public void upload(String ftpPath, String fileName, String localPath) {
		
	}

}
