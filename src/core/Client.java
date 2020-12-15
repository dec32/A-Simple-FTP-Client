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
 * 这里面的方法都不带返回值(除了cd)，但是我们需要知道方法是否顺利执行
 * 一个简单的解决方法是给所有方法加上一个boolean返回值，true代表顺利执行，false代表遇到了异常，执行失败
 * 但是这样太低级了, 想要变强就不能用低级的方法
 * 所以改写的办法是给所有方法加上throws声明，告知调用者具体的异常类型
 * （之所以要用讨厌的异常机制是因为不想在这个类里和GUI和json扯上关系）
 */
public class Client {
	private FTPClient ftpClient;
	List<String> folderList = new ArrayList<String>();
	List<String> fileList = new ArrayList<String>();
	public Client() {
		
	}
	
	public void login(String address, int port, String username, String password) throws SocketException, IOException {

		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8"); // 设置编码为utf-8
		ftpClient.connect(address, port);// 连接到服务器
		ftpClient.login(username, password);// 登录
		int replyCode = ftpClient.getReplyCode(); // 查看登录状态
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			System.out.println("连接失败");
			throw new IOException();
		}
		System.out.println("连接成功");
	}
	
	
	//转到指定的目录下(只支持绝对路径, 后续考虑支持相对路径)
	public FTPFile[] cd(String ftpPath) throws IOException {
		ftpClient.changeWorkingDirectory(ftpPath);
		FTPFile[] ftpFiles = ftpClient.listFiles();
		//复制一份到folderList和fileList中
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
	 * 把指定的本地文件上传到当前目录
	 * TODO: 下载操作应该使用多线程来实现
	 */
	public void download(String fileName, String localPath) throws IOException {

		FTPFile[] ftpFiles = ftpClient.listFiles();// 获取目录中的所有文件
		for (FTPFile f : ftpFiles) {// 寻找目标文件
			if (fileName.equals(f.getName())) {
				File dst = new File(localPath + "/" + fileName);// destination 目标位置
				FileOutputStream fos = new FileOutputStream(dst);
				ftpClient.retrieveFile(fileName, fos);
				fos.close();
				System.out.println("下载成功");
				return;
			}
		}

	}
	
	/*
	 * 把指定的本地文件上传到当前目录
	 * TODO: 上传应该使用多线程来实现
	 */
	public void upload(String localPath){
					
		System.out.println("上传成功");
		System.out.println("上传失败");

	}
	
	/*
	 * 删除当前目录中指定的文件或文件夹
	 */
	public void delete(String name) {
		
	}
	
	/*
	 * 给当前目录中指定的文件或文件夹重命名
	 */
	public void rename(String name, String newName) {

	}
	
	/*
	 * 下线
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
