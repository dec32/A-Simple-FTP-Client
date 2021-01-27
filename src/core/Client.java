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
	public void delete(String name,String curpath) throws IOException {
       System.out.println("curpath: "+curpath+"  delete: "+name);
		//ɾ����ָ���ļ�
		FTPFile[] ftpFiles = ftpClient.listFiles();
		for(FTPFile f : ftpFiles){
			if(name.equals(f.getName())){
				if(f.isFile()){ //������ļ���ֱ��ɾ��
					System.out.println("isfile");
					ftpClient.deleteFile(name);
				}
				else if(f.isDirectory()){ //������ļ��У���ɾ���ļ����е����ݣ���ɾ���ļ���
					System.out.println("isDirectory");
				    /*
					 * ����ftpClientֻ��ɾ�����ļ��У�����ɾ�������ļ����ļ���
					 * -->������Ҫ�Ƚ��ļ����е��ļ�ɾ������ɾ�����ļ���
					 */
					String new_path;
					if(curpath.equals("/"))  new_path = curpath+name;
					else  new_path = curpath + "/" +name;
                    
					removeAllFiles(new_path);
				
					//ɾ�������ļ��У����ļ��д�ʱΪ��
					ftpClient.changeWorkingDirectory(curpath);
					ftpClient.removeDirectory(name);
				}

			}
		}


	}


	public void removeAllFiles(String curpath) throws IOException{ //ɾ���ļ����е������ļ��Լ��������ļ���
//		System.out.println("curpath name : "+ curpath);
		ftpClient.changeWorkingDirectory(curpath); //�ѵ�ǰ·���л���Ҫɾ�����ļ�����
		FTPFile[] ftpFiles = ftpClient.listFiles();
		
		if(ftpFiles.length==0){ //�����ɾ�ļ�����Ϊ��
			
			String fatherDire = curpath.substring(0, curpath.lastIndexOf("/")); //��ȡ��Ŀ¼
			System.out.println("len 0 fatherDire: "+fatherDire);
			System.out.println("delete curpath" + curpath);
			ftpClient.changeWorkingDirectory(fatherDire);
			System.out.println(ftpClient.removeDirectory(curpath) ); //�Ƿ�ɹ�ɾ��
		}
		
		if(ftpFiles.length>0 )
		for(FTPFile f : ftpFiles){
			if(f.isFile()){ //���ļ���ֱ��ɾ��
				ftpClient.deleteFile(f.getName());
			}
			else{ //����Ǹ��ļ��У��ݹ�
				String subDirectory = curpath+"/"+f.getName();

				removeAllFiles(subDirectory);

				//�л�����Ŀ¼��ɾ���ļ���
				String fatherDire = subDirectory.substring(0, subDirectory.lastIndexOf("/"));
				System.out.println("len >0 fatherDire: "+fatherDire);
				System.out.println("len >0 curpath "+curpath);
				ftpClient.changeWorkingDirectory(fatherDire);
				boolean flag = ftpClient.removeDirectory(curpath);
				System.out.println("remove directory : "+ flag);
			}
		}
	    //�л�����Ŀ¼
//		ftpClient.changeWorkingDirectory(name.substring(0, name.lastIndexOf("/")));
//		ftpClient.removeDirectory(name);




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

	}





	//getters & setters
	public List<String> getFolderList() {
		return folderList;
	}

	public List<String> getFileList() {
		return fileList;
	}


}
