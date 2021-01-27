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
//		JsonParser jasonParser = new JsonParser(client);//Json������, ��ʱ����ʹ�õ�
//		Scanner sc = new Scanner(System.in);
//		/*
//		 * Ϊ�˷�����ԣ��ڱ����Ͽ���FTP����
//		 * Ȼ�������Լ����Ե��û��������룬���ӵ�localhost��21�˿ڣ�ftp�����Ĭ�϶˿ڣ����в���
//		 */
//		System.out.print("Username: ");
//		String username = sc.nextLine();
//		System.out.print("Password: ");
//		String password = sc.nextLine();
//		try {
//			client.login("localhost", 21, username, password);//��¼
//			client.cd("/");//ת����Ŀ¼
//			client.download("wave.jpg", "D:/ftp download");//���ظ�Ŀ¼��wave.jpg�ļ�
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
