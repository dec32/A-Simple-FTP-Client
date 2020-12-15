package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RenameWindow extends Stage{
	private TextField newNameField = new TextField();
	private Button confirmButton=new Button("确定");
	private String newName;
	public RenameWindow() {
		initUI();
		setListener();
	}
	
	private void initUI() {
		this.setTitle("重命名");
		this.setWidth(280);
		this.setHeight(150);
		this.setResizable(false);
		VBox mainLayout = new VBox(newNameField,confirmButton);
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.setSpacing(20);
		mainLayout.setPadding(new Insets(20));
		this.setScene(new Scene(mainLayout));
	}
	
	private void setListener(){
		confirmButton.setOnAction(e->{
			newName = newNameField.getText();
			this.close();
		});
		newNameField.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				newName = newNameField.getText();
				this.close();
			}
		});
	}

	public String getNewName() {
		return newName;
	}
	
	
}
