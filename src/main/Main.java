package main;

import java.util.Scanner;

import core.Client;

public class Main {

	public static void main(String[] args) {
		Client c = new Client();
		Scanner sc = new Scanner(System.in);
		/*
		 * Ϊ�˷�����ԣ��ڱ����Ͽ���FTP����
		 * Ȼ�������Լ����Ե��û��������룬���ӵ�localhost��21�˿ڣ�ftp�����Ĭ�϶˿ڣ����в���
		 */
		System.out.print("Username:");
		String username = sc.nextLine();
		System.out.print("Password:");
		String password = sc.nextLine();
		c.login("localhost", 21, username, password);
		c.download("/", "wave.jpg", "D:/ftp download");
	}

}
