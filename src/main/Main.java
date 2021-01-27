package main;

import java.util.Scanner;

import core.Client;
import gui.LoginWindow;
import gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import net.sf.json.JSONObject;

public class Main extends Application{

	public static void main(String[] args) {
		
		launch();		
//		Client client = new Client();
//		JsonParser jasonParser = new JsonParser(client);//Json解释器, 暂时不会使用到
//		Scanner sc = new Scanner(System.in);
//		/*
//		 * 为了方便测试，在本机上开启FTP服务
//		 * 然后输入自己电脑的用户名和密码，连接到localhost的21端口（ftp服务的默认端口）进行测试
//		 */
//		System.out.print("Username: ");
//		String username = sc.nextLine();
//		System.out.print("Password: ");
//		String password = sc.nextLine();
//		try {
//			client.login("localhost", 21, username, password);//登录
//			client.cd("/");//转到根目录
//			client.download("wave.jpg", "D:/ftp download");//下载根目录的wave.jpg文件
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Client client = new Client();
		LoginWindow lw = new LoginWindow(client);
		lw.show();	
	}

}
