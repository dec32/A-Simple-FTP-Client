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


	public MainWindow(Client client) {
		this.client = client;
		initUI();
		update();
		setListener();

	}

	private void initUI() { //���������
		this.setResizable(false);
		this.setTitle("FTP �ͻ���");
		VBox mainLayout = new VBox();
		HBox buttonPanel = new HBox();

		//������ť
		buttonPanel.getChildren().addAll(backButton, uploadButton, newFolderButton);
		buttonPanel.setSpacing(10);

		//������
		mainLayout.getChildren().addAll(buttonPanel,folderView); //��ʱ��folderview�ǿ�ֵ����Ҫupdate����ܿ����ļ��б�
		mainLayout.setSpacing(10);
		mainLayout.setPadding(new Insets(10));
		Scene scene = new Scene(mainLayout);
//		scene.getStylesheets().add("file:///" + new File("css/style.css").getAbsolutePath().replace("\\", "/").replace(" ", "%20"));
		this.setScene(scene);

	}
	private void setListener() {
		backButton.setOnAction(e->{  //���˵ļ���
			on_backButtonClicked();
		});
		uploadButton.setOnAction(e->{ //�ϴ��ļ���
			on_uploadButtonClicked();
		});
		newFolderButton.setOnAction(e->{ //�½��ļ��еļ���
			on_newFolderButtonClicked();
		});
		//�����б�ļ���
		folderView.setOnMouseClicked(e->{ //�ļ��б�ļ���
			if(e.getClickCount() == 2 && e.getButton() ==MouseButton.PRIMARY) { //���������ļ��б��е�ĳ��Ԫ�����˫��
				FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
				if(fvi.isFolder()) { //������ļ���
					on_openFolder(fvi.getName());  //���ļ���
				}else {
					//˫���ļ�����ʱʲô������
				}
			}
		});
//		folderView.setOnContextMenuRequested(event->{
//			FolderViewItem fvi = folderView.getSelectionModel().getSelectedItem();
//			//�������б���Ҽ��˵�ʱ����ʾ��������б�����Ҽ��˵����������ü����¼�
//			if(fvi.isFolder()) {
//				fvi.getOpenItem().setOnAction(e->{
//					on_openFolder(fvi.getName());
//					System.out.println("��");
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
//					System.out.println("����");
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

	//ˢ���ļ��б�
	private void update() {
		System.out.println("update method");
		folderView.getItems().clear();  //����ļ��б�
		//System.out.println("client.getFolderList().size(): "+ client.getFolderList().size());
		for(String s:client.getFolderList()) {
			folderView.getItems().add(new FolderViewItem(s,true)); //�Ƚ��ļ���ͼ�����ȥ
		}
		for(String s:client.getFileList()) {
			folderView.getItems().add(new FolderViewItem(s,false)); //���ļ�ͼ�����ȥ
		}
		setOnContextMenuItemsClicked(); //�����������¼�
	}


	//Ϊ�Ҽ��˵���������¼���Ӧ
	//TODO����һ������ʵ����д��update���棬����folderView.setOnContextMenuRequest��
	private void setOnContextMenuItemsClicked() {
		for(FolderViewItem fvi:folderView.getItems()) { //�����ļ��б��е������ļ����ļ��еļ���
			if(fvi.isFolder()) {
				fvi.getOpenItem().setOnAction(e->{    //�ļ��д򿪵ļ�������
					on_openFolder(fvi.getName());
				});
				fvi.getRenameItem().setOnAction(e->{  //�ļ����������ļ���
					on_rename(fvi.getName());
//					on_openFolder(" ");   //ˢ�µ�ǰҳ��
				});
				fvi.getDeleteItem().setOnAction(e->{  //�ļ���ɾ���ļ���
					on_delete(fvi.getName());

//					on_openFolder(" ");   //ˢ�µ�ǰҳ��

				});
			}else {
				fvi.getDownloadItem().setOnAction(e->{ //�ļ����صļ���
					on_downloadFile(fvi.getName());

				});
				fvi.getRenameItem().setOnAction(e->{   //�ļ��������ļ���
					on_rename(fvi.getName());
//					on_openFolder(" ");   //ˢ�µ�ǰҳ��
				});
				fvi.getDeleteItem().setOnAction(e->{   //�ļ�ɾ���ļ���
					on_delete(fvi.getName());  //�����fvi.getnameֻ��һ�����·�������Ǿ���·��
//					on_openFolder(" ");   //ˢ�µ�ǰҳ��
				});
			}
		}


	}


	//�¼���Ӧ

	//����
	private void on_backButtonClicked() {
		try {
			System.out.println("���˰�ť�����");
			client.back();
			update();
		} catch (Exception e) {
			System.out.println("����ʧ��");
			return;
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
			update();
			System.out.println("�ϴ��ļ���"+localPath);
		} catch (Exception e) {
			System.out.println("�ϴ�ʧ��");
		}		
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
			update();
		} catch (Exception e) {
			System.out.println("�½��ļ���ʧ��");
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

	//ɾ���ļ�
	private void on_delete(String name) {
		try {
			client.delete(name);
			update();
		} catch (Exception e) {
			System.out.println("ɾ���ļ���ʧ��");
		}
	}
	//���ļ���
	private void on_openFolder(String name) {	
		try {
			client.cd(name);
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//�������ļ���
	private void on_rename(String name) {
		TypeWindow tw = new TypeWindow("������");
		//TODO:���ᴰ�ڣ��������ļ��к��ļ�Ӧ�õ������������Ŷ�
		tw.showAndWait();
		String newName = tw.getTypedString();
		try {
			client.rename(name, newName);
			update();
		} catch (Exception e) {
			System.out.println("������ʧ��");
		}
	}


}
