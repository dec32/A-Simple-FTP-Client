package gui;


import java.awt.TextField;
import java.io.File;
import java.io.IOException;

import core.Client;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Stage{
	private Client client;
	private Button backButton = new Button("��");
	private Button uploadButton = new Button("�ϴ�");
	private Button newFolderButton = new Button("�½��ļ���");
	private ListView<FolderViewItem> folderView = new ListView<FolderViewItem>();
	private String curFtpPath = "/";
	
	public MainWindow(Client client) {
		this.client = client;
		initUI();
		update();
		setListener();
		
	}
	
	private void initUI() {
		this.setResizable(false);
		VBox mainLayout = new VBox();
		HBox buttonPanel = new HBox();
		
		//������ť
		buttonPanel.getChildren().addAll(backButton, uploadButton, newFolderButton);
		buttonPanel.setSpacing(10);
		
		//������
		mainLayout.getChildren().addAll(buttonPanel,folderView);
		mainLayout.setSpacing(10);
		mainLayout.setPadding(new Insets(10));
		this.setScene(new Scene(mainLayout));
		
	}
	private void setListener() {
		backButton.setOnAction(e->{
			on_backButtonClicked();
		});
		uploadButton.setOnAction(e->{
			on_uploadButtonClicked();
		});
		newFolderButton.setOnAction(e->{
			on_newFolderButtonClicked();
		});
		//�����б�ļ���
		folderView.setOnMouseClicked(e->{
			if(e.getClickCount() == 2 && e.getButton() ==MouseButton.PRIMARY) {
				FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
				if(fvi.isFolder()) {
					on_openFolder(fvi.getName());
				}else {
					//˫���ļ�����ʱʲô������
				}
			}
		});
	}
	
	private void update() {
		this.setTitle(curFtpPath);
		folderView.getItems().clear();
		for(String s:client.getFolderList()) {
			folderView.getItems().add(new FolderViewItem(s,true));
		}
		for(String s:client.getFileList()) {
			folderView.getItems().add(new FolderViewItem(s,false));
		}
		setOnContextMenuItemsClicked();
	}
	
	
	//Ϊ�Ҽ��˵���������¼���Ӧ
	//TODO����һ������ʵ����д��update���棬����folderView.setOnContextMenuRequest��
	private void setOnContextMenuItemsClicked() {
		for(FolderViewItem fvi:folderView.getItems()) {
			if(fvi.isFolder()) {
				fvi.getOpenItem().setOnAction(e->{
					on_openFolder(fvi.getName());
				});
				fvi.getRenameItem().setOnAction(e->{
					on_renameFolder(fvi.getName());
				});
				fvi.getDeleteItem().setOnAction(e->{
					on_deleteFolder(fvi.getName());
				});
			}else {
				fvi.getDownloadItem().setOnAction(e->{
					on_downloadFile(fvi.getName());
				});
				fvi.getRenameItem().setOnAction(e->{
					on_renameFile(fvi.getName());
				});
				fvi.getDeleteItem().setOnAction(e->{
					on_deleteFile(fvi.getName());
				});
			}
		}
	}
	
	
	//�¼���Ӧ
	
	//����
	private void on_backButtonClicked() {
		if(curFtpPath.equals("/")) {
			return;
		}else {
			String splited[] = curFtpPath.split("/");
			String pathToGo = "";
			for (int i = 1; i < splited.length-1; i++) {
				pathToGo+="/";
				pathToGo+=splited[i];
			}
			if(pathToGo.equals("")) {
				pathToGo = "/";
			}
			try {
				client.cd(pathToGo);
			} catch (IOException e) {
				System.out.println("����ʧ��");
				return;
			}
			curFtpPath = pathToGo;
			update();
		}
	}
	
	//�ϴ�
	private void on_uploadButtonClicked() {
		FileChooser fc = new FileChooser();
		fc.setTitle("ѡ���ļ�");
		File f = fc.showOpenDialog(new Stage());
		if(f==null) {
			return;
		}
		String localPath = f.getAbsolutePath();
		try {
			client.upload(localPath);
		} catch (Exception e) {
			System.out.println("�ϴ�ʧ��");
			return;
		}
		
		System.out.println("�ϴ��ļ���"+localPath);
	}
	
	//�½��ļ���
	private void on_newFolderButtonClicked() {
		System.out.println("�½��ļ���");
		TypeWindow tw = new TypeWindow("�½��ļ���");
		//TODO:���ᴰ�ڣ��������ļ��к��ļ�Ӧ�õ������������Ŷ�
		tw.showAndWait();
		String name = tw.getTypedString();
		try {
			client.md(name);
		} catch (Exception e) {
			System.out.println("�½��ļ���ʧ��");
			return;
		}
		
	}
	
	//����
	private void on_downloadFile(String name) {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("ѡ�񱣴�Ŀ¼");
		File file = dc.showDialog(new Stage());
		if(file == null) {
			return;
		}
		String localPath = file.getAbsolutePath();
		//��������ʱ������·���̶�
		try {
			client.download(name, localPath);
		} catch (IOException e1) {
			System.out.println("����ʧ��");
		}
	}
	
	//�������ļ�
	private void on_renameFile(String name) {
		TypeWindow tw = new TypeWindow("������");
		//TODO:���ᴰ�ڣ��������ļ��к��ļ�Ӧ�õ������������Ŷ�
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
		} catch (Exception e) {
			System.out.println("������ʧ��");
			return;
		}
	}
	//ɾ���ļ�
	private void on_deleteFile(String name) {
		try {
			client.delete(name);
		} catch (Exception e) {
			System.out.println("ɾ���ļ���ʧ��");
		}
	}
	//���ļ���
	private void on_openFolder(String name) {
		String pathToGo;
		//�����µ�·��
		if(curFtpPath.equals("/")) {
			pathToGo = curFtpPath+name;
		}else {
			pathToGo = curFtpPath+"/"+name;
		}
		//cd
		try {
			client.cd(pathToGo);
		} catch (Exception e1) {
			System.out.println("�����ļ���ʧ��");
		}
		//���µ�ǰ·��
		curFtpPath = pathToGo;
		update();
	}
	//�������ļ���
	private void on_renameFolder(String name) {
		TypeWindow tw = new TypeWindow("������");
		//TODO:���ᴰ�ڣ��������ļ��к��ļ�Ӧ�õ������������Ŷ�
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
		} catch (Exception e) {
			System.out.println("������ʧ��");
			return;
		}
	}
	//ɾ���ļ���
	private void on_deleteFolder(String name) {
		try {
			client.delete(name);
		} catch (Exception e) {
			System.out.println("ɾ���ļ���ʧ��");
		}
	}
	
	
}
