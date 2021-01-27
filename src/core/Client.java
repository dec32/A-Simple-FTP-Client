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

	public void md(String name) throws IOException {
		boolean success = ftpClient.makeDirectory(name); //创建目录
		if(success == false) {
			throw new IOException();
		}
	}


	/*
	 * FTP服务端文件下载到本地文件的当前目录下
	 * TODO: 下载操作应该使用多线程来实现（GUI调用此方法时新建一个线程）
	 */
	public void download(String fileName, String localPath) throws IOException { //filename:FTP服务端某文件的相对路径，localpath：本地文件目录的绝对路径
          System.out.println("fileName: " + fileName + " localPath: "+localPath);
		FTPFile[] ftpFiles = ftpClient.listFiles();// 获取目录中的所有文件,即FTP当前目录的所有文件
		System.out.println(ftpFiles.length);
		for (FTPFile f : ftpFiles) {
			System.out.println(f.getName()+"  yes right");
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
	 * 把指定的本地文件上传到服务端文件的当前目录
	 * TODO: 上传应该使用多线程来实现（GUI调用此方法时新建一个线程）
	 */
	public void upload(String localPath) throws IOException{

		File localfile = new File(localPath);
		FileInputStream fis = new FileInputStream(localfile);
		if(ftpClient.storeFile(localfile.getName(), fis))
		System.out.println("上传成功");
		else
		System.out.println("上传失败");

	}

	/*
	 * 删除当前目录中指定的文件或文件夹
	 */
	public void delete(String name) throws IOException {
		FTPFile[] ftpFiles = ftpClient.listFiles();
		for(FTPFile f : ftpFiles){
			if(name.equals(f.getName())){
				if(f.isFile()){ //如果是文件，直接删除
					ftpClient.deleteFile(name);
				}
				//如果是文件夹，先删除文件夹中的内容，再删除文件夹
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
		if(isExist) System.out.println("删除失败！");
		else System.out.println("删除成功！");
	}

	
	public void rd(String name) throws IOException {
		ftpClient.changeWorkingDirectory(name);//把当前路径切换到要删除的文件夹下
		FTPFile[] ftpFiles = ftpClient.listFiles();
		for(FTPFile f:ftpFiles) {
			if(f.isDirectory()) {
				rd(f.getName());//如果是个文件夹，递归
			}else {
				ftpClient.deleteFile(f.getName());//是文件，直接删除
			}
		}
		ftpClient.changeToParentDirectory();//完成删除工作后，回到父目录
		ftpClient.removeDirectory(name);//再移除掉这个目录
		
	}
//	public void removeAllFiles(String name) throws IOException{ //删除文件夹中的所有文件以及它的子文件夹
////		System.out.println("curpath name : "+ curpath);
//		ftpClient.changeWorkingDirectory(name); //把当前路径切换到要删除的文件夹下
//		FTPFile[] ftpFiles = ftpClient.listFiles();
//		if(ftpFiles.length==0){ //如果待删文件夹下为空
//			ftpClient.changeToParentDirectory();
//			return;
//		}
//		for(FTPFile f : ftpFiles){
//			if(f.isFile()){ //是文件，直接删除
//				ftpClient.deleteFile(f.getName());
//			}else{//如果是个文件夹，递归
//				removeAllFiles(f.getName());
//				ftpClient.removeDirectory(f.getName());
//				ftpClient.changeToParentDirectory();
//			}
//		}	   
//	}
	
	public void back() throws IOException {
		ftpClient.changeToParentDirectory();
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
	}


	/*
	 * 给当前目录中指定的文件或文件夹重命名
	 */
	public void rename(String name, String newName) {
		System.out.println("把文件"+name+"重命名为"+newName);
		try {
			ftpClient.rename(name, newName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;

	}

	/*
	 * 下线
	 */
	public void logout() {
		try {
			if(ftpClient.isConnected())
			   ftpClient.logout();
			   System.out.println("退出登录成功！");
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   System.out.println("退出登录失败！");
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
