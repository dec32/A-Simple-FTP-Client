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
	private Button backButton = new Button("←");
	private Button uploadButton = new Button("上传");
	private Button newFolderButton = new Button("新建文件夹");
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
		
		//三个按钮
		buttonPanel.getChildren().addAll(backButton, uploadButton, newFolderButton);
		buttonPanel.setSpacing(10);
		
		//主布局
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
		//设置列表的监听
		folderView.setOnMouseClicked(e->{
			if(e.getClickCount() == 2 && e.getButton() ==MouseButton.PRIMARY) {
				FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
				if(fvi.isFolder()) {
					on_openFolder(fvi.getName());
				}else {
					//双击文件，暂时什么都不做
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
	
	
	//为右键菜单点击设置事件响应
	//TODO（这一部分其实可以写到update里面，利用folderView.setOnContextMenuRequest）
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
	
	
	//事件响应
	
	//后退
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
				System.out.println("后退失败");
				return;
			}
			curFtpPath = pathToGo;
			update();
		}
	}
	
	//上传
	private void on_uploadButtonClicked() {
		FileChooser fc = new FileChooser();
		fc.setTitle("选择文件");
		File f = fc.showOpenDialog(new Stage());
		if(f==null) {
			return;
		}
		String localPath = f.getAbsolutePath();
		try {
			client.upload(localPath);
		} catch (Exception e) {
			System.out.println("上传失败");
			return;
		}
		
		System.out.println("上传文件："+localPath);
	}
	
	//新建文件夹
	private void on_newFolderButtonClicked() {
		System.out.println("新建文件夹");
		TypeWindow tw = new TypeWindow("新建文件夹");
		//TODO:冻结窗口，重命名文件夹和文件应该调用两个方法才对
		tw.showAndWait();
		String name = tw.getTypedString();
		try {
			client.md(name);
		} catch (Exception e) {
			System.out.println("新建文件夹失败");
			return;
		}
		
	}
	
	//下载
	private void on_downloadFile(String name) {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("选择保存目录");
		File file = dc.showDialog(new Stage());
		if(file == null) {
			return;
		}
		String localPath = file.getAbsolutePath();
		//这里先暂时把下载路径固定
		try {
			client.download(name, localPath);
		} catch (IOException e1) {
			System.out.println("下载失败");
		}
	}
	
	//重命名文件
	private void on_renameFile(String name) {
		TypeWindow tw = new TypeWindow("重命名");
		//TODO:冻结窗口，重命名文件夹和文件应该调用两个方法才对
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
		} catch (Exception e) {
			System.out.println("重命名失败");
			return;
		}
	}
	//删除文件
	private void on_deleteFile(String name) {
		try {
			client.delete(name);
		} catch (Exception e) {
			System.out.println("删除文件夹失败");
		}
	}
	//打开文件夹
	private void on_openFolder(String name) {
		String pathToGo;
		//生成新的路径
		if(curFtpPath.equals("/")) {
			pathToGo = curFtpPath+name;
		}else {
			pathToGo = curFtpPath+"/"+name;
		}
		//cd
		try {
			client.cd(pathToGo);
		} catch (Exception e1) {
			System.out.println("进入文件夹失败");
		}
		//更新当前路径
		curFtpPath = pathToGo;
		update();
	}
	//重命名文件夹
	private void on_renameFolder(String name) {
		TypeWindow tw = new TypeWindow("重命名");
		//TODO:冻结窗口，重命名文件夹和文件应该调用两个方法才对
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
		} catch (Exception e) {
			System.out.println("重命名失败");
			return;
		}
	}
	//删除文件夹
	private void on_deleteFolder(String name) {
		try {
			client.delete(name);
		} catch (Exception e) {
			System.out.println("删除文件夹失败");
		}
	}
	
	
}
