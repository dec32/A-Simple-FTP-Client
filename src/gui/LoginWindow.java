package gui;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import core.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends Stage{
	private Client client;
	
	private Label usernameLabel = new Label("�û���: ");
	private TextField usernameField = new TextField("localhost");
	private Label passwordLabel = new Label("����: ");
	private PasswordField passwordField = new PasswordField();
	private Label addressLabel = new Label("��ַ: ");
	private TextField addressField = new TextField("localhost");
	private Label portLabel = new Label("�˿�: ");
	private TextField portField = new TextField("21");
	private Button loginButton = new Button("��¼");
	
	public LoginWindow(Client client) {
		this.client = client;
		initUI();
		setListener();
	}
	private void initUI() {
		this.setTitle("��¼");
//		this.setWidth(400);
//		this.setHeight(250);
		this.setResizable(false);
		//���ò���
		VBox mainLayout = new VBox();
		GridPane loginPanel = new GridPane();
		HBox connectionPanel = new HBox();
		HBox buttonPanel = new HBox();
		
		//�û���������
		loginPanel.add(usernameLabel, 0, 0);
		loginPanel.add(usernameField, 1, 0);
		loginPanel.add(passwordLabel, 0, 1);
		loginPanel.add(passwordField, 1, 1);
		loginPanel.add(addressLabel, 0, 2);
		loginPanel.add(addressField, 1, 2);
		loginPanel.add(portLabel, 0, 3);
		loginPanel.add(portField, 1, 3);
		loginPanel.setVgap(10);
		loginPanel.setHgap(0);

//		//��ַ�Ͷ˿ں�
//		connectionPanel.getChildren().addAll(addressLabel,addressField,portLabel,portField);		
//		connectionPanel.setSpacing(15);
//		portField.setPrefWidth(30);
		//��¼��ť
		buttonPanel.getChildren().add(loginButton);
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		//������
		mainLayout.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				on_loginButtonClicked();
			}
		});
		mainLayout.getChildren().addAll(loginPanel,buttonPanel);
		mainLayout.setSpacing(15);
		mainLayout.setPadding(new Insets(25,25,10,25));
		Scene scene = new Scene(mainLayout);
//		scene.getStylesheets().add("file:///" + new File("css/style.css").getAbsolutePath().replace("\\", "/").replace(" ", "%20"));
		this.setScene(scene);
		
		
		
	}
	
	private void setListener() {
		//���ü���
		loginButton.setOnAction(e->{
			on_loginButtonClicked();
		});
				
	}
	
	private void on_loginButtonClicked() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		String address = addressField.getText();
		int port = Integer.valueOf(portField.getText());
		
		try {
			client.login(address, port, username, password);
			client.cd("/");
		} catch (Exception e) {
			this.setTitle("��¼ʧ��");
			return;
		}
		this.close();
		MainWindow mw = new MainWindow(client);
		mw.show();
	}
	
}
