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
	private MenuItem openItem = new MenuItem("打开");
	private MenuItem downloadItem = new MenuItem("下载");
	private MenuItem renameItem = new MenuItem("重命名");
	private MenuItem deleteItem = new MenuItem("删除");
	public FolderViewItem(String name,boolean folder) {		
		this.name = name;
		this.folder = folder;
		
		this.setText(name);
		//设置图标
		if(folder) {
			this.setGraphic(new ImageView(new Image("file:folder icon.png")));
		}else {
			this.setGraphic(new ImageView(new Image("file:file icon.png")));
		}
		//设置右键菜单
		if(folder) {
			this.contextMenu.getItems().addAll(openItem,renameItem,deleteItem);
		}else {
			this.contextMenu.getItems().addAll(downloadItem,renameItem,deleteItem);
		}
		this.setContextMenu(contextMenu);
		
	}
	public boolean isFolder() {
		return folder;
	}
	public String getName() {
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
