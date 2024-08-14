package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import logic.UserType;

/**
 * Controller for the Login page in the application. This class handles user
 * authentication, manages the transition to the appropriate home page based on
 * user type, and provides methods for displaying alerts and handling back
 * navigation.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class LoginController implements Initializable, IControl {
	/**
	 * Button that exits the application when clicked.
	 */
	@FXML
	private Button btnExit = null;

	/**
	 * Button that initiates the login process when clicked.
	 */
	@FXML
	private Button btnLogin = null;

	/**
	 * TextField where the user inputs their username.
	 */
	@FXML
	private TextField Usernametxt;

	/**
	 * TextField where the user inputs their password.
	 */
	@FXML
	private TextField Passwordtxt;

	/**
	 * ComboBox for selecting the user type (e.g., Customer, Manager).
	 */
	@FXML
	private ComboBox<String> UserTypecb;

	/**
	 * Label that displays messages or alerts to the user.
	 */
	@FXML
	private Label messagelbl;

	/**
	 * Initializes the controller by setting default values and populating the user
	 * type combo box.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		UserTypecb.getItems().addAll("Customer", "Manager", "CEO", "Supplier");
		ChatClient.currController = this;
		Usernametxt.setText("alice");
		Passwordtxt.setText("password123");
	}

	/**
	 * Handles the login action by validating user input and connecting to the
	 * server to authenticate the user. Opens the appropriate home page based on the
	 * user type upon successful login.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the login process.
	 */
	public void login(ActionEvent event) throws Exception {
		String username = Usernametxt.getText();
		String password = Passwordtxt.getText();
		UserType ut = UserType.getUserType(UserTypecb.getValue());
		if (username.trim().isEmpty()) {
			showAlert("Validation Error", "All fields must be filled.");
		} else if (password.trim().isEmpty()) {
			showAlert("Validation Error", "All fields must be filled.");
		} else if (ut == null) {
			showAlert("Validation Error", "All fields must be filled.");
		} else {
			ArrayList<Object> inputList = new ArrayList<>();
			inputList.add(username);
			inputList.add(password);
			inputList.add(UserType.getStringType(ut));
			Message msg = new Message(MessageType.UserConnect, inputList);
			ClientUI.chat.accept(msg);
			String retunedMessage = ChatClient.lastAction;
			System.out.println(retunedMessage);
			if (retunedMessage.equals("User not found")) {
				showAlert("Validation Error", "User not found.");
			} else if (retunedMessage.equals("User is already logged in")) {
				showAlert("Validation Error", "User is already logged in.");
			} else if (retunedMessage.equals("User is valid")) {
				openUserHomePage(event);
			}
		}
	}

	/**
	 * Starts the stage for the login page and sets up the scene.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Login.css").toExternalForm());
		primaryStage.setTitle("Login Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of going back to the previous view or exiting the
	 * application.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the back navigation process.
	 */
	public void getBackBtn(ActionEvent event) throws Exception {
		InetAddress ip = InetAddress.getLocalHost();
		ArrayList<Object> inputList = new ArrayList<>();
		inputList.add(ip.getHostName());
		Message msg = new Message(MessageType.ClientDisconnected, inputList);
		ClientUI.chat.accept(msg);
		System.exit(0);
	}

	/**
	 * Opens the appropriate home page based on the user's type after successful
	 * login.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the transition to the home
	 *                   page.
	 */
	public void openUserHomePage(ActionEvent event) throws Exception {
		UserType type = ChatClient.currUser.getUserType();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		switch (type) {
		case Customer:
			CustomerHomePageController chp = new CustomerHomePageController();
			chp.start(new Stage());
			break;
		case CEO:
			CEOHomePageController chpc = new CEOHomePageController();
			chpc.start(new Stage());
			break;
		case Manager:
			ManagerHomePageController mhpc = new ManagerHomePageController();
			mhpc.start(new Stage());
			break;
		case Supplier:
			SupplierHomePageController shp = new SupplierHomePageController();
			shp.start(new Stage());
			break;
		default:
			break;
		}

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
