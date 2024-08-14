package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller for the Connect to Server page in the application. This class
 * handles the connection to the server by validating the IP address entered by
 * the user, and manages the transition to the LoginController page upon
 * successful connection.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ConnectToServerController implements IControl, Initializable {

	/**
	 * Button that exits the application when clicked.
	 */
	@FXML
	private Button btnExit;

	/**
	 * Button that initiates the connection to the server using the provided IP
	 * address.
	 */
	@FXML
	private Button btnConnect;

	/**
	 * Label that displays messages or alerts to the user.
	 */
	@FXML
	private Label messagelbl;

	/**
	 * TextField where the user inputs the server's IP address.
	 */
	@FXML
	private TextField iptxt;

	/**
	 * Closes the application when the exit button is clicked.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	public void getBackBtn(ActionEvent event) {
		System.exit(0);
	}

	/**
	 * Validates if the given IP address is in the correct format.
	 * 
	 * @param ip The IP address to be validated.
	 * @return True if the IP address is valid; false otherwise.
	 */
	private boolean isValidIPAddress(String ip) {
		String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
				+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		return Pattern.compile(ipPattern).matcher(ip).matches();
	}

	/**
	 * Connects to the server using the IP address entered by the user, and
	 * transitions to the LoginController page upon successful connection.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the connection process.
	 */
	@FXML
	void connectToServer(ActionEvent event) throws Exception {
		String ip = iptxt.getText();

		if (ip.trim().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Enter IP");
			a.showAndWait();
		} else if (!isValidIPAddress(ip)) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Enter a valid IP");
			a.showAndWait();
		} else if (!InetAddress.getByName(ip).isReachable(1000)) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("The IP you entered is unreachable");
			a.showAndWait();
		} else {
			ClientUI.chat = new ClientController(ip, 5555);
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window

			System.out.println("Connecting to LoginController");

			LoginController lc = new LoginController();
			InetAddress thisIP = InetAddress.getLocalHost();
			ArrayList<Object> inputList = new ArrayList<>();
			inputList.add(thisIP.getHostName());
			inputList.add(thisIP.getHostAddress());
			Message msg = new Message(MessageType.ClientConnected, inputList);
			ClientUI.chat.accept(msg);

			System.out.println("Starting LoginController");
			lc.start(new Stage());
		}

	}
	/**
	 * Starts the ConnectToServerController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the stage.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ConnectToServer.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ConnectToServer.css").toExternalForm());
		primaryStage.setTitle("Connect to server page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Initializes the controller after its root element has been completely
	 * processed.
	 * 
	 * @param arg0 The location used to resolve relative paths for the root object,
	 *             or null if the location is not known.
	 * @param arg1 The resources used to localize the root object, or null if the
	 *             root object was not localized.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ChatClient.currController = this;
		iptxt.setText("192.168.31.58");
	}
	/**
	 * Displays an alert with the specified title and message.
	 * 
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
	public void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
