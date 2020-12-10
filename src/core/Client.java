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
	
	public void login(String ip, int port, String username, String password) {
		
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8"); //设置编码为utf-8
		try {
			ftpClient.connect(ip, port);//连接到服务器
			ftpClient.login(username, password);//登录
			int replyCode = ftpClient.getReplyCode(); //查看登录状态
	        if(!FTPReply.isPositiveCompletion(replyCode)){ 
	        	System.out.println("连接失败");
	        	return;
	        } 
	        System.out.println("连接成功");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	/*
	 * todo: 下载操作应该使用多线程来实现
	 */
	public void download(String ftpPath, String localPath) {
		if(ftpClient==null) {
			return;//未登录则禁止下载
		}
		String parentFolderPath = Util.getParentFolderPath(ftpPath);
		String fileName = Util.getFileName(ftpPath);
		try {
			ftpClient.changeWorkingDirectory(parentFolderPath);//转到目标文件所在的父目录
			FTPFile[] ftpFiles = ftpClient.listFiles();//获取目录中的所有文件
			for(FTPFile f:ftpFiles) {//寻找目标文件
				if(fileName.equals(f.getName())) {
					File dst = new File(localPath+"/"+fileName);//destination 目标位置
					FileOutputStream fos = new FileOutputStream(dst);
					ftpClient.retrieveFile(fileName, fos);
					fos.close();
					System.out.println("下载成功");
					break;
				}
			}		
		} catch (Exception e) {
			System.out.println("下载失败");
			e.printStackTrace();
		}
	}
	
	/*
	 * todo：上传应该使用多线程来实现
	 */
	public void upload(String ftpPath, String localPath) {
		if(ftpClient==null) {
			return;//未登录则禁止上传
		}
		try {
			ftpClient.changeWorkingDirectory(ftpPath);//转到目标目录
			
			System.out.println("上传成功");
		} catch (Exception e) {
			System.out.println("上传失败");
			e.printStackTrace();
		}
	}
	
	public void delete(String ftpPath) {
		
	}
	
	public void rename(String ftpPath, String newName) {
		if(ftpClient==null) {
			return;//未登录则禁止重命名
		}
	}
	
	public void logout() {
		if(ftpClient==null) {
			return;//未登录则不必要下线
		}
	}

}
