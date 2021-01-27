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


	public MainWindow(Client client) {
		this.client = client;
		initUI();
		update();
		setListener();

	}

	private void initUI() { //设置主面板
		this.setResizable(false);
		this.setTitle("FTP 客户端");
		VBox mainLayout = new VBox();
		HBox buttonPanel = new HBox();

		//三个按钮
		buttonPanel.getChildren().addAll(backButton, uploadButton, newFolderButton);
		buttonPanel.setSpacing(10);

		//主布局
		mainLayout.getChildren().addAll(buttonPanel,folderView); //此时的folderview是空值，需要update后才能看到文件列表
		mainLayout.setSpacing(10);
		mainLayout.setPadding(new Insets(10));
		Scene scene = new Scene(mainLayout);
//		scene.getStylesheets().add("file:///" + new File("css/style.css").getAbsolutePath().replace("\\", "/").replace(" ", "%20"));
		this.setScene(scene);

	}
	private void setListener() {
		backButton.setOnAction(e->{  //回退的监听
			on_backButtonClicked();
		});
		uploadButton.setOnAction(e->{ //上传的监听
			on_uploadButtonClicked();
		});
		newFolderButton.setOnAction(e->{ //新建文件夹的监听
			on_newFolderButtonClicked();
		});
		//设置列表的监听
		folderView.setOnMouseClicked(e->{ //文件列表的监听
			if(e.getClickCount() == 2 && e.getButton() ==MouseButton.PRIMARY) { //监听到对文件列表中的某个元素左键双击
				FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
				if(fvi.isFolder()) { //如果是文件夹
					on_openFolder(fvi.getName());  //打开文件夹
				}else {
					//双击文件，暂时什么都不做
				}
			}
		});
//		folderView.setOnContextMenuRequested(event->{
//			FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
//			//当请求列表的右键菜单时，显示被点击的列表项的右键菜单，并且设置监听事件
//			if(fvi.isFolder()) {
//				fvi.getOpenItem().setOnAction(e->{
//					on_openFolder(fvi.getName());
//					System.out.println("打开");
//				});
//				fvi.getRenameItem().setOnAction(e->{
//					on_renameFolder(fvi.getName());
//				});
//				fvi.getDeleteItem().setOnAction(e->{
//					on_deleteFolder(fvi.getName());
//				});
//			}else {
//				fvi.getDownloadItem().setOnAction(e->{
//					on_downloadFile(fvi.getName());
//					System.out.println("下载");
//				});
//				fvi.getRenameItem().setOnAction(e->{
//					on_renameFile(fvi.getName());
//				});
//				fvi.getDeleteItem().setOnAction(e->{
//					on_deleteFile(fvi.getName());
//				});
//			}
//			fvi.getContextMenu().show(folderView,event.getScreenX(),event.getScreenY());
//			event.consume();
//		});
	}

	//刷新文件列表
	private void update() {
		System.out.println("update method");
		folderView.getItems().clear();  //清空文件列表
		//System.out.println("client.getFolderList().size(): "+ client.getFolderList().size());
		for(String s:client.getFolderList()) {
			folderView.getItems().add(new FolderViewItem(s,true)); //先将文件夹图标放上去
		}
		for(String s:client.getFileList()) {
			folderView.getItems().add(new FolderViewItem(s,false)); //将文件图标放上去
		}
		setOnContextMenuItemsClicked(); //设置鼠标监听事件
	}


	//为右键菜单点击设置事件响应
	//TODO（这一部分其实可以写到update里面，利用folderView.setOnContextMenuRequest）
	private void setOnContextMenuItemsClicked() {
		for(FolderViewItem fvi:folderView.getItems()) { //对于文件列表中的所有文件，文件夹的监听
			if(fvi.isFolder()) {
				fvi.getOpenItem().setOnAction(e->{    //文件夹打开的监听操作
					on_openFolder(fvi.getName());
				});
				fvi.getRenameItem().setOnAction(e->{  //文件夹重命名的监听
					on_rename(fvi.getName());
//					on_openFolder(" ");   //刷新当前页面
				});
				fvi.getDeleteItem().setOnAction(e->{  //文件夹删除的监听
					on_delete(fvi.getName());

//					on_openFolder(" ");   //刷新当前页面

				});
			}else {
				fvi.getDownloadItem().setOnAction(e->{ //文件下载的监听
					on_downloadFile(fvi.getName());

				});
				fvi.getRenameItem().setOnAction(e->{   //文件重命名的监听
					on_rename(fvi.getName());
//					on_openFolder(" ");   //刷新当前页面
				});
				fvi.getDeleteItem().setOnAction(e->{   //文件删除的监听
					on_delete(fvi.getName());  //这里的fvi.getname只是一个相对路径，不是绝对路径
//					on_openFolder(" ");   //刷新当前页面
				});
			}
		}


	}


	//事件响应

	//后退
	private void on_backButtonClicked() {
		try {
			System.out.println("后退按钮被点击");
			client.back();
			update();
		} catch (Exception e) {
			System.out.println("后退失败");
			return;
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
			update();
			System.out.println("上传文件："+localPath);
		} catch (Exception e) {
			System.out.println("上传失败");
		}		
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
			update();
		} catch (Exception e) {
			System.out.println("新建文件夹失败");
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

	//删除文件
	private void on_delete(String name) {
		try {
			client.delete(name);
			update();
		} catch (Exception e) {
			System.out.println("删除文件夹失败");
		}
	}
	//打开文件夹
	private void on_openFolder(String name) {	
		try {
			client.cd(name);
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//重命名文件夹
	private void on_rename(String name) {
		TypeWindow tw = new TypeWindow("重命名");
		//TODO:冻结窗口，重命名文件夹和文件应该调用两个方法才对
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
			update();
		} catch (Exception e) {
			System.out.println("重命名失败");
		}
	}


}
