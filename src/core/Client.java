package core;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/*
 * TODO: ������ķ�������������ֵ(����getCurFileList)������������Ҫ֪�������Ƿ�˳��ִ��
 * һ���򵥵Ľ�������Ǹ����з�������һ��bool����ֵ��true����˳��ִ�У�false�����������쳣��ִ��ʧ��
 * ��������һ�����⣺ʧ�����кܶ���ԭ��ģ�ȡ����catch�����쳣���ͣ�
 * ���Ը�д�İ취�Ǹ����з�������throws��������֪�����߾����ʧ������
 * ��֮����Ҫ�ô�Ҷ�������쳣��������Ϊ��������������GUI��json���Ϲ�ϵ��
 */
public class Client {
	private FTPClient ftpClient; 
	
	public Client() {
		
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
	public void download(String ftpPath, String localPath) {
		if(ftpClient==null) {
			return;//δ��¼���ֹ����
		}
		String parentFolderPath = Util.getParentFolderPath(ftpPath);
		String fileName = Util.getFileName(ftpPath);
		try {
			ftpClient.changeWorkingDirectory(parentFolderPath);//ת��Ŀ���ļ����ڵĸ�Ŀ¼
			FTPFile[] ftpFiles = ftpClient.listFiles();//��ȡĿ¼�е������ļ�
			for(FTPFile f:ftpFiles) {//Ѱ��Ŀ���ļ�
				if(fileName.equals(f.getName())) {
					File dst = new File(localPath+"/"+fileName);//destination Ŀ��λ��
					FileOutputStream fos = new FileOutputStream(dst);
					ftpClient.retrieveFile(fileName, fos);
					fos.close();
					System.out.println("���سɹ�");
					return;
				}
			}		
		} catch (Exception e) {
			System.out.println("����ʧ��");
			e.printStackTrace();
		}
	}
	
	/*
	 * todo���ϴ�Ӧ��ʹ�ö��߳���ʵ��
	 */
	public void upload(String ftpPath, String localPath) {
		if(ftpClient==null) {
			return;//δ��¼���ֹ�ϴ�
		}
		try {
			ftpClient.changeWorkingDirectory(ftpPath);//ת��Ŀ��Ŀ¼
			
			System.out.println("�ϴ��ɹ�");
		} catch (Exception e) {
			System.out.println("�ϴ�ʧ��");
			e.printStackTrace();
		}
	}
	
	public void delete(String ftpPath) {
		
	}
	
	public void rename(String ftpPath, String newName) {
		if(ftpClient==null) {
			return;//δ��¼���ֹ������
		}
	}
	
	public void cd() {
		
	}
	
	public void getCurFileList() {
		
	}
	
	public void md() {
		
	}
	
	public void logout() {
		if(ftpClient==null) {
			return;//δ��¼�򲻱�Ҫ����
		}
	}

}
