package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	public void md(String name) throws IOException {
		boolean success = ftpClient.makeDirectory(name); //����Ŀ¼
		if(success == false) {
			throw new IOException();
		}
	}


	/*
	 * FTP������ļ����ص������ļ��ĵ�ǰĿ¼��
	 * TODO: ���ز���Ӧ��ʹ�ö��߳���ʵ�֣�GUI���ô˷���ʱ�½�һ���̣߳�
	 */
	public void download(String fileName, String localPath) throws IOException { //filename:FTP�����ĳ�ļ������·����localpath�������ļ�Ŀ¼�ľ���·��
          System.out.println("fileName: " + fileName + " localPath: "+localPath);
		FTPFile[] ftpFiles = ftpClient.listFiles();// ��ȡĿ¼�е������ļ�,��FTP��ǰĿ¼�������ļ�
		System.out.println(ftpFiles.length);
		for (FTPFile f : ftpFiles) {
			System.out.println(f.getName()+"  yes right");
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
	 * ��ָ���ı����ļ��ϴ���������ļ��ĵ�ǰĿ¼
	 * TODO: �ϴ�Ӧ��ʹ�ö��߳���ʵ�֣�GUI���ô˷���ʱ�½�һ���̣߳�
	 */
	public void upload(String localPath) throws IOException{

		File localfile = new File(localPath);
		FileInputStream fis = new FileInputStream(localfile);
		if(ftpClient.storeFile(localfile.getName(), fis))
		System.out.println("�ϴ��ɹ�");
		else
		System.out.println("�ϴ�ʧ��");

	}

	/*
	 * ɾ����ǰĿ¼��ָ�����ļ����ļ���
	 */
	public void delete(String name) throws IOException {
		FTPFile[] ftpFiles = ftpClient.listFiles();
		for(FTPFile f : ftpFiles){
			if(name.equals(f.getName())){
				if(f.isFile()){ //������ļ���ֱ��ɾ��
					ftpClient.deleteFile(name);
				}
				//������ļ��У���ɾ���ļ����е����ݣ���ɾ���ļ���
				else if(f.isDirectory()){ 
					rd(name);					
				}
			}
		}
		
		boolean isExist = false;
		ftpFiles = ftpClient.listFiles();
		for(FTPFile f: ftpFiles){
			if(name.equals(f.getName())){
				isExist = true;
				break;
			}
		}
		if(isExist) System.out.println("ɾ��ʧ�ܣ�");
		else System.out.println("ɾ���ɹ���");
	}

	
	public void rd(String name) throws IOException {
		ftpClient.changeWorkingDirectory(name);//�ѵ�ǰ·���л���Ҫɾ�����ļ�����
		FTPFile[] ftpFiles = ftpClient.listFiles();
		for(FTPFile f:ftpFiles) {
			if(f.isDirectory()) {
				rd(f.getName());//����Ǹ��ļ��У��ݹ�
			}else {
				ftpClient.deleteFile(f.getName());//���ļ���ֱ��ɾ��
			}
		}
		ftpClient.changeToParentDirectory();//���ɾ�������󣬻ص���Ŀ¼
		ftpClient.removeDirectory(name);//���Ƴ������Ŀ¼
		
	}
//	public void removeAllFiles(String name) throws IOException{ //ɾ���ļ����е������ļ��Լ��������ļ���
////		System.out.println("curpath name : "+ curpath);
//		ftpClient.changeWorkingDirectory(name); //�ѵ�ǰ·���л���Ҫɾ�����ļ�����
//		FTPFile[] ftpFiles = ftpClient.listFiles();
//		if(ftpFiles.length==0){ //�����ɾ�ļ�����Ϊ��
//			ftpClient.changeToParentDirectory();
//			return;
//		}
//		for(FTPFile f : ftpFiles){
//			if(f.isFile()){ //���ļ���ֱ��ɾ��
//				ftpClient.deleteFile(f.getName());
//			}else{//����Ǹ��ļ��У��ݹ�
//				removeAllFiles(f.getName());
//				ftpClient.removeDirectory(f.getName());
//				ftpClient.changeToParentDirectory();
//			}
//		}	   
//	}
	
	public void back() throws IOException {
		ftpClient.changeToParentDirectory();
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
	}


	/*
	 * ����ǰĿ¼��ָ�����ļ����ļ���������
	 */
	public void rename(String name, String newName) {
		System.out.println("���ļ�"+name+"������Ϊ"+newName);
		try {
			ftpClient.rename(name, newName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;

	}

	/*
	 * ����
	 */
	public void logout() {
		try {
			if(ftpClient.isConnected())
			   ftpClient.logout();
			   System.out.println("�˳���¼�ɹ���");
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   System.out.println("�˳���¼ʧ�ܣ�");
			  }
	}





	//getters & setters
	public List<String> getFolderList() {
		return folderList;
	}

	public List<String> getFileList() {
		return fileList;
	}


}
