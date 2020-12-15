package gui;

import java.io.IOException;
import java.net.SocketException;

import core.Client;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends Stage{
	private Client client;
	
	private Label usernameLabel = new Label("Username: ");
	private TextField usernameField = new TextField("10641");
	private Label passwordLabel = new Label("Password: ");
	private TextField passwordField = new TextField();
	private Label addressLabel = new Label("Address: ");
	private TextField addressField = new TextField("localhost");
	private Label portLabel = new Label("Port: ");
	private TextField portField = new TextField("21");
	private Button loginButton = new Button("Login");
	
	public LoginWindow(Client client) {
		this.client = client;
		initUI();
		setListener();
	}
	private void initUI() {
		this.setTitle("Login");
		//设置布局
		VBox mainLayout = new VBox();
		GridPane loginPanel = new GridPane();
		HBox connectionPanel = new HBox();
		HBox buttonPanel = new HBox();
		
		loginPanel.add(usernameLabel, 0, 0);
		loginPanel.add(usernameField, 1, 0);
		loginPanel.add(passwordLabel, 0, 1);
		loginPanel.add(passwordField, 1, 1);
		loginPanel.setVgap(10);
		loginPanel.setHgap(10);
		
		connectionPanel.getChildren().addAll(addressLabel,addressField,portLabel,portField);		
		connectionPanel.setSpacing(15);
		
		buttonPanel.getChildren().add(loginButton);
		
		mainLayout.getChildren().addAll(loginPanel,connectionPanel,buttonPanel);
		mainLayout.setSpacing(15);
		mainLayout.setPadding(new Insets(10));
		
		this.setScene(new Scene(mainLayout));
		
		
		
	}
	
	private void setListener() {
		//设置监听
		loginButton.setOnAction(e->{
			on_loginButtonClicked();
		});
				
	}
	
	private void on_loginButtonClicked() {
		System.out.println("login button is clicked");
		String username = usernameField.getText();
		String password = passwordField.getText();
		String address = addressField.getText();
		int port = Integer.valueOf(portField.getText());
		
		try {
			client.login(address, port, username, password);
			client.cd("/");
		} catch (Exception e) {
			this.setTitle("Login failed");
		}
		this.close();
		MainWindow mw = new MainWindow(client);
		mw.show();
	}
	
}
