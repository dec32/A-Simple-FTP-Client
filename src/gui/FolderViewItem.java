package gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FolderViewItem extends Label{
	boolean folder;
	String name;
	public FolderViewItem(String name,boolean folder) {		
		this.name = name;
		this.folder = folder;
		
		this.setText(name);
		if(folder) {
			this.setGraphic(new ImageView(new Image("file:folder icon.png")));
		}else {
			this.setGraphic(new ImageView(new Image("file:file icon.png")));
		}
	}
	public boolean isFolder() {
		return folder;
	}
	public String getName() {
		return name;
	}
		
}
