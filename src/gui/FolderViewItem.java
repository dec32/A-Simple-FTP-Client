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
	public FolderViewItem(String name,boolean folder) {		//name是当前
		this.name = name;
		this.folder = folder;
		
		this.setText(name);
		//设置图标
		if(folder) {
			this.setGraphic(new ImageView(new Image("file:folder icon.png"))); //文件夹图标
		}else {
			this.setGraphic(new ImageView(new Image("file:file icon.png")));    //文件图标
		}
		//设置右键菜单属性
		if(folder) {
			this.contextMenu.getItems().addAll(openItem,renameItem,deleteItem); //文件夹-三个属性：打开，重命名，删除
		}else {
			this.contextMenu.getItems().addAll(downloadItem,renameItem,deleteItem);  //文件-三个属性：下载，重命名，删除
		}
		this.setContextMenu(contextMenu);
		
	}
	public boolean isFolder() {
		return folder;
	}
	public String getName() {  //folder=true，则是文件夹路径，folder=false,则是文件路径
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
