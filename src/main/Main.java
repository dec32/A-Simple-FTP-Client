package main;

import java.util.Scanner;

import core.Client;

public class Main {

	public static void main(String[] args) {
		Client c = new Client();
		Scanner sc = new Scanner(System.in);
		/*
		 * 为了方便测试，在本机上开启FTP服务
		 * 然后输入自己电脑的用户名和密码，连接到localhost的21端口（ftp服务的默认端口）进行测试
		 */
		System.out.print("Username:");
		String username = sc.nextLine();
		System.out.print("Password:");
		String password = sc.nextLine();
		c.login("localhost", 21, username, password);
		c.download("/", "wave.jpg", "D:/ftp download");
	}

}
