package gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FolderViewItem extends Label{
	private boolean folder;
	private String name;
	private ContextMenu contextMenu = new ContextMenu();
	private MenuItem openItem = new MenuItem("��");
	private MenuItem downloadItem = new MenuItem("����");
	private MenuItem renameItem = new MenuItem("������");
	private MenuItem deleteItem = new MenuItem("ɾ��");
	public FolderViewItem(String name,boolean folder) {		//name�ǵ�ǰ
		this.name = name;
		this.folder = folder;
		
		this.setText(name);
		//����ͼ��
		if(folder) {
			this.setGraphic(new ImageView(new Image("file:folder icon.png"))); //�ļ���ͼ��
		}else {
			this.setGraphic(new ImageView(new Image("file:file icon.png")));    //�ļ�ͼ��
		}
		//�����Ҽ��˵�����
		if(folder) {
			this.contextMenu.getItems().addAll(openItem,renameItem,deleteItem); //�ļ���-�������ԣ��򿪣���������ɾ��
		}else {
			this.contextMenu.getItems().addAll(downloadItem,renameItem,deleteItem);  //�ļ�-�������ԣ����أ���������ɾ��
		}
		this.setContextMenu(contextMenu);
		
	}
	public boolean isFolder() {
		return folder;
	}
	public String getName() {  //folder=true�������ļ���·����folder=false,�����ļ�·��
		return name;
	}
	public MenuItem getOpenItem() {
		return openItem;
	}
	public MenuItem getDownloadItem() {
		return downloadItem;
	}
	public MenuItem getRenameItem() {
		return renameItem;
	}
	public MenuItem getDeleteItem() {
		return deleteItem;
	}
	
}
